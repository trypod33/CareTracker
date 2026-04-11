package com.caretracker.ui.health

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.caretracker.data.models.DailyHealthEntry
import com.caretracker.databinding.ItemHealthEntryBinding
import java.text.SimpleDateFormat
import java.util.*

class HealthEntryAdapter(
    private val onEdit:   (DailyHealthEntry) -> Unit,
    private val onDelete: (DailyHealthEntry) -> Unit
) : ListAdapter<DailyHealthEntry, HealthEntryAdapter.VH>(DIFF) {

    inner class VH(val b: ItemHealthEntryBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemHealthEntryBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) {
        val entry = getItem(position)
        val b = holder.b

        // Format date display
        val fmt = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        val keyFmt = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        b.tvEntryDate.text = runCatching {
            fmt.format(keyFmt.parse(entry.date)!!)
        }.getOrDefault(entry.date)

        // Weight
        b.tvWeight.text = entry.weightValue?.let { "$it ${entry.weightUnit}" } ?: "—"
        // BP
        b.tvBp.text = if (entry.bpSystolic != null && entry.bpDiastolic != null)
            "${entry.bpSystolic}/${entry.bpDiastolic}" else "—"
        // HR
        b.tvHr.text  = entry.heartRate?.toString() ?: "—"
        // Sleep
        b.tvSleep.text = entry.sleepHours?.let { "${it}h" } ?: "—"
        // Mood
        b.tvMood.text  = entry.mood?.let {
            when { it >= 8 -> "${it}/10 😊"; it >= 5 -> "${it}/10 😐"; else -> "${it}/10 😟" }
        } ?: "—"
        // Energy
        b.tvEnergy.text = entry.energyLevel?.let { "${it}/10" } ?: "—"
        // Steps
        b.tvSteps.text = entry.steps?.let {
            if (it >= 1000) "${it / 1000}k" else it.toString()
        } ?: "—"

        b.btnEditEntry.setOnClickListener   { onEdit(entry) }
        b.btnDeleteEntry.setOnClickListener { onDelete(entry) }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<DailyHealthEntry>() {
            override fun areItemsTheSame(a: DailyHealthEntry, b: DailyHealthEntry) = a.id == b.id
            override fun areContentsTheSame(a: DailyHealthEntry, b: DailyHealthEntry) = a == b
        }
    }
}
