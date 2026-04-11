package com.caretracker.ui.medications

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.caretracker.data.models.Medication
import com.caretracker.databinding.ItemMedicationBinding

class MedicationAdapter(
    private val onClick:  (Medication) -> Unit,
    private val onDelete: (Medication) -> Unit
) : ListAdapter<Medication, MedicationAdapter.MedicationViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicationViewHolder {
        return MedicationViewHolder(
            ItemMedicationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MedicationViewHolder, position: Int) {
        val med = getItem(position)
        val b   = holder.binding

        // Header
        b.tvMedName.text     = med.name
        b.tvMedDosage.text   = "${med.dosage} ${med.unit}"
        b.tvMedFrequency.text = med.frequency.replace("_", " ").replaceFirstChar { it.uppercase() }
        b.tvPrescriber.text  = ""  // extend model with prescriber name field if needed

        // Inventory section
        b.tvPillCount.text   = "${med.pillsRemaining} pills"
        val refillTotal = if (med.pillsPerDose > 0) med.pillsPerDose else 30
        b.progressPills.max      = refillTotal
        b.progressPills.progress = med.pillsRemaining.coerceAtMost(refillTotal)

        // Taken status
        b.tvTakenStatus.text = "Not taken today"   // TODO: wire to dose log

        // Card background tint
        try {
            val tint = android.graphics.Color.parseColor(med.color)
            // Apply a subtle tint to the icon background only
            (b.ivMedIcon.background as? android.graphics.drawable.GradientDrawable)
                ?.setColor(tint)
        } catch (_: Exception) { /* ignore invalid color strings */ }

        // Short tap — edit
        holder.itemView.setOnClickListener { onClick(med) }

        // Action buttons on the card
        b.btnEditMed.setOnClickListener   { onClick(med) }
        b.btnDeleteMed.setOnClickListener {
            AlertDialog.Builder(it.context)
                .setTitle("Delete \"${med.name}\"?")
                .setMessage("This will permanently delete this medication.")
                .setPositiveButton("Delete") { _, _ -> onDelete(med) }
                .setNegativeButton("Cancel", null)
                .show()
        }
        b.btnTakeMed.setOnClickListener   { /* TODO: log dose */ }
        b.btnRefillMed.setOnClickListener { /* TODO: refill dialog */ }
    }

    class MedicationViewHolder(val binding: ItemMedicationBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object DiffCallback : DiffUtil.ItemCallback<Medication>() {
        override fun areItemsTheSame(a: Medication, b: Medication) = a.id == b.id
        override fun areContentsTheSame(a: Medication, b: Medication) = a == b
    }
}
