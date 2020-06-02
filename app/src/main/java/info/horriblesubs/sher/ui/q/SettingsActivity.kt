package info.horriblesubs.sher.ui.q

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.iid.FirebaseInstanceId
import info.horriblesubs.sher.R
import info.horriblesubs.sher.libs.preference.PreferenceAdapter
import info.horriblesubs.sher.libs.preference.listeners.OnPreferenceChangeListener
import info.horriblesubs.sher.libs.preference.model.BasePreference
import info.horriblesubs.sher.libs.preference.model.GroupTitlePreference
import info.horriblesubs.sher.libs.preference.prefs.*
import info.horriblesubs.sher.libs.toolbar.Toolbar
import info.horriblesubs.sher.ui.setLinearLayoutAdapter
import info.horriblesubs.sher.ui.startBrowser
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.q_activity.*


class SettingsActivity: AppCompatActivity(), OnPreferenceChangeListener,
    Toolbar.OnToolbarActionListener {

    private val adapter by lazy {
        PreferenceAdapter(this, this)
    }

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.q_activity)
        recyclerView?.setLinearLayoutAdapter(adapter)
        toolbar?.apply {
            onToolbarActionListener = this@SettingsActivity
            setNavigationButton(R.drawable.ic_back) {
                finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        AppCompatDelegate.setDefaultNightMode(ThemePreference.value)
        adapter.addAll(
            GroupTitlePreference("Notification settings"),
            NotificationPreference, TokenPreference,
            GroupTitlePreference("Schedule settings"),
            TimeLeftPreference, TimeFormatPreference,
            GroupTitlePreference("Application settings"),
            ThemePreference, MarkedPreference,
            GroupTitlePreference("Others"),
            PrivacyPolicyPreference, GithubPreference,
            BuildDatePreference, VersionPreference
        )
    }

    override fun onDestroy() {
        clearFindViewByIdCache()
        super.onDestroy()
    }

    override fun <T> onPreferenceChange(preference: BasePreference<T>, position: Int) {
        when(preference) {
            is TimeFormatPreference, is TimeLeftPreference, is MarkedPreference -> {
                Toast.makeText(this, preference.title + " updated.", Toast.LENGTH_SHORT).show()
            }
            is PrivacyPolicyPreference, is GithubPreference -> {
                startBrowser(this, preference.value.toString())
            }
            is BuildDatePreference, is VersionPreference -> {
                Toast.makeText(this, preference.summary, Toast.LENGTH_SHORT).show()
            }
            is NotificationPreference -> {
                NotificationPreference.setDefaultNotificationMode(preference.value)
                Toast.makeText(this, preference.title + " updated.", Toast.LENGTH_SHORT).show()
            }
            is TokenPreference -> {
                Toast.makeText(this, "Fixing notification id...", Toast.LENGTH_SHORT).show()
                FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener {
                    if (it.isSuccessful)
                        preference.value = it.result?.token ?: ""
                    else
                        Toast.makeText(this, "Error fixing notification id.", Toast.LENGTH_SHORT).show()
                }
            }
            is ThemePreference -> {
                Toast.makeText(this, preference.summary, Toast.LENGTH_SHORT).show()
                AppCompatDelegate.setDefaultNightMode(preference.value)
            }
        }
    }

    override fun onQueryChanged(text: String?) {
        adapter.filter.filter(text)
    }
}