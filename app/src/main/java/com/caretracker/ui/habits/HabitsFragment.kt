package com.caretracker.ui.habits

import android.graphics.Typeface
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
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
    private var habitsJob: Job? = null
    private var journalJob: Job? = null

    private var currentUserId: Long = 0L
    private var currentHabits: List<HabitEntity> = emptyList()
    private var selectedMoodScore: Int? = null

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
        binding.tvDayOfWeekDate.text = dayName + " / " + monthDay
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
            val isToday = (i == todayIdx)
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
            onTap = { habit -> handleHabitTap(habit) },
            onLongPress = { habit -> handleHabitLongPress(habit) }
        )
        binding.rvHabits.layoutManager = LinearLayoutManager(requireContext())
        binding.rvHabits.adapter = habitAdapter

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
            currentHabits = habits
            refreshHabitList(habits, app)
            updateStats(habits, app)
        }
    }

    private suspend fun refreshHabitList(habits: List<HabitEntity>, app: CareTrackerApp) {
        val items = habits.map { habit ->
            val existing = app.repository.getHabitLogForDate(habit.id, today)
            val currentCount = existing?.count ?: 0
            val isDone = currentCount >= habit.targetCount && habit.targetCount > 0
            val streak = computeStreak(habit.id, app)
            HabitWithStatus(habit, isDone, streak, currentCount)
        }
        habitAdapter.submitList(items)

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
            total == 0 -> "Tap '+ Add' above to begin."
            doneCount == total -> "All $total habits completed today."
            else -> "$doneCount of $total habits completed today. $remaining remaining before midnight."
        }
        val dayStreak = computeDayStreak(habits, app)
        binding.tvStreakCount.text = "$dayStreak"
    }

    private suspend fun updateStats(habits: List<HabitEntity>, app: CareTrackerApp) {
        val total = habits.size
        val todayDone = habits.count {
            val log = app.repository.getHabitLogForDate(it.id, today)
            (log?.count ?: 0) >= it.targetCount
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
                habits.forEach {
                    val log = app.repository.getHabitLogForDate(it.id, dateStr)
                    if ((log?.count ?: 0) >= it.targetCount) weekDone++
                }
            }
            cal.add(Calendar.DAY_OF_MONTH, 1)
        }
        val weekPct = if (weekTotal > 0) (weekDone * 100) / weekTotal else 0
        binding.tvStatWeek.text = "$weekPct%"

        val bestStreak = habits.maxOfOrNull { h ->
            var s = 0
            val c = Calendar.getInstance()
            val sdf2 = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            var d = sdf2.format(c.time)
            var running = true
            while (running) {
                val log = app.repository.getHabitLogForDate(h.id, d)
                if ((log?.count ?: 0) >= h.targetCount) {
                    s++
                    c.add(Calendar.DAY_OF_MONTH, -1)
                    d = sdf2.format(c.time)
                } else {
                    running = false
                }
            }
            s
        } ?: 0
        binding.tvStatBestStreak.text = "$bestStreak"
    }

    private suspend fun computeStreak(habitId: Long, app: CareTrackerApp): Int {
        val logs = app.repository.getAllLogsForHabit(habitId)
        val dates = logs.map { it.loggedDate }.toSortedSet(reverseOrder())
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val cal = Calendar.getInstance()
        var streak = 0
        var checkDate = sdf.format(cal.time)
        while (dates.contains(checkDate)) {
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
            val anyLogged = habits.any {
                val log = app.repository.getHabitLogForDate(it.id, dateStr)
                (log?.count ?: 0) >= it.targetCount
            }
            if (anyLogged) {
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

    private fun handleHabitTap(habit: HabitEntity) {
        val app = requireActivity().application as CareTrackerApp
        lifecycleScope.launch {
            val existing = app.repository.getHabitLogForDate(habit.id, today)

            if (habit.targetCount <= 1) {
                if (existing == null) {
                    app.repository.insertHabitLog(HabitLogEntity(habitId = habit.id, loggedDate = today, count = 1))
                } else {
                    app.repository.deleteHabitLog(existing)
                }
            } else {
                val newCount = (existing?.count ?: 0) + 1
                if (existing == null) {
                    app.repository.insertHabitLog(
                        HabitLogEntity(habitId = habit.id, loggedDate = today, count = newCount)
                    )
                } else {
                    app.repository.insertHabitLog(existing.copy(count = newCount))
                }
            }

            refreshHabitList(currentHabits, app)
            updateStats(currentHabits, app)
        }
    }

    private fun handleHabitLongPress(habit: HabitEntity) {
        val app = requireActivity().application as CareTrackerApp
        if (habit.targetCount > 1) {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(habit.name)
                .setItems(arrayOf("Enter count", "Edit habit")) { _, which ->
                    when (which) {
                        0 -> showManualCountDialog(habit, app)
                        1 -> showEditHabitDialog(habit, app)
                    }
                }
                .show()
        } else {
            showEditHabitDialog(habit, app)
        }
    }

    private fun showManualCountDialog(habit: HabitEntity, app: CareTrackerApp) {
        val input = TextInputEditText(requireContext()).apply {
            hint = "Enter count"
            inputType = InputType.TYPE_CLASS_NUMBER
            setPadding(48, 32, 48, 16)
        }

        lifecycleScope.launch {
            val existing = app.repository.getHabitLogForDate(habit.id, today)
            input.setText((existing?.count ?: 0).toString())
            input.setSelection(input.text?.length ?: 0)

            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Set count for ${habit.name}")
                .setMessage("Target: ${habit.targetCount}")
                .setView(input)
                .setPositiveButton("Save") { _, _ ->
                    val value = input.text?.toString()?.toIntOrNull() ?: 0
                    lifecycleScope.launch {
                        val current = app.repository.getHabitLogForDate(habit.id, today)
                        when {
                            value <= 0 && current != null -> app.repository.deleteHabitLog(current)
                            value > 0 && current == null -> app.repository.insertHabitLog(
                                HabitLogEntity(habitId = habit.id, loggedDate = today, count = value)
                            )
                            value > 0 && current != null -> app.repository.insertHabitLog(
                                current.copy(count = value)
                            )
                        }
                        refreshHabitList(currentHabits, app)
                        updateStats(currentHabits, app)
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    private suspend fun observeJournal(userId: Long, app: CareTrackerApp) {
        val todayEntry = app.repository.getMoodEntryForDate(userId, today)
        todayEntry?.let {
            binding.etJournalContent.setText(it.content ?: "")
            binding.etJournalTags.setText(it.tags ?: "")
            selectedMoodScore = it.moodScore
            it.moodScore?.let { score ->
                val idx = score - 1
                if (idx in 0 until binding.layoutMoodPicker.childCount) {
                    for (i in 0 until binding.layoutMoodPicker.childCount) {
                        binding.layoutMoodPicker.getChildAt(i).alpha = 0.3f
                    }
                    binding.layoutMoodPicker.getChildAt(idx).alpha = 1f
                }
            }
        }
        app.repository.getMoodEntries(userId).collect { entries ->
            val past = entries.filter { it.entryDate != today }
            journalAdapter.submitList(past)
            binding.tvPastEntriesHeader.isVisible = past.isNotEmpty()
            binding.rvJournalEntries.isVisible = past.isNotEmpty()
        }
    }

    private fun showEditHabitDialog(habit: HabitEntity, app: CareTrackerApp) {
        val layout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 24, 48, 8)
        }

        val nameInput = TextInputEditText(requireContext()).apply {
            hint = "Habit name"
            setText(habit.name)
            setSelection(habit.name.length)
        }

        val targetInput = TextInputEditText(requireContext()).apply {
            hint = "Target count (1 = simple check-off)"
            inputType = InputType.TYPE_CLASS_NUMBER
            setText(habit.targetCount.toString())
        }

        layout.addView(nameInput)
        layout.addView(targetInput)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Edit Habit")
            .setView(layout)
            .setPositiveButton("Save") { _, _ ->
                val updatedName = nameInput.text?.toString()?.trim().orEmpty()
                val targetCount = targetInput.text?.toString()?.toIntOrNull()?.coerceAtLeast(1) ?: 1
                if (updatedName.isNotEmpty()) {
                    lifecycleScope.launch {
                        app.repository.updateHabit(
                            habit.copy(name = updatedName, targetCount = targetCount)
                        )
                        Toast.makeText(requireContext(), "Habit updated", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNeutralButton("Delete") { _, _ ->
                confirmDeleteHabit(habit, app)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun confirmDeleteHabit(habit: HabitEntity, app: CareTrackerApp) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Delete Habit")
            .setMessage("Delete '${habit.name}'? This cannot be undone.")
            .setPositiveButton("Delete") { _, _ ->
                lifecycleScope.launch {
                    app.repository.deleteHabit(habit)
                    Toast.makeText(requireContext(), "Habit deleted", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showAddHabitDialog(userId: Long, app: CareTrackerApp) {
        val layout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(48, 24, 48, 8)
        }

        val nameInput = TextInputEditText(requireContext()).apply {
            hint = "Habit name (e.g. Drink 8 Glasses)"
        }

        val targetInput = TextInputEditText(requireContext()).apply {
            hint = "Target count (1 = simple check-off)"
            inputType = InputType.TYPE_CLASS_NUMBER
            setText("1")
        }

        layout.addView(nameInput)
        layout.addView(targetInput)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("New Habit")
            .setView(layout)
            .setPositiveButton("Add") { _, _ ->
                val name = nameInput.text?.toString()?.trim().orEmpty()
                val targetCount = targetInput.text?.toString()?.toIntOrNull()?.coerceAtLeast(1) ?: 1
                if (name.isNotEmpty()) {
                    lifecycleScope.launch {
                        app.repository.insertHabit(
                            HabitEntity(userId = userId, name = name, targetCount = targetCount)
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
