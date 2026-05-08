package com.caretracker.ui.habits

import android.graphics.Typeface
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.caretracker.CareTrackerApp
import com.caretracker.R
import com.caretracker.data.entities.HabitEntity
import com.caretracker.data.entities.HabitLogEntity
import com.caretracker.data.entities.MoodJournalEntity
import com.caretracker.databinding.FragmentHabitsBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class HabitsFragment : Fragment() {
    private var _binding: FragmentHabitsBinding? = null
    private val binding get() = _binding!!

    private lateinit var habitAdapter: HabitAdapter
    private lateinit var journalAdapter: JournalAdapter
    private lateinit var habitTouchHelper: ItemTouchHelper
    private var habitsJob: Job? = null
    private var journalJob: Job? = null

    private var currentUserId: Long = 0L
    private var currentHabits: List<HabitEntity> = emptyList()
    private var selectedMoodScore: Int? = null
    private var orderedHabitIds: MutableList<Long> = mutableListOf()

    private val today: String
        get() = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHabitsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val app = requireActivity().application as CareTrackerApp

        setupHeader()
        setupAdapters()
        setupMoodPicker()
        setupJournalSave(app)

        binding.tvManage.setOnClickListener { showAddHabitDialog(currentUserId, app) }

        if (arguments?.getBoolean("openAddDialog", false) == true) {
            binding.root.post { showAddHabitDialog(currentUserId, app) }
        }

        if (arguments?.getBoolean("openAddDialog", false) == true) {
            binding.root.post { showAddHabitDialog(currentUserId, app) }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            app.currentUserIdFlow.collect { userId ->
                currentUserId = userId
                habitsJob?.cancel()
                journalJob?.cancel()
                habitsJob = launch { observeHabits(userId, app) }
                journalJob = launch { observeJournal(userId, app) }
            }
        }
    }

    private fun setupHeader() {
        val cal = Calendar.getInstance()
        val dayName = SimpleDateFormat("EEEE,", Locale.getDefault()).format(cal.time)
        val monthDay = SimpleDateFormat("MMM d", Locale.getDefault()).format(cal.time)
        binding.tvDayOfWeekDate.text = dayName + " " + monthDay
        val week = cal.get(Calendar.WEEK_OF_YEAR)
        val year = cal.get(Calendar.YEAR)
        binding.tvWeekYear.text = "Week $week · $year"
        buildWeekStrip(cal)
    }

    private fun buildWeekStrip(cal: Calendar) {
        val strip = binding.layoutWeekStrip
        strip.removeAllViews()
        val dayNames = listOf("MO", "TU", "WE", "TH", "FR", "SA", "SU")
        val todayDow = cal.get(Calendar.DAY_OF_WEEK)
        val todayIdx = if (todayDow == Calendar.SUNDAY) 6 else todayDow - 2
        val weekCal = cal.clone() as Calendar
        weekCal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)

        for (i in 0..6) {
            val dayNum = weekCal.get(Calendar.DAY_OF_MONTH)
            val isToday = i == todayIdx
            val cell = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER
                val lp = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                layoutParams = lp
                setPadding(0, 16, 0, 16)
                if (isToday) setBackgroundResource(R.drawable.bg_week_day_active)
            }
            val tvLabel = TextView(requireContext()).apply {
                text = dayNames[i]
                textSize = 10f
                gravity = Gravity.CENTER
                setTextColor(if (isToday) 0xFF8db87a.toInt() else 0xFF777777.toInt())
            }
            val tvNum = TextView(requireContext()).apply {
                text = "$dayNum"
                textSize = 15f
                gravity = Gravity.CENTER
                setTextColor(if (isToday) 0xFFffffff.toInt() else 0xFF999999.toInt())
                if (isToday) setTypeface(null, Typeface.BOLD)
            }
            cell.addView(tvLabel)
            cell.addView(tvNum)
            strip.addView(cell)
            weekCal.add(Calendar.DAY_OF_MONTH, 1)
        }
    }

    private fun setupAdapters() {
        habitAdapter = HabitAdapter(
            onPrimaryTap = { habit -> primaryTapHabit(habit) },
            onEditCount = { habit -> showEditCountDialog(habit) },
            onManageHabit = { habit -> showManageHabitDialog(habit) },
            onStartDrag = { holder -> habitTouchHelper.startDrag(holder) }
        )
        binding.rvHabits.layoutManager = LinearLayoutManager(requireContext())
        binding.rvHabits.adapter = habitAdapter

        habitTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            0
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                habitAdapter.moveItem(viewHolder.adapterPosition, target.adapterPosition)
                val moved = orderedHabitIds.removeAt(viewHolder.adapterPosition)
                orderedHabitIds.add(target.adapterPosition, moved)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) = Unit

            override fun isLongPressDragEnabled(): Boolean = false
        })
        habitTouchHelper.attachToRecyclerView(binding.rvHabits)

        journalAdapter = JournalAdapter()
        binding.rvJournalEntries.layoutManager = LinearLayoutManager(requireContext())
        binding.rvJournalEntries.adapter = journalAdapter
    }

    private fun setupMoodPicker() {
        val moods = listOf("😢" to 1, "😕" to 2, "😐" to 3, "🙂" to 4, "😄" to 5)
        moods.forEach { (emoji, score) ->
            val tv = TextView(requireContext()).apply {
                text = emoji
                textSize = 26f
                setPadding(10, 4, 10, 4)
                setOnClickListener {
                    selectedMoodScore = score
                    for (i in 0 until binding.layoutMoodPicker.childCount) {
                        binding.layoutMoodPicker.getChildAt(i).alpha = 0.3f
                    }
                    alpha = 1f
                }
            }
            binding.layoutMoodPicker.addView(tv)
        }
    }

    private fun setupJournalSave(app: CareTrackerApp) {
        binding.btnSaveJournal.setOnClickListener {
            val content = binding.etJournalContent.text.toString().trim()
            val tags = binding.etJournalTags.text.toString().trim()
            if (content.isEmpty()) {
                Toast.makeText(requireContext(), "Write something first!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            lifecycleScope.launch {
                val existing = app.repository.getMoodEntryForDate(currentUserId, today)
                if (existing != null) {
                    app.repository.updateMoodEntry(
                        existing.copy(
                            content = content,
                            moodScore = selectedMoodScore,
                            tags = tags.ifEmpty { null }
                        )
                    )
                } else {
                    app.repository.insertMoodEntry(
                        MoodJournalEntity(
                            userId = currentUserId,
                            entryDate = today,
                            content = content,
                            moodScore = selectedMoodScore,
                            tags = tags.ifEmpty { null }
                        )
                    )
                }
                binding.etJournalContent.text?.clear()
                binding.etJournalTags.text?.clear()
                selectedMoodScore = null
                for (i in 0 until binding.layoutMoodPicker.childCount) {
                    binding.layoutMoodPicker.getChildAt(i).alpha = 1f
                }
                Toast.makeText(requireContext(), "Journal saved ✓", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private suspend fun observeHabits(userId: Long, app: CareTrackerApp) {
        app.repository.getHabitsForUser(userId).collect { habits ->
            val base = habits.sortedBy { habit ->
                val idx = orderedHabitIds.indexOf(habit.id)
                if (idx >= 0) idx else Int.MAX_VALUE
            }
            currentHabits = if (orderedHabitIds.isEmpty()) habits else base
            if (orderedHabitIds.isEmpty()) {
                orderedHabitIds = currentHabits.map { it.id }.toMutableList()
            }
            refreshHabitList(currentHabits, app)
            updateStats(currentHabits, app)
        }
    }

    private suspend fun refreshHabitList(habits: List<HabitEntity>, app: CareTrackerApp) {
        val items = habits.map { habit ->
            val log = app.repository.getHabitLogForDate(habit.id, today)
            val todayCount = log?.count ?: 0
            val isDone = if (habit.targetCount > 1) todayCount >= habit.targetCount else log != null
            val streak = computeStreak(habit.id, app, habit.targetCount > 1)
            HabitWithStatus(habit, isDone, streak, todayCount)
        }
        habitAdapter.submitItems(items)

        val doneCount = items.count { it.isDoneToday }
        val total = items.size
        val pct = if (total > 0) (doneCount * 100) / total else 0
        binding.progressRing.progress = pct
        binding.tvProgressPct.text = "$pct%"

        val remaining = total - doneCount
        binding.tvMomentumTitle.text = when {
            total == 0 -> "Add your first habit!"
            doneCount == total -> "All done! Amazing! 🎉"
            pct >= 70 -> "Good momentum!"
            pct >= 30 -> "Keep going!"
            else -> "Let's get started!"
        }

        binding.tvMomentumSub.text = when {
            total == 0 -> "Tap Manage to add a habit."
            doneCount == total -> "All $total habits completed today."
            else -> "$doneCount of $total habits completed. $remaining remaining."
        }

        val dayStreak = computeDayStreak(habits, app)
        binding.tvStreakCount.text = "$dayStreak"
    }

    private suspend fun updateStats(habits: List<HabitEntity>, app: CareTrackerApp) {
        val total = habits.size
        val todayDone = habits.count { habit ->
            val log = app.repository.getHabitLogForDate(habit.id, today)
            if (habit.targetCount > 1) (log?.count ?: 0) >= habit.targetCount else log != null
        }
        binding.tvStatToday.text = "$todayDone/$total"

        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        var weekDone = 0
        var weekTotal = 0

        repeat(7) {
            val dateStr = sdf.format(cal.time)
            if (dateStr <= today) {
                weekTotal += total
                habits.forEach { habit ->
                    val log = app.repository.getHabitLogForDate(habit.id, dateStr)
                    val done = if (habit.targetCount > 1) (log?.count ?: 0) >= habit.targetCount else log != null
                    if (done) weekDone++
                }
            }
            cal.add(Calendar.DAY_OF_MONTH, 1)
        }

        binding.tvStatWeek.text = if (weekTotal > 0) "${(weekDone * 100) / weekTotal}%" else "0%"
        binding.tvStatBestStreak.text = habits.maxOfOrNull { computeStreak(it.id, app, it.targetCount > 1) }?.toString() ?: "0"
    }

    private suspend fun observeJournal(userId: Long, app: CareTrackerApp) {
        app.repository.getMoodEntries(userId).collect { entries ->
            binding.tvPastEntriesHeader.isVisible = entries.isNotEmpty()
            binding.rvJournalEntries.isVisible = entries.isNotEmpty()
            journalAdapter.submitList(entries)
        }
    }

    private suspend fun computeStreak(habitId: Long, app: CareTrackerApp, isCounterHabit: Boolean): Int {
        val logs = app.repository.getAllLogsForHabit(habitId)
        if (logs.isEmpty()) return 0

        val qualifyingDates = if (isCounterHabit) logs.filter { it.count > 0 }.map { it.loggedDate }.toSet()
        else logs.map { it.loggedDate }.toSet()

        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val cal = Calendar.getInstance()
        var streak = 0
        var checkDate = sdf.format(cal.time)

        while (qualifyingDates.contains(checkDate)) {
            streak++
            cal.add(Calendar.DAY_OF_MONTH, -1)
            checkDate = sdf.format(cal.time)
        }
        return streak
    }

    private suspend fun computeDayStreak(habits: List<HabitEntity>, app: CareTrackerApp): Int {
        if (habits.isEmpty()) return 0

        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val cal = Calendar.getInstance()
        var streak = 0

        repeat(365) {
            val dateStr = sdf.format(cal.time)
            val allDone = habits.all { habit ->
                val log = app.repository.getHabitLogForDate(habit.id, dateStr)
                if (habit.targetCount > 1) (log?.count ?: 0) >= habit.targetCount else log != null
            }
            if (allDone) {
                streak++
                cal.add(Calendar.DAY_OF_MONTH, -1)
            } else if (streak == 0 && dateStr == today) {
                cal.add(Calendar.DAY_OF_MONTH, -1)
            } else {
                return streak
            }
        }
        return streak
    }

    private fun primaryTapHabit(habit: HabitEntity) {
        if (habit.targetCount > 1) incrementHabit(habit) else toggleHabit(habit)
    }

    private fun toggleHabit(habit: HabitEntity) {
        val app = requireActivity().application as CareTrackerApp
        lifecycleScope.launch {
            val existing = app.repository.getHabitLogForDate(habit.id, today)
            if (existing == null) {
                app.repository.insertHabitLog(HabitLogEntity(habitId = habit.id, loggedDate = today))
            } else {
                app.repository.deleteHabitLog(existing)
            }
            refreshHabitList(currentHabits, app)
            updateStats(currentHabits, app)
        }
    }

    private fun incrementHabit(habit: HabitEntity) {
        val app = requireActivity().application as CareTrackerApp
        lifecycleScope.launch {
            val existing = app.repository.getHabitLogForDate(habit.id, today)
            if (existing == null) {
                app.repository.insertHabitLog(HabitLogEntity(habitId = habit.id, loggedDate = today, count = 1))
            } else {
                app.repository.insertHabitLog(existing.copy(count = existing.count + 1))
            }
            refreshHabitList(currentHabits, app)
            updateStats(currentHabits, app)
        }
    }

    private fun showEditCountDialog(habit: HabitEntity) {
        val app = requireActivity().application as CareTrackerApp
        val input = EditText(requireContext()).apply {
            inputType = InputType.TYPE_CLASS_NUMBER
            hint = "Today's count"
        }

        lifecycleScope.launch {
            val existing = app.repository.getHabitLogForDate(habit.id, today)
            input.setText((existing?.count ?: 0).toString())

            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Edit count for " + habit.name)
                .setView(input)
                .setPositiveButton("Save") { _, _ ->
                    val newCount = input.text?.toString()?.trim()?.toIntOrNull()?.coerceAtLeast(0) ?: 0
                    lifecycleScope.launch {
                        val current = app.repository.getHabitLogForDate(habit.id, today)
                        if (newCount <= 0) {
                            if (current != null) app.repository.deleteHabitLog(current)
                        } else {
                            val newLog = if (current == null) {
                                HabitLogEntity(habitId = habit.id, loggedDate = today, count = newCount)
                            } else {
                                current.copy(count = newCount)
                            }
                            app.repository.insertHabitLog(newLog)
                        }
                        refreshHabitList(currentHabits, app)
                        updateStats(currentHabits, app)
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    private fun showManageHabitDialog(habit: HabitEntity) {
        val options = arrayOf("Edit habit", "Delete habit")

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(habit.name)
            .setItems(options) { _, which ->
                when (which) {
                    0 -> showEditHabitDialog(habit)
                    1 -> confirmDeleteHabit(habit)
                }
            }
            .show()
    }

    private fun showEditHabitDialog(habit: HabitEntity) {
        val app = requireActivity().application as CareTrackerApp

        val layout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(40, 24, 40, 8)
        }

        val nameInput = TextInputEditText(requireContext()).apply {
            hint = "Habit name"
            setText(habit.name)
        }

        val categoryInput = TextInputEditText(requireContext()).apply {
            hint = "Category"
            setText(habit.category)
        }

        val iconInput = TextInputEditText(requireContext()).apply {
            hint = "Icon emoji"
            setText(habit.icon)
        }

        val targetInput = EditText(requireContext()).apply {
            hint = "Target count"
            inputType = InputType.TYPE_CLASS_NUMBER
            setText(habit.targetCount.toString())
        }

        layout.addView(nameInput)
        layout.addView(categoryInput)
        layout.addView(iconInput)
        layout.addView(targetInput)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Edit Habit")
            .setView(layout)
            .setPositiveButton("Save") { _, _ ->
                val updated = habit.copy(
                    name = nameInput.text?.toString()?.trim().orEmpty().ifEmpty { habit.name },
                    category = categoryInput.text?.toString()?.trim().orEmpty().ifEmpty { "health" },
                    icon = iconInput.text?.toString()?.trim().orEmpty().ifEmpty { "✨" },
                    targetCount = targetInput.text?.toString()?.trim()?.toIntOrNull()?.coerceAtLeast(1) ?: 1
                )
                lifecycleScope.launch {
                    app.repository.updateHabit(updated)
                    currentHabits = currentHabits.map { if (it.id == habit.id) updated else it }
                    refreshHabitList(currentHabits, app)
                    updateStats(currentHabits, app)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun confirmDeleteHabit(habit: HabitEntity) {
        val app = requireActivity().application as CareTrackerApp
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Delete habit?")
            .setMessage("This removes " + habit.name + ".")
            .setPositiveButton("Delete") { _, _ ->
                lifecycleScope.launch {
                    app.repository.deleteHabit(habit)
                    orderedHabitIds.remove(habit.id)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showAddHabitDialog(userId: Long, app: CareTrackerApp) {
        val layout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(40, 24, 40, 8)
        }

        val nameInput = TextInputEditText(requireContext()).apply {
            hint = "Habit name"
        }

        val categoryInput = TextInputEditText(requireContext()).apply {
            hint = "Category (health, mind, fitness...)"
            setText("health")
        }

        val iconInput = TextInputEditText(requireContext()).apply {
            hint = "Icon emoji"
            setText("✨")
        }

        val targetInput = EditText(requireContext()).apply {
            hint = "Target count (1 = simple check habit)"
            inputType = InputType.TYPE_CLASS_NUMBER
            setText("1")
        }

        layout.addView(nameInput)
        layout.addView(categoryInput)
        layout.addView(iconInput)
        layout.addView(targetInput)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Add Habit")
            .setView(layout)
            .setPositiveButton("Save") { _, _ ->
                val title = nameInput.text?.toString()?.trim().orEmpty()
                val category = categoryInput.text?.toString()?.trim().orEmpty().ifEmpty { "health" }
                val icon = iconInput.text?.toString()?.trim().orEmpty().ifEmpty { "✨" }
                val target = targetInput.text?.toString()?.trim()?.toIntOrNull()?.coerceAtLeast(1) ?: 1

                if (title.isNotEmpty()) {
                    lifecycleScope.launch {
                        app.repository.insertHabit(
                            HabitEntity(
                                userId = userId,
                                name = title,
                                category = category,
                                icon = icon,
                                targetCount = target
                            )
                        )
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
