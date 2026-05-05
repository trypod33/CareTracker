package com.caretracker.ui.tasks

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.caretracker.R
import com.caretracker.data.entities.TaskEntity
import java.util.Collections

class TaskAdapter(
    private val onToggle: (TaskEntity) -> Unit,
    private val onDelete: (TaskEntity) -> Unit,
    private val onStartDrag: (RecyclerView.ViewHolder) -> Unit
) : RecyclerView.Adapter<TaskAdapter.VH>() {

    private val items = mutableListOf<TaskEntity>()

    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        val tvTitle: TextView = v.findViewById(R.id.tvTaskTitle)
        val tvDue: TextView = v.findViewById(R.id.tvTaskDue)
        val tvPriority: TextView = v.findViewById(R.id.tvTaskPriority)
        val btnDelete: ImageButton = v.findViewById(R.id.btnDeleteTask)
        val tvDragHandle: TextView = v.findViewById(R.id.tvDragHandleTask)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) {
        val task = items[position]

        holder.tvTitle.text = task.title
        holder.tvDue.text = task.dueDate?.let { "Due: $it" } ?: ""
        holder.tvDue.visibility = if (task.dueDate != null) View.VISIBLE else View.GONE

        holder.tvPriority.text = task.priority.uppercase()
        holder.tvPriority.setTextColor(
            when (task.priority.lowercase()) {
                "high" -> 0xFFE74C3C.toInt()
                "medium" -> 0xFFE67E22.toInt()
                else -> 0xFF2ECC71.toInt()
            }
        )

        val isDone = task.status == "done"
        if (isDone) {
            holder.tvTitle.paintFlags = holder.tvTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.tvTitle.alpha = 0.5f
        } else {
            holder.tvTitle.paintFlags = holder.tvTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            holder.tvTitle.alpha = 1.0f
        }

        holder.itemView.setOnClickListener { onToggle(task) }
        holder.btnDelete.setOnClickListener { onDelete(task) }

        holder.tvDragHandle.setOnTouchListener { _, event ->
            if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                onStartDrag(holder)
                true
            } else {
                false
            }
        }
    }

    override fun getItemCount(): Int = items.size

    fun submitItems(newItems: List<TaskEntity>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    fun moveItem(from: Int, to: Int) {
        if (from !in items.indices || to !in items.indices) return
        Collections.swap(items, from, to)
        notifyItemMoved(from, to)
    }

    fun currentItems(): List<TaskEntity> = items.toList()
}
