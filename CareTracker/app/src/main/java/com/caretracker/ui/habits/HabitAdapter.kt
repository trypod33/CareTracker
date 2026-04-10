package com.caretracker.ui.habits

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.caretracker.data.models.Habit
import com.caretracker.databinding.ItemHabitBinding

class HabitAdapter(private val onClick: (Habit) -> Unit) :
    ListAdapter<Habit, HabitAdapter.HabitViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        return HabitViewHolder(
            ItemHabitBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        val habit = getItem(position)
        holder.binding.tvHabitName.text = habit.name
        holder.binding.tvHabitFrequency.text = habit.frequency.replaceFirstChar { it.uppercase() }
        holder.binding.tvHabitTarget.text = "Goal: ${habit.targetValue} ${habit.unit}"
        holder.binding.tvHabitDescription.text = habit.description
        holder.itemView.setOnClickListener { onClick(habit) }
    }

    class HabitViewHolder(val binding: ItemHabitBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object DiffCallback : DiffUtil.ItemCallback<Habit>() {
        override fun areItemsTheSame(oldItem: Habit, newItem: Habit) =
            oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Habit, newItem: Habit) =
            oldItem == newItem
    }
}
