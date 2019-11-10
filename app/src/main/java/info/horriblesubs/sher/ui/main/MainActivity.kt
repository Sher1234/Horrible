package info.horriblesubs.sher.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.ActivityResult
import com.google.android.play.core.install.model.AppUpdateType.FLEXIBLE
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability.UPDATE_AVAILABLE
import info.horriblesubs.sher.R
import info.horriblesubs.sher.common.Constants
import info.horriblesubs.sher.ui.main.MainActivity.Keys.*
import info.horriblesubs.sher.ui.main.bookmarked.Bookmarked
import info.horriblesubs.sher.ui.main.explore.Explore
import info.horriblesubs.sher.ui.main.schedule.Schedule
import info.horriblesubs.sher.ui.main.settings.Settings
import info.horriblesubs.sher.ui.main.shows.Shows

class MainActivity: AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener,
    InstallStateUpdatedListener, Observer<AppUpdateInfo?> {

    private lateinit var nav: BottomNavigationView
    private var manager: AppUpdateManager? = null
    private lateinit var model: MainVM

    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)
        setContentView(R.layout._b_activity)
        Constants.requestMigrate(this)
        model = ViewModelProvider(this).get(MainVM::class.java)
        checkPlayUpdate(AppUpdateManagerFactory.create(this))
        val position = intent.getIntExtra("link", R.id.bookmarked)
        if (model.position == null) model.position = position
        nav = findViewById(R.id.navigationView)
        nav.setOnNavigationItemSelectedListener(this)
        nav.selectedItemId = model.position?:position
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
        model.position = id
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

    private fun checkPlayUpdate(manager: AppUpdateManager? = this.manager) {
        this.manager = manager
        model.uInfo.observe(this, this)
        manager?.registerListener(this)
        if(model.uAvailable.value == null)
            manager?.appUpdateInfo?.addOnSuccessListener {
                if (it.updateAvailability() == UPDATE_AVAILABLE && it.isUpdateTypeAllowed(FLEXIBLE)) {
                    model.uAvailable.value = true
                    model.uInfo.value = it
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 987) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    Toast.makeText(this, "Downloading update", Toast.LENGTH_SHORT).show()
                }
                Activity.RESULT_CANCELED -> {
                    Toast.makeText(this, "Update cancelled", Toast.LENGTH_SHORT).show()
                }
                ActivityResult.RESULT_IN_APP_UPDATE_FAILED -> {
                    Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onStateUpdate(state: InstallState?) {
        if (state?.installStatus() == InstallStatus.DOWNLOADED) {
            Snackbar.make(nav, "Restart app to update", Snackbar.LENGTH_INDEFINITE)
                .setAction("Restart") {
                    manager?.completeUpdate()
                    manager?.unregisterListener(this)
                }.show()
        }
    }

    override fun onChanged(t: AppUpdateInfo?) {
        if (t != null && model.uAvailable.value == true)
            manager?.startUpdateFlowForResult(t, FLEXIBLE, this, 987)
    }
}