package com.caretracker.ui.health
import android.view.*
import androidx.recyclerview.widget.*
import com.caretracker.data.entities.HealthEntryEntity
import com.caretracker.databinding.ItemHealthEntryBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class HealthHistoryAdapter(
    private val onItemClick: (HealthEntryEntity) -> Unit,
    private val onDeleteClick: (HealthEntryEntity) -> Unit
) : ListAdapter<HealthEntryEntity, HealthHistoryAdapter.VH>(DIFF) {
    inner class VH(private val b: ItemHealthEntryBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(e: HealthEntryEntity) {
            b.tvEntryDate.text = runCatching {
                LocalDate.parse(e.entryDate).format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
            }.getOrDefault(e.entryDate)
            val p = mutableListOf<String>()
            if (e.bloodPressureSystolic != null && e.bloodPressureDiastolic != null) p.add("BP ${e.bloodPressureSystolic}/${e.bloodPressureDiastolic}")
            e.heartRate?.let { p.add("HR $it") }
            e.bloodSugar?.let { p.add("BG ${"%.0f".format(it)}") }
            e.sleepHours?.let { p.add("Sleep ${"%.1f".format(it)}h") }
            e.steps?.let { p.add("%,d steps".format(it)) }
            e.mood?.let { p.add("Mood $it/10") }
            b.tvEntrySummary.text = if (p.isEmpty()) "No vitals" else p.joinToString(" · ")
            b.tvEntryWeight.text = e.weight?.let { "${"%.1f".format(it)} ${e.weightUnit}" } ?: ""
            b.root.setOnClickListener { onItemClick(e) }
            b.btnDeleteEntry.setOnClickListener { onDeleteClick(e) }
        }
    }
    override fun onCreateViewHolder(p: ViewGroup, t: Int) = VH(ItemHealthEntryBinding.inflate(LayoutInflater.from(p.context), p, false))
    override fun onBindViewHolder(h: VH, pos: Int) = h.bind(getItem(pos))
    companion object {
        val DIFF = object : DiffUtil.ItemCallback<HealthEntryEntity>() {
            override fun areItemsTheSame(a: HealthEntryEntity, b: HealthEntryEntity) = a.id == b.id
            override fun areContentsTheSame(a: HealthEntryEntity, b: HealthEntryEntity) = a == b
        }
    }
}
