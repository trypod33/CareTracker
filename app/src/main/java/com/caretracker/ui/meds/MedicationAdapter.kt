package com.caretracker.ui.meds

import android.graphics.Color
import android.view.LayoutInflater
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import com.caretracker.databinding.ItemMedicationBinding
import java.util.Collections

class MedicationAdapter(
    private val onEdit: (MedWithStatus) -> Unit,
    private val onDelete: (MedWithStatus) -> Unit,
    private val onTake: (MedWithStatus) -> Unit,
    private val onRefill: (MedWithStatus) -> Unit,
    private val onStartDrag: (RecyclerView.ViewHolder) -> Unit
) : RecyclerView.Adapter<MedicationAdapter.VH>() {

    private val items = mutableListOf<MedWithStatus>()

    inner class VH(val b: ItemMedicationBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int) =
        VH(ItemMedicationBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        val med = item.med
        val b = holder.b

        try { b.viewMedColor.setBackgroundColor(Color.parseColor(med.color)) }
        catch (_: Exception) { b.viewMedColor.setBackgroundColor(Color.parseColor("#4f9cf9")) }

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

        b.tvDragHandleMed.setOnTouchListener { _, event ->
            if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                onStartDrag(holder)
                true
            } else {
                false
            }
        }
    }

    override fun getItemCount(): Int = items.size

    fun submitItems(newItems: List<MedWithStatus>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    fun moveItem(from: Int, to: Int) {
        if (from !in items.indices || to !in items.indices) return
        Collections.swap(items, from, to)
        notifyItemMoved(from, to)
    }

    fun currentItems(): List<MedWithStatus> = items.toList()
}
