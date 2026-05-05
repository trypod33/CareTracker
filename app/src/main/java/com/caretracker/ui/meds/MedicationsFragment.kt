package com.caretracker.ui.meds

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.caretracker.CareTrackerApp
import com.caretracker.data.entities.MedicationEntity
import com.caretracker.data.entities.UserEntity
import com.caretracker.databinding.DialogAddMedicationBinding
import com.caretracker.databinding.FragmentMedicationsBinding
import com.google.android.material.chip.Chip
import kotlinx.coroutines.launch

class MedicationsFragment : Fragment() {

    private var _binding: FragmentMedicationsBinding? = null
    private val binding get() = _binding!!

    private val vm: MedicationsViewModel by viewModels {
        MedicationsViewModel.Factory((requireActivity().application as CareTrackerApp).repository)
    }

    private lateinit var adapter: MedicationAdapter
    private var currentUserId: Long = -1L

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMedicationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MedicationAdapter(
            onEdit = { showAddEditDialog(it.med) },
            onDelete = { confirmDelete(it.med) },
            onTake = { vm.takeMed(it.med) },
            onRefill = { vm.refillMed(it.med) },
            onStartDrag = { }
        )

        binding.rvMedications.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMedications.adapter = adapter
        binding.btnAddMedication.setOnClickListener { showAddEditDialog(null) }

