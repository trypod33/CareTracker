package com.caretracker

import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.caretracker.databinding.ActivityMainBinding
import com.caretracker.ui.calendar.CalendarFragment
import com.caretracker.ui.carecircle.CareCircleFragment
import com.caretracker.ui.dashboard.DashboardFragment
import com.caretracker.ui.habits.HabitsFragment
import com.caretracker.ui.health.HealthFragment
import com.caretracker.ui.medications.MedicationsFragment
import com.caretracker.ui.profile.ProfileSwitcherFragment
import com.caretracker.ui.reports.ReportsFragment
import com.caretracker.data.repository.PersonRepository
import com.caretracker.utils.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @Inject lateinit var sessionManager: SessionManager
    @Inject lateinit var personRepository: PersonRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        // Hide default title — we manage it ourselves
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val toggle = ActionBarDrawerToggle(
            this, binding.drawerLayout, binding.toolbar,
            R.string.nav_open, R.string.nav_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Observe active person and keep profile chip in sync
        lifecycleScope.launch {
            personRepository.observeActivePerson().collectLatest { person ->
                if (person != null) {
                    sessionManager.activePersonId = person.id
                    binding.tvProfileAvatar.text = person.avatar
                    binding.tvProfileName.text = person.name
                } else {
                    binding.tvProfileAvatar.text = "\uD83D\uDC64"
                    binding.tvProfileName.text = "Select Profile"
                }
            }
        }

        // Profile chip tap → show switcher bottom sheet
        binding.profileChip.setOnClickListener {
            val sheet = ProfileSwitcherFragment()
            sheet.onProfileSelected = { person ->
                binding.tvProfileAvatar.text = person.avatar
                binding.tvProfileName.text = person.name
                // Refresh current fragment so it reloads data for new person
                val current = supportFragmentManager
                    .findFragmentById(R.id.fragmentContainer)
                current?.let {
                    supportFragmentManager.beginTransaction()
                        .detach(it).attach(it).commit()
                }
            }
            sheet.onAddPersonClicked = {
                // Navigate to Care Circle where persons are managed
                replaceFragment(CareCircleFragment(), "Care Circle")
                binding.navView.setCheckedItem(R.id.nav_care_circle)
            }
            sheet.show(supportFragmentManager, ProfileSwitcherFragment.TAG)
        }

        if (savedInstanceState == null) {
            replaceFragment(DashboardFragment(), "Dashboard")
            binding.navView.setCheckedItem(R.id.nav_dashboard)
        }

        binding.navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_dashboard   -> replaceFragment(DashboardFragment(),   "Dashboard")
                R.id.nav_habits      -> replaceFragment(HabitsFragment(),      "Habits")
                R.id.nav_health      -> replaceFragment(HealthFragment(),      "Health")
                R.id.nav_medications -> replaceFragment(MedicationsFragment(), "Medications")
                R.id.nav_calendar    -> replaceFragment(CalendarFragment(),    "Calendar")
                R.id.nav_care_circle -> replaceFragment(CareCircleFragment(),  "Care Circle")
                R.id.nav_reports     -> replaceFragment(ReportsFragment(),     "Reports")
                else                 -> replaceFragment(DashboardFragment(),   "Dashboard")
            }
            binding.drawerLayout.closeDrawers()
            true
        }
    }

    fun replaceFragment(fragment: Fragment, pageTitle: String) {
        binding.tvPageTitle.text = pageTitle
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}
