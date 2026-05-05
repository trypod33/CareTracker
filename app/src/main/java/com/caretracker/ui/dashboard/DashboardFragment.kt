package com.caretracker.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.caretracker.CareTrackerApp
import com.caretracker.databinding.FragmentDashboardBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private var snapshotJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val app = requireActivity().application as CareTrackerApp
        binding.tvDate.text =
            SimpleDateFormat("EEEE, MMMM d yyyy", Locale.getDefault()).format(Date())

        snapshotJob?.cancel()
        snapshotJob = viewLifecycleOwner.lifecycleScope.launch {
            app.currentUserIdFlow.collect { userId ->
                val user = app.repository.getUserById(userId)
                val name = user?.displayName ?: user?.username ?: "there"
                binding.tvWelcome.text = "Welcome, $name!"
                loadSnapshot(app, userId)
            }
        }
    }

    private suspend fun loadSnapshot(app: CareTrackerApp, userId: Long) {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        val habits  = app.repository.getHabitsForUser(userId).first()
        val medList = app.repository.getMedicationsForUser(userId).first()
        val tasks   = app.repository.getTasksForUser(userId).first()

        var habitDone = 0
        for (habit in habits) {
            val log = app.repository.getHabitLogForDate(habit.id, today)
            val done = if (habit.targetCount > 1)
                (log?.count ?: 0) >= habit.targetCount
            else
                log != null
            if (done) habitDone++
        }
        val habitTotal = habits.size

        var medsDone = 0
        for (med in medList) {
            if (app.repository.getMedLogsForDateOnce(med.id, today).isNotEmpty()) medsDone++
        }
        val medTotal = medList.size

        val openTasks = tasks.count { it.status != "done" }

        val score = ((habitDone + medsDone) * 10).coerceAtMost(99)
        binding.tvDashboardScore.text = score.toString()
        binding.tvStatHabits.text = "$habitDone/$habitTotal"
        binding.tvStatMeds.text   = "$medsDone/$medTotal"
        binding.tvStatTasks.text  = openTasks.toString()

        val habitPct = if (habitTotal > 0) (habitDone * 100) / habitTotal else 0
        val medPct   = if (medTotal   > 0) (medsDone * 100) / medTotal   else 0
        val taskPct  = if (tasks.isNotEmpty())
            ((tasks.size - openTasks) * 100) / tasks.size else 0

        binding.progressRing.progress = ((habitPct + medPct) / 2).coerceIn(0, 100)
        binding.tvProgressPct.text    = "${binding.progressRing.progress}%"

        binding.tvMomentumTitle.text = when {
            habitTotal == 0 && medTotal == 0 -> "Start your day"
            habitPct == 100 && medPct == 100 -> "Everything is on track"
            habitPct >= 70  || medPct >= 70  -> "Good momentum"
            else                             -> "Keep it moving"
        }
        binding.tvMomentumSub.text = when {
            habitTotal == 0 && medTotal == 0 ->
                "Add habits and medications to fill this snapshot."
            else ->
                "$habitDone of $habitTotal habits, $medsDone of $medTotal meds, $openTasks tasks open."
        }

        binding.progressHabits.progress = habitPct
        binding.progressMeds.progress   = medPct
        binding.progressTasks.progress  = taskPct
        binding.tvWeekSummary.text =
            "Habits ${habitPct}% · Meds ${medPct}% · Tasks $openTasks open"

        val urgent = mutableListOf<String>()
        var hasDueMeds = false
        for (med in medList) {
            if (app.repository.getMedLogsForDateOnce(med.id, today).isEmpty()) {
                hasDueMeds = true
                break
            }
        }
        if (hasDueMeds) urgent += "Some meds are still due today."
        if (habits.isNotEmpty() && habitDone < habits.size)
            urgent += "A few habits are still open."

        if (urgent.isEmpty()) {
            binding.tvAttention.text    = "No urgent items."
            binding.tvAttentionSub.text = "Everything looks stable right now."
        } else {
            binding.tvAttention.text    = urgent.first()
            binding.tvAttentionSub.text = urgent.drop(1).joinToString(" ")
                .ifBlank { "Tap into meds, habits, or readings to review details." }
        }

        binding.rvHabits.visibility =
            if (habits.isNotEmpty())  View.VISIBLE else View.GONE
        binding.rvMeds.visibility   =
            if (medList.isNotEmpty()) View.VISIBLE else View.GONE
        binding.rvTasks.visibility  =
            if (tasks.isNotEmpty())   View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        snapshotJob?.cancel()
        _binding = null
    }
}
