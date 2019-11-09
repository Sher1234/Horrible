package info.horriblesubs.sher.ui.main

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import info.horriblesubs.sher.R
import info.horriblesubs.sher.ui.main.MainActivity.Keys.*
import info.horriblesubs.sher.ui.main.bookmarked.Bookmarked
import info.horriblesubs.sher.ui.main.explore.Explore
import info.horriblesubs.sher.ui.main.schedule.Schedule
import info.horriblesubs.sher.ui.main.settings.Settings
import info.horriblesubs.sher.ui.main.shows.Shows

class MainActivity: AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var vm: MainVM

    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)
        setContentView(R.layout._b_activity)
        val position = intent.getIntExtra("link", R.id.bookmarked)
        val nav: BottomNavigationView = findViewById(R.id.navigationView)
        vm = ViewModelProvider(this).get(MainVM::class.java)
        if (vm.position == null) vm.position = position
        nav.setOnNavigationItemSelectedListener(this)
        nav.selectedItemId = vm.position?:position
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.bookmarked -> swapFragments(item.itemId, BOOKMARKED)
            R.id.schedule -> swapFragments(item.itemId, SCHEDULE)
            R.id.settings -> swapFragments(item.itemId, SETTINGS)
            R.id.explore -> swapFragments(item.itemId, EXPLORE)
            R.id.shows -> swapFragments(item.itemId, SHOWS)
            else -> false
        }
    }

    private fun swapFragments(id: Int, keys: Keys): Boolean {
        val oldFragment = supportFragmentManager.findFragmentById(R.id.fragment)
        val newFragment = supportFragmentManager.findFragmentByTag(keys.name)
        if (oldFragment == newFragment && newFragment != null) return true
        fragment(keys, id)
        return true
    }

    private fun fragment(key: Keys, id: Int) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment, key.fragment, key.name).commit()
        vm.position = id
    }

    private enum class Keys {
        EXPLORE, BOOKMARKED, SHOWS, SCHEDULE, SETTINGS;
        internal val fragment: Fragment
            get() = when (this) {
                BOOKMARKED -> Bookmarked()
                SCHEDULE -> Schedule()
                SETTINGS -> Settings()
                EXPLORE -> Explore()
                SHOWS -> Shows()
            }
    }
}