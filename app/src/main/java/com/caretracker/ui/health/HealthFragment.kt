package com.caretracker.ui.health

import android.app.*
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.*
import com.caretracker.R
import com.caretracker.CareTrackerApp
import com.caretracker.data.entities.HealthEntryEntity
import com.caretracker.databinding.DialogLogHealthBinding
import com.caretracker.databinding.FragmentHealthBinding
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Calendar

class HealthFragment : Fragment() {

    private var _b: FragmentHealthBinding? = null
    private val b get() = _b!!

    private val vm: HealthViewModel by viewModels {
        HealthViewModel.Factory((requireActivity().application as CareTrackerApp).repository)
    }

    private lateinit var adapter: HealthHistoryAdapter

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _b = FragmentHealthBinding.inflate(i, c, false)
        return b.root
    }

    override fun onViewCreated(view: View, s: Bundle?) {
        super.onViewCreated(view, s)

        b.tvHealthDate.text = LocalDate.now()
            .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL))

        adapter = HealthHistoryAdapter({ showDialog(it) }, { confirmDelete(it) })
        b.rvHealthHistory.layoutManager = LinearLayoutManager(requireContext())
        b.rvHealthHistory.adapter = adapter

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(rv: RecyclerView, v: RecyclerView.ViewHolder, t: RecyclerView.ViewHolder) = false
            override fun onSwiped(vh: RecyclerView.ViewHolder, d: Int) {
                confirmDelete(adapter.currentList[vh.adapterPosition])
                adapter.notifyItemChanged(vh.adapterPosition)
            }
        }).attachToRecyclerView(b.rvHealthHistory)

        b.btnLogHealth.setOnClickListener { showDialog(null) }

        val app = requireActivity().application as CareTrackerApp
        vm.loadForUser(app.currentUserId)

        viewLifecycleOwner.lifecycleScope.launch {
            vm.todayEntry.collect { updateCards(it) }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            vm.entries.collect {
                adapter.submitList(it.toList())
                b.layoutEmpty.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
                b.rvHealthHistory.visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
                updateTrendCards(it)
            }
        }
    }

    private fun updateCards(e: HealthEntryEntity?) {
        b.tvWeight.text = e?.weight?.let { "%.1f".format(it) } ?: "--"
        b.tvBloodPressure.text = if (e?.bloodPressureSystolic != null && e.bloodPressureDiastolic != null)
            "${e.bloodPressureSystolic}/${e.bloodPressureDiastolic}" else "--/--"
        b.tvHeartRate.text = e?.heartRate?.toString() ?: "--"
        b.tvBloodSugar.text = e?.bloodSugar?.let { "%.0f".format(it) } ?: "--"
        b.tvSleep.text = e?.sleepHours?.let { "%.1f".format(it) } ?: "--"
        b.tvSteps.text = e?.steps?.let { "%,d".format(it) } ?: "--"
        b.tvWater.text = e?.waterOz?.let { "%.1f".format(it) } ?: "0.0"
        b.tvCalories.text = e?.calories?.let { "%,d".format(it) } ?: "--"
    }

    private fun updateTrendCards(entries: List<HealthEntryEntity>) {
        val recent = entries.take(14)

        val bp = recent.filter { it.bloodPressureSystolic != null && it.bloodPressureDiastolic != null }
        if (bp.isEmpty()) {
            b.tvBpTrend.text = "Blood pressure: no recent data"
        } else {
            val latest = bp.first()
            val avgSys = bp.mapNotNull { it.bloodPressureSystolic }.average()
            val avgDia = bp.mapNotNull { it.bloodPressureDiastolic }.average()
            b.tvBpTrend.text =
                "Blood pressure: latest ${latest.bloodPressureSystolic}/${latest.bloodPressureDiastolic}, avg ${avgSys.toInt()}/${avgDia.toInt()} over ${bp.size} entries"
        }

        val sugar = recent.filter { it.bloodSugar != null }
        if (sugar.isEmpty()) {
            b.tvSugarTrend.text = "Blood sugar: no recent data"
        } else {
            val values = sugar.mapNotNull { it.bloodSugar }
            b.tvSugarTrend.text =
                "Blood sugar: latest ${values.first().toInt()}, high ${values.maxOrNull()?.toInt()}, low ${values.minOrNull()?.toInt()}, avg ${values.average().toInt()}"
        }

        val hr = recent.filter { it.heartRate != null }
        if (hr.isEmpty()) {
            b.tvHeartTrend.text = "Heart rate: no recent data"
        } else {
            val values = hr.mapNotNull { it.heartRate }
            b.tvHeartTrend.text =
                "Heart rate: latest ${values.first()} bpm, high ${values.maxOrNull()}, low ${values.minOrNull()}, avg ${values.average().toInt()} bpm"
        }

        val sleep = recent.filter { it.sleepHours != null }
        if (sleep.isEmpty()) {
            b.tvSleepTrend.text = "Sleep: no recent data"
        } else {
            val values = sleep.mapNotNull { it.sleepHours }
            b.tvSleepTrend.text =
                "Sleep: latest ${"%.1f".format(values.first())} hrs, high ${"%.1f".format(values.maxOrNull() ?: 0f)}, low ${"%.1f".format(values.minOrNull() ?: 0f)}, avg ${"%.1f".format(values.average())} hrs"
        }
    }

    private fun showDialog(existing: HealthEntryEntity?) {
        val d = DialogLogHealthBinding.inflate(layoutInflater)
        val app = requireActivity().application as CareTrackerApp

        var date = existing?.entryDate
            ?: LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)

        d.etDate.setText(fmt(date))
        d.etDate.setOnClickListener {
            val p = date.split("-")
            val cal = Calendar.getInstance()
            if (p.size == 3) cal.set(p[0].toInt(), p[1].toInt() - 1, p[2].toInt())
            DatePickerDialog(requireContext(), { _, y, m, day ->
                date = "%04d-%02d-%02d".format(y, m + 1, day)
                d.etDate.setText(fmt(date))
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        val units = listOf("lbs", "kg")
        d.spinnerWeightUnit.adapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_item, units
        ).also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
        d.spinnerWeightUnit.setSelection(if (existing?.weightUnit == "kg") 1 else 0)

        fun sl(fn: (Int) -> Unit) = object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar, p: Int, u: Boolean) = fn(p + 1)
            override fun onStartTrackingTouch(sb: SeekBar) {}
            override fun onStopTrackingTouch(sb: SeekBar) {}
        }
        d.seekSleepQuality.setOnSeekBarChangeListener(sl { d.tvSleepQuality.text = it.toString() })
        d.seekMood.setOnSeekBarChangeListener(sl { d.tvMood.text = it.toString() })
        d.seekEnergy.setOnSeekBarChangeListener(sl { d.tvEnergy.text = it.toString() })

        val todaySaved = vm.todayEntry.value
        val isToday = date == LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)

        if (existing != null) {
            existing.weight?.let { d.etWeight.setText("%.1f".format(it)) }
            existing.heartRate?.let { d.etHeartRate.setText(it.toString()) }
            existing.bloodPressureSystolic?.let { d.etSystolic.setText(it.toString()) }
            existing.bloodPressureDiastolic?.let { d.etDiastolic.setText(it.toString()) }
            existing.bloodSugar?.let { d.etBloodSugar.setText("%.0f".format(it)) }
            existing.sleepHours?.let { d.etSleep.setText("%.1f".format(it)) }
            existing.sleepQuality?.let {
                d.seekSleepQuality.progress = (it - 1).coerceIn(0, 9)
                d.tvSleepQuality.text = it.toString()
            }
            existing.mood?.let {
                d.seekMood.progress = (it - 1).coerceIn(0, 9)
                d.tvMood.text = it.toString()
            }
            existing.energy?.let {
                d.seekEnergy.progress = (it - 1).coerceIn(0, 9)
                d.tvEnergy.text = it.toString()
            }
            existing.steps?.let { d.etSteps.setText(it.toString()) }
            existing.exerciseMinutes?.let { d.etExercise.setText(it.toString()) }
            existing.waterOz?.let { d.etWater.setText("%.1f".format(it)) }
            existing.calories?.let { d.etCalories.setText(it.toString()) }
            existing.notes?.let { d.etNotes.setText(it) }
        } else if (isToday && todaySaved != null) {
            todaySaved.weight?.let { d.etWeight.setText("%.1f".format(it)) }
            todaySaved.heartRate?.let { d.etHeartRate.setText(it.toString()) }
            todaySaved.bloodPressureSystolic?.let { d.etSystolic.setText(it.toString()) }
            todaySaved.bloodPressureDiastolic?.let { d.etDiastolic.setText(it.toString()) }
            todaySaved.bloodSugar?.let { d.etBloodSugar.setText("%.0f".format(it)) }
            todaySaved.sleepHours?.let { d.etSleep.setText("%.1f".format(it)) }
            todaySaved.sleepQuality?.let {
                d.seekSleepQuality.progress = (it - 1).coerceIn(0, 9)
                d.tvSleepQuality.text = it.toString()
            }
            todaySaved.mood?.let {
                d.seekMood.progress = (it - 1).coerceIn(0, 9)
                d.tvMood.text = it.toString()
            }
            todaySaved.energy?.let {
                d.seekEnergy.progress = (it - 1).coerceIn(0, 9)
                d.tvEnergy.text = it.toString()
            }
            d.etSteps.hint = "Add steps (have ${"%,d".format(todaySaved.steps ?: 0)})"
            d.etExercise.hint = "Add mins (have ${todaySaved.exerciseMinutes ?: 0})"
            d.etWater.hint = "Add oz (have ${"%.1f".format(todaySaved.waterOz ?: 0f)})"
            d.etCalories.hint = "Add cals (have ${"%,d".format(todaySaved.calories ?: 0)})"
        }

        val dlg = AlertDialog.Builder(requireContext(), R.style.FullWidthDialog)
            .setView(d.root).create()
        dlg.window?.setBackgroundDrawableResource(android.R.color.transparent)

        d.btnCancel.setOnClickListener { dlg.dismiss() }
        d.btnSave.setOnClickListener {
            vm.saveEntry(
                HealthEntryEntity(
                    id = existing?.id ?: 0L,
                    userId = app.currentUserId,
                    entryDate = date,
                    weight = d.etWeight.text?.toString()?.toFloatOrNull(),
                    weightUnit = units[d.spinnerWeightUnit.selectedItemPosition],
                    heartRate = d.etHeartRate.text?.toString()?.toIntOrNull(),
                    bloodPressureSystolic = d.etSystolic.text?.toString()?.toIntOrNull(),
                    bloodPressureDiastolic = d.etDiastolic.text?.toString()?.toIntOrNull(),
                    bloodSugar = d.etBloodSugar.text?.toString()?.toFloatOrNull(),
                    sleepHours = d.etSleep.text?.toString()?.toFloatOrNull(),
                    sleepQuality = d.seekSleepQuality.progress + 1,
                    mood = d.seekMood.progress + 1,
                    energy = d.seekEnergy.progress + 1,
                    steps = d.etSteps.text?.toString()?.toIntOrNull(),
                    exerciseMinutes = d.etExercise.text?.toString()?.toIntOrNull(),
                    waterOz = d.etWater.text?.toString()?.toFloatOrNull(),
                    calories = d.etCalories.text?.toString()?.toIntOrNull(),
                    notes = d.etNotes.text?.toString()?.ifBlank { null }
                )
            )
            dlg.dismiss()
        }
        dlg.show()
    }

    private fun confirmDelete(e: HealthEntryEntity) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Entry")
            .setMessage("Delete entry for ${fmt(e.entryDate)}?")
            .setPositiveButton("Delete") { _, _ -> vm.deleteEntry(e) }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun fmt(iso: String) = runCatching {
        LocalDate.parse(iso).format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))
    }.getOrDefault(iso)

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }
}
