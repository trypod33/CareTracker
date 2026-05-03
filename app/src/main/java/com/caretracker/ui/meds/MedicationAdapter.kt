package com.caretracker.ui.meds

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.caretracker.databinding.ItemMedicationBinding

class MedicationAdapter(
    private val onEdit: (MedWithStatus) -> Unit,
    private val onDelete: (MedWithStatus) -> Unit,
    private val onTake: (MedWithStatus) -> Unit,
    private val onRefill: (MedWithStatus) -> Unit
) : ListAdapter<MedWithStatus, MedicationAdapter.VH>(DIFF) {

    inner class VH(val b: ItemMedicationBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemMedicationBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        val med = item.med
        val b = holder.b

        try { b.viewMedColor.setBackgroundColor(Color.parseColor(med.color)) }
        catch (e: Exception) { b.viewMedColor.setBackgroundColor(Color.parseColor("#4f9cf9")) }

        b.tvMedName.text = med.name
        b.tvDosage.text = "${med.dosage ?: ""} ${med.dosageUnit}".trim()
        b.tvForm.text = med.form.replaceFirstChar { it.uppercase() }
        b.tvFrequency.text = med.frequency ?: ""
        b.tvPrescriber.text = med.prescriber ?: ""

        val maxPills = (med.pillsPerRefill ?: 30).coerceAtLeast(1)
        b.progressPills.progress = ((med.currentCount.toFloat() / maxPills).coerceIn(0f, 1f) * 100).toInt()
        b.tvPillCount.text = "${med.currentCount} pills"

        if (item.takenToday) {
            b.tvTakenStatus.text = "Taken today ✓"
            b.tvTakenStatus.setTextColor(Color.parseColor("#4caf50"))
            b.btnTake.isEnabled = false
            b.btnTake.alpha = 0.5f
        } else {
            b.tvTakenStatus.text = "Not taken today"
            b.tvTakenStatus.setTextColor(Color.parseColor("#888888"))
            b.btnTake.isEnabled = true
            b.btnTake.alpha = 1f
        }

        b.btnTake.setOnClickListener { onTake(item) }
        b.btnRefill.setOnClickListener { onRefill(item) }
        b.btnEdit.setOnClickListener { onEdit(item) }
        b.btnDelete.setOnClickListener { onDelete(item) }
    }

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<MedWithStatus>() {
            override fun areItemsTheSame(a: MedWithStatus, b: MedWithStatus) = a.med.id == b.med.id
            override fun areContentsTheSame(a: MedWithStatus, b: MedWithStatus) = a == b
        }
    }
}
