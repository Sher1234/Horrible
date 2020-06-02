package info.horriblesubs.sher.ui.b

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.bottomnavigation.BottomNavigationView
import info.horriblesubs.sher.R
import info.horriblesubs.sher.functions.GoogleAds
import info.horriblesubs.sher.libs.preference.prefs.ThemePreference
import info.horriblesubs.sher.ui.BaseFragment
import info.horriblesubs.sher.ui.EXTRA_DATA
import info.horriblesubs.sher.ui._extras.listeners.OnBackPressedListener
import info.horriblesubs.sher.ui.b.explore.ExploreFragment
import info.horriblesubs.sher.ui.b.library.LibraryFragment
import info.horriblesubs.sher.ui.b.schedule.ScheduleFragment
import info.horriblesubs.sher.ui.b.shows.ShowsFragment
import info.horriblesubs.sher.ui.q.SettingsActivity
import info.horriblesubs.sher.ui.viewModels
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.b_activity.*

class MainActivity: AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private val model by viewModels<MainModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.b_activity)
        GoogleAds(this).getBannerAd(this, adBannerLayout)
        val itemId = intent.getIntExtra(EXTRA_DATA, R.id.library)
        if (model.itemId == null) model.itemId = itemId
        bottomNavigationView.setOnNavigationItemSelectedListener(this)
        bottomNavigationView.selectedItemId = model.itemId ?: itemId
    }

    override fun onResume() {
        super.onResume()
        AppCompatDelegate.setDefaultNightMode(ThemePreference.value)
    }

    override fun onDestroy() {
        clearFindViewByIdCache()
        super.onDestroy()
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment)
        if (fragment !is OnBackPressedListener || fragment.onBackPressed(this))
            super.onBackPressed()
    }

    private fun <T: BaseFragment> changeFragment(itemId: Int, fragment: T) {
        val visibleFragment = supportFragmentManager.findFragmentById(R.id.fragment)
        val newFragment = supportFragmentManager.findFragmentByTag(fragment.name)
        if (visibleFragment != newFragment || visibleFragment == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment, fragment, fragment.name).commit()
            model.itemId = itemId
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.settings -> startActivity(Intent(this, SettingsActivity::class.java))
            R.id.schedule -> changeFragment(item.itemId, ScheduleFragment())
            R.id.library -> changeFragment(item.itemId, LibraryFragment())
            R.id.explore -> changeFragment(item.itemId, ExploreFragment())
            R.id.shows -> changeFragment(item.itemId, ShowsFragment())
            else -> Unit
        }
        return true
    }
}