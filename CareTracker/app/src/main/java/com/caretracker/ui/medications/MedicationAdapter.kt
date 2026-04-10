package com.caretracker.ui.medications

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.caretracker.data.models.Medication
import com.caretracker.databinding.ItemMedicationBinding

class MedicationAdapter(private val onClick: (Medication) -> Unit) :
    ListAdapter<Medication, MedicationAdapter.MedicationViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicationViewHolder {
        return MedicationViewHolder(
            ItemMedicationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MedicationViewHolder, position: Int) {
        val med = getItem(position)
        holder.binding.tvMedName.text = med.name
        holder.binding.tvMedDosage.text = "${med.dosage} ${med.unit}"
        holder.binding.tvMedFrequency.text = med.frequency.replace("_", " ").replaceFirstChar { it.uppercase() }
        holder.binding.tvMedPills.text = "${med.pillsRemaining} pills remaining"
        holder.itemView.setOnClickListener { onClick(med) }
    }

    class MedicationViewHolder(val binding: ItemMedicationBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object DiffCallback : DiffUtil.ItemCallback<Medication>() {
        override fun areItemsTheSame(oldItem: Medication, newItem: Medication) =
            oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Medication, newItem: Medication) =
            oldItem == newItem
    }
}
