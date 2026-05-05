package com.caretracker.ui.habits

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.caretracker.R
import com.caretracker.data.entities.HabitEntity

data class HabitWithStatus(
    val habit: HabitEntity,
    val isDoneToday: Boolean,
    val streak: Int,
    val todayCount: Int = 0
)

class HabitAdapter(
    private val onPrimaryTap: (HabitEntity) -> Unit,
    private val onEditCount: (HabitEntity) -> Unit,
    private val onManageHabit: (HabitEntity) -> Unit,
    private val onStartDrag: (VH) -> Unit
) : RecyclerView.Adapter<HabitAdapter.VH>() {

    private val items = mutableListOf<HabitWithStatus>()

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
        val btnHabitEdit: TextView = view.findViewById(R.id.btnHabitEdit)
        val btnDrag: TextView = view.findViewById(R.id.btnDrag)
    }

    fun submitItems(newItems: List<HabitWithStatus>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    fun currentItems(): List<HabitWithStatus> = items.toList()

    fun moveItem(from: Int, to: Int) {
        if (from !in items.indices || to !in items.indices) return
        val moved = items.removeAt(from)
        items.add(to, moved)
        notifyItemMoved(from, to)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(LayoutInflater.from(parent.context).inflate(R.layout.item_habit, parent, false))

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        val habit = item.habit
        val isCounterHabit = habit.targetCount > 1

        holder.tvIcon.text = habit.icon
        holder.tvName.text = habit.name

        holder.tvCategory.text = if (isCounterHabit) {
            habit.category.replaceFirstChar { it.uppercase() } + " • " +
                item.todayCount + "/" + habit.targetCount
        } else {
            habit.category.replaceFirstChar { it.uppercase() }
        }

        holder.tvStreak.text = when {
            isCounterHabit && item.todayCount > 0 ->
                "Today: " + item.todayCount + "/" + habit.targetCount
            item.streak > 0 ->
                "🔥${item.streak}d streak"
            isCounterHabit ->
                "Tap to add • Hold to edit count"
            else ->
                "Tap to mark done"
        }

        if (isCounterHabit) {
            val done = item.todayCount >= habit.targetCount
            holder.tvToggle.text = if (done) item.todayCount.toString() else "+" + item.todayCount
            holder.tvToggle.setBackgroundResource(
                if (done) R.drawable.bg_toggle_checked else R.drawable.bg_toggle_unchecked
            )
            holder.tvToggle.setTextColor(if (done) 0xFF111811.toInt() else 0xFF8db87a.toInt())
        } else {
            holder.tvToggle.text = if (item.isDoneToday) "✓" else "+"
            holder.tvToggle.setBackgroundResource(
                if (item.isDoneToday) R.drawable.bg_toggle_checked else R.drawable.bg_toggle_unchecked
            )
            holder.tvToggle.setTextColor(if (item.isDoneToday) 0xFF111811.toInt() else 0xFF8db87a.toInt())
        }

        holder.tvToggle.setOnClickListener { onPrimaryTap(habit) }
        holder.itemView.setOnClickListener { onPrimaryTap(habit) }

        holder.tvToggle.setOnLongClickListener {
            if (isCounterHabit) {
                onEditCount(habit)
                true
            } else {
                false
            }
        }
        holder.itemView.setOnLongClickListener {
            if (isCounterHabit) {
                onEditCount(habit)
                true
            } else {
                false
            }
        }

        holder.btnHabitEdit.setOnClickListener { onManageHabit(habit) }

        holder.btnDrag.setOnTouchListener { _, event ->
            if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                onStartDrag(holder)
            }
            false
        }
    }
}
