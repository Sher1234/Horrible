package info.horriblesubs.sher.ui.b

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import info.horriblesubs.sher.R
import info.horriblesubs.sher.databinding.BActivityBinding
import info.horriblesubs.sher.functions.GoogleAds
import info.horriblesubs.sher.libs.preference.prefs.ThemePreference
import info.horriblesubs.sher.ui.EXTRA_DATA
import info.horriblesubs.sher.ui._extras.listeners.OnBackPressedListener
import info.horriblesubs.sher.ui.b.explore.ExploreFragment
import info.horriblesubs.sher.ui.b.library.LibraryFragment
import info.horriblesubs.sher.ui.b.schedule.ScheduleFragment
import info.horriblesubs.sher.ui.b.settings.SettingsFragment
import info.horriblesubs.sher.ui.b.shows.ShowsFragment
import info.horriblesubs.sher.ui.viewBinding
import info.horriblesubs.sher.ui.viewModels

class MainActivity: AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private val binding by viewBinding<BActivityBinding>(R.layout.b_activity)
    private val model by viewModels<MainModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(this)
        val itemId = intent.getIntExtra(EXTRA_DATA, R.id.library)
        GoogleAds(this).getBannerAd(this, binding.adBannerLayout)
        if (model.itemId == null) model.itemId = itemId
        binding.bottomNavigationView.selectedItemId = model.itemId ?: itemId
    }

    override fun onResume() {
        super.onResume()
        AppCompatDelegate.setDefaultNightMode(ThemePreference.value)
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment)
        if (fragment !is OnBackPressedListener || fragment.onBackPressed(this))
            super.onBackPressed()
    }

    private fun changeFragment(itemId: Int, fragment: Fragment): Boolean {
        val newFragment = supportFragmentManager.findFragmentByTag(fragment.javaClass.name)
        val visibleFragment = supportFragmentManager.findFragmentById(R.id.fragment)
        if (visibleFragment != newFragment || visibleFragment == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment, fragment, fragment.javaClass.name).commit()
            model.itemId = itemId
        }
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem) = when(item.itemId) {
        R.id.settings -> changeFragment(item.itemId, SettingsFragment())
        R.id.schedule -> changeFragment(item.itemId, ScheduleFragment())
        R.id.explore -> changeFragment(item.itemId, ExploreFragment())
        R.id.library -> changeFragment(item.itemId, LibraryFragment())
        R.id.shows -> changeFragment(item.itemId, ShowsFragment())
        else -> false
    }
}