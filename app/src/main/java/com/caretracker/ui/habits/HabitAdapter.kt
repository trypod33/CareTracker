package com.caretracker.ui.habits

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.caretracker.R
import com.caretracker.data.entities.HabitEntity

data class HabitWithStatus(
    val habit: HabitEntity,
    val isDoneToday: Boolean,
    val streak: Int
)

class HabitAdapter(
    private val onToggle: (HabitEntity) -> Unit,
    private val onLongPress: (HabitEntity) -> Unit
) : ListAdapter<HabitWithStatus, HabitAdapter.VH>(DIFF) {

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<HabitWithStatus>() {
            override fun areItemsTheSame(a: HabitWithStatus, b: HabitWithStatus) = a.habit.id == b.habit.id
            override fun areContentsTheSame(a: HabitWithStatus, b: HabitWithStatus) = a == b
        }
    }

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val tvIcon: TextView = view.findViewById(R.id.tvHabitIcon)
        val tvName: TextView = view.findViewById(R.id.tvHabitName)
        val tvStreak: TextView = view.findViewById(R.id.tvHabitStreak)
        val tvCategory: TextView = view.findViewById(R.id.tvHabitCategory)
        val tvToggle: TextView = view.findViewById(R.id.tvToggle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(LayoutInflater.from(parent.context).inflate(R.layout.item_habit, parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        holder.tvIcon.text = item.habit.icon
        holder.tvName.text = item.habit.name
        holder.tvStreak.text = if (item.streak > 0) "🔥${item.streak}d streak  " else "Start today!  "
        holder.tvCategory.text = item.habit.category.replaceFirstChar { it.uppercase() }

        if (item.isDoneToday) {
            holder.tvToggle.text = "✓"
            holder.tvToggle.setBackgroundResource(R.drawable.bg_toggle_checked)
        } else {
            holder.tvToggle.text = "+"
            holder.tvToggle.setBackgroundResource(R.drawable.bg_toggle_unchecked)
        }

        holder.itemView.setOnClickListener { onToggle(item.habit) }
        holder.itemView.setOnLongClickListener {
            onLongPress(item.habit)
            true
        }
    }
}