        viewLifecycleOwner.lifecycleScope.launch {
            vm.filteredMeds.collect { list ->
                adapter.submitItems(list)
                binding.tvEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            vm.filterOptions.collect { options ->
                rebuildFilterChips(options)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            vm.filter.collect { active ->
                binding.chipGroupFilter.children.filterIsInstance<Chip>().forEach { chip ->
                    chip.isChecked = chip.text.toString() == active
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            (requireActivity().application as CareTrackerApp).currentUserIdFlow.collect { userId ->
                currentUserId = userId
                vm.loadForUser(userId)
                syncFilterToCurrentUser()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            vm.users.collect {
                syncFilterToCurrentUser()
            }
        }
    }

    private fun rebuildFilterChips(options: List<String>) {
        val current = vm.filter.value
        binding.chipGroupFilter.removeAllViews()

        options.forEach { label ->
            val chip = Chip(requireContext()).apply {
                text = label
                isCheckable = true
                isChecked = label == current
                setOnClickListener { vm.setFilter(label) }
            }
            binding.chipGroupFilter.addView(chip)
        }
    }

    private fun syncFilterToCurrentUser() {
        val users = vm.users.value.filter { it.isActive }
        if (users.isEmpty() || currentUserId <= 0L) return

        val selectedUser = users.firstOrNull { it.id == currentUserId }
        val label = selectedUser?.displayName?.trim()?.takeIf { it.isNotBlank() } ?: selectedUser?.username ?: "All"

        val availableOptions = vm.filterOptions.value
        if (label in availableOptions) {
            vm.setFilter(label)
        } else {
            vm.setFilter("All")
        }
    }

    private fun showAddEditDialog(existing: MedicationEntity?) {
        val db = DialogAddMedicationBinding.inflate(layoutInflater)
        val users = vm.users.value.filter { it.isActive }
        val userLabels = users.map { u -> displayLabel(u) }

        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, userLabels)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        db.spinnerPerson.adapter = spinnerAdapter

        val targetUserId = existing?.userId ?: currentUserId.takeIf { it > 0 } ?: vm.getCurrentUserId()
        val selectedIndex = users.indexOfFirst { it.id == targetUserId }.coerceAtLeast(0)
        db.spinnerPerson.setSelection(selectedIndex)

        existing?.let { med ->
            db.etMedName.setText(med.name)
            db.etGenericName.setText(med.genericName)
            db.etDosage.setText(med.dosage)
            db.etDosageUnit.setText(med.dosageUnit)
            db.etForm.setText(med.form)
            db.etFrequency.setText(med.frequency)
            db.etTimesPerDay.setText(med.timesPerDay.toString())
            db.etScheduledTimes.setText(med.scheduledTimes)
            db.cbWithFood.isChecked = med.withFood
            db.etInstructions.setText(med.instructions)
            db.etPrescriber.setText(med.prescriber)
            db.etPharmacy.setText(med.pharmacy)
            db.etRxNumber.setText(med.rxNumber)
            db.etCurrentCount.setText(med.currentCount.toString())
            db.etPillsPerRefill.setText(med.pillsPerRefill?.toString() ?: "30")
            db.etRefillReminder.setText(med.refillReminderAt.toString())
            mapOf(
                "#4f9cf9" to db.rbBlue,
                "#9b59b6" to db.rbPurple,
                "#2ecc71" to db.rbGreen,
                "#e67e22" to db.rbOrange,
                "#e74c3c" to db.rbRed,
                "#e91e8c" to db.rbPink,
                "#ff9800" to db.rbAmber,
                "#00bcd4" to db.rbCyan
            )[med.color]?.isChecked = true
        }

        AlertDialog.Builder(requireContext())
            .setTitle(if (existing != null) "Edit Medication" else "Add Medication")
            .setView(db.root)
            .setPositiveButton(if (existing != null) "Save" else "Add") { _, _ ->
                val color = when {
                    db.rbBlue.isChecked   -> "#4f9cf9"
                    db.rbPurple.isChecked -> "#9b59b6"
                    db.rbGreen.isChecked  -> "#2ecc71"
                    db.rbOrange.isChecked -> "#e67e22"
                    db.rbRed.isChecked    -> "#e74c3c"
                    db.rbPink.isChecked   -> "#e91e8c"
                    db.rbAmber.isChecked  -> "#ff9800"
                    db.rbCyan.isChecked   -> "#00bcd4"
                    else -> "#4f9cf9"
                }

                vm.saveMed(
                    MedicationEntity(
                        id = existing?.id ?: 0L,
                        userId = users.getOrNull(db.spinnerPerson.selectedItemPosition)?.id
                            ?: currentUserId.takeIf { it > 0 }
                            ?: vm.getCurrentUserId(),
                        name = db.etMedName.text.toString().trim(),
                        genericName = db.etGenericName.text.toString().takeIf { it.isNotBlank() },
                        dosage = db.etDosage.text.toString().takeIf { it.isNotBlank() },
                        dosageUnit = db.etDosageUnit.text.toString().ifBlank { "mg" },
                        form = db.etForm.text.toString().ifBlank { "tablet" },
                        frequency = db.etFrequency.text.toString().takeIf { it.isNotBlank() },
                        timesPerDay = db.etTimesPerDay.text.toString().toIntOrNull() ?: 1,
                        scheduledTimes = db.etScheduledTimes.text.toString().takeIf { it.isNotBlank() },
                        withFood = db.cbWithFood.isChecked,
                        instructions = db.etInstructions.text.toString().takeIf { it.isNotBlank() },
                        prescriber = db.etPrescriber.text.toString().takeIf { it.isNotBlank() },
                        pharmacy = db.etPharmacy.text.toString().takeIf { it.isNotBlank() },
                        rxNumber = db.etRxNumber.text.toString().takeIf { it.isNotBlank() },
                        currentCount = db.etCurrentCount.text.toString().toIntOrNull() ?: 0,
                        pillsPerRefill = db.etPillsPerRefill.text.toString().toIntOrNull() ?: 30,
                        refillReminderAt = db.etRefillReminder.text.toString().toIntOrNull() ?: 7,
                        color = color,
                        isActive = true
                    )
                )
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun displayLabel(user: UserEntity): String {
        return user.displayName?.trim()?.takeIf { it.isNotBlank() } ?: user.username
    }

    private fun confirmDelete(med: MedicationEntity) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete ${med.name}?")
            .setMessage("This will remove the medication and cannot be undone.")
            .setPositiveButton("Delete") { _, _ -> vm.deleteMed(med) }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
