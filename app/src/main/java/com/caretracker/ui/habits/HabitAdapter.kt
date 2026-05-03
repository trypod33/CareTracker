package com.caretracker.ui.habits

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.caretracker.R
import com.caretracker.data.entities.HabitEntity

class HabitAdapter(
    private val habits: List<HabitEntity>,
    private val onToggle: (HabitEntity) -> Unit
) : RecyclerView.Adapter<HabitAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvHabitName)
        val tvCategory: TextView = view.findViewById(R.id.tvHabitCategory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_habit, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val habit = habits[position]
        holder.tvName.text = "${habit.icon} ${habit.name}"
        holder.tvCategory.text = habit.category
        holder.itemView.setOnClickListener { onToggle(habit) }
    }

    override fun getItemCount() = habits.size
}
