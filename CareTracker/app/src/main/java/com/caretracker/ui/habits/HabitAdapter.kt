package com.caretracker.ui.habits

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.caretracker.data.models.Habit
import com.caretracker.databinding.ItemHabitBinding
import kotlinx.coroutines.launch

/** Units that get the quick-log button and running total display. */
private val MEASURABLE_UNITS = setOf("oz", "ml", "cups", "glasses", "steps", "minutes", "mins", "hours", "calories", "cal")

/** Default amounts per unit — shown on the button label. */
private val DEFAULT_AMOUNTS = mapOf(
    "oz"       to 16.9,
    "ml"       to 500.0,
    "cups"     to 1.0,
    "glasses"  to 1.0,
    "steps"    to 1000.0,
    "minutes"  to 30.0,
    "mins"     to 30.0,
    "hours"    to 1.0,
    "calories" to 100.0,
    "cal"      to 100.0
)

class HabitAdapter(
    private val scope: LifecycleCoroutineScope,
    private val viewModel: HabitViewModel,
    private val onClick: (Habit) -> Unit
) : ListAdapter<Habit, HabitAdapter.HabitViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        return HabitViewHolder(
            ItemHabitBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        val habit = getItem(position)
        val b = holder.binding
        val unit = habit.unit.lowercase().trim()
        val isMeasurable = unit in MEASURABLE_UNITS
        val defaultAmt = DEFAULT_AMOUNTS[unit] ?: 1.0

        // Basic fields
        b.tvHabitName.text = habit.name
        b.tvHabitFrequency.text = habit.frequency.replaceFirstChar { it.uppercase() }
        b.tvHabitTarget.text = "Goal: ${habit.targetValue} ${habit.unit}"
        b.tvHabitDescription.text = habit.description
        b.itemView.setOnClickListener { onClick(habit) }

        if (isMeasurable) {
            // Show quick-log section
            b.layoutQuickLog.visibility = View.VISIBLE
            b.tvTodayTotal.visibility = View.VISIBLE

            // Format button label nicely: 16.9 → "+ 16.9 oz", 1.0 → "+ 1 oz"
            fun fmtAmt(v: Double) = if (v == kotlin.math.floor(v)) v.toInt().toString() else "%.1f".format(v)
            b.btnQuickLog.text = "+ ${fmtAmt(defaultAmt)} ${habit.unit}"

            // Load today's total
            scope.launch {
                val total = viewModel.getTodayTotal(habit.id)
                b.tvTodayTotal.text = "Today: ${fmtAmt(total)} ${habit.unit}"
            }

            // Single tap: log the default amount
            b.btnQuickLog.setOnClickListener {
                scope.launch {
                    val newTotal = viewModel.quickLog(habit, defaultAmt)
                    fun fmtA(v: Double) = if (v == kotlin.math.floor(v)) v.toInt().toString() else "%.1f".format(v)
                    b.tvTodayTotal.text = "Today: ${fmtA(newTotal)} ${habit.unit}"
                }
            }

            // Long press: ask for a custom amount
            b.btnQuickLog.setOnLongClickListener {
                val ctx = it.context
                val input = EditText(ctx).apply {
                    hint = "Amount in ${habit.unit}"
                    setText("%.1f".format(defaultAmt))
                    inputType = android.text.InputType.TYPE_CLASS_NUMBER or
                            android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
                    selectAll()
                }
                val container = LinearLayout(ctx).apply {
                    orientation = LinearLayout.VERTICAL
                    setPadding(48, 16, 48, 0)
                    addView(input)
                }
                AlertDialog.Builder(ctx)
                    .setTitle("Log ${habit.name}")
                    .setMessage("Enter the amount in ${habit.unit}:")
                    .setView(container)
                    .setPositiveButton("Log") { _, _ ->
                        val entered = input.text.toString().toDoubleOrNull()
                        if (entered != null && entered > 0) {
                            scope.launch {
                                val newTotal = viewModel.quickLog(habit, entered)
                                fun fmtA(v: Double) = if (v == kotlin.math.floor(v)) v.toInt().toString() else "%.1f".format(v)
                                b.tvTodayTotal.text = "Today: ${fmtA(newTotal)} ${habit.unit}"
                                b.btnQuickLog.text = "+ ${fmtA(entered)} ${habit.unit}"
                            }
                        }
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
                true
            }
        } else {
            b.layoutQuickLog.visibility = View.GONE
            b.tvTodayTotal.visibility = View.GONE
        }
    }

    class HabitViewHolder(val binding: ItemHabitBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object DiffCallback : DiffUtil.ItemCallback<Habit>() {
        override fun areItemsTheSame(oldItem: Habit, newItem: Habit) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Habit, newItem: Habit) = oldItem == newItem
    }
}
