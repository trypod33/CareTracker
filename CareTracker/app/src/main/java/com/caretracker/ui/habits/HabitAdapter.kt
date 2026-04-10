package com.caretracker.ui.habits

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.caretracker.data.models.Habit
import com.caretracker.databinding.ItemHabitBinding
import kotlinx.coroutines.delay
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

        fun fmtAmt(v: Double) =
            if (v == kotlin.math.floor(v)) v.toInt().toString() else "%.1f".format(v)

        fun defaultLabel() = "+ ${fmtAmt(defaultAmt)} ${habit.unit}"

        // Basic fields
        b.tvHabitName.text = habit.name
        b.tvHabitFrequency.text = habit.frequency.replaceFirstChar { it.uppercase() }
        b.tvHabitTarget.text = "Goal: ${habit.targetValue} ${habit.unit}"
        b.tvHabitDescription.text = habit.description
        b.root.setOnClickListener { onClick(habit) }

        if (isMeasurable) {
            b.layoutQuickLog.visibility = View.VISIBLE
            b.tvTodayTotal.visibility = View.VISIBLE

            // Always start with the default label
            b.btnQuickLog.text = defaultLabel()

            // Load today's running total
            scope.launch {
                val total = viewModel.getTodayTotal(habit.id)
                b.tvTodayTotal.text = "Today: ${fmtAmt(total)} ${habit.unit}"
            }

            // Single tap: log default amount
            b.btnQuickLog.setOnClickListener {
                scope.launch {
                    val newTotal = viewModel.quickLog(habit, defaultAmt)
                    b.tvTodayTotal.text = "Today: ${fmtAmt(newTotal)} ${habit.unit}"
                }
            }

            // Long press: custom amount dialog
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
                                // Briefly show what was just logged as confirmation
                                b.tvTodayTotal.text = "Today: ${fmtAmt(newTotal)} ${habit.unit}"
                                b.btnQuickLog.text = "\u2713 ${fmtAmt(entered)} ${habit.unit} logged"
                                // After 2 seconds, reset button back to default label
                                delay(2000)
                                b.btnQuickLog.text = defaultLabel()
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
