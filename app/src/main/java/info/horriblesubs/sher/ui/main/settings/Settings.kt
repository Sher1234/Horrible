package info.horriblesubs.sher.ui.main.settings

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.google.android.material.snackbar.Snackbar
import info.horriblesubs.sher.R
import info.horriblesubs.sher.common.Constants
import info.horriblesubs.sher.ui.main.settings.KeySettings.*

class Settings : PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener {

    private var notification: SwitchPreferenceCompat? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDivider(ColorDrawable(Color.TRANSPARENT))
        findPreference<Preference>(Token.key)?.summary = Constants.value(Token) as String?
        findPreference<ListPreference>(Theme.key)?.onPreferenceChangeListener = this
        findPreference<Preference>(Token.key)?.setOnPreferenceClickListener {
            Snackbar.make(view, "Device specific notification ID", Snackbar.LENGTH_SHORT).show()
            true
        }
        notification = findPreference(Notifications.key)
        Log.i("TAG", "onViewCreated: " + Constants.value(Token))
        toggleIcon(Constants.value(Notifications) as Boolean)
        notification?.onPreferenceChangeListener = this
    }

    override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
        when (preference?.key) {
            Theme.key -> Constants.theme(newValue.toString())
            Notifications.key -> {
                toggleIcon(newValue as Boolean)
                Constants.subscribe(newValue)
            }
            else -> return false
        }
        return true
    }

    private fun toggleIcon(b: Boolean = false) {
        val drawable = if(b) R.drawable.ic_notifications_active else R.drawable.ic_notifications_off
        notification?.icon = context?.let {ContextCompat.getDrawable(it, drawable)}
    }
}

enum class KeySettings(val key: String, val defaultValue: Any) {
    FavNotifications("fav_notifications", false),
    Notifications("notifications", false),
    TimeFormat("time_format", "HH:mm"),
    MarkedFav("marked_fav", true),
    TimeLeft("time_left", true),
    Theme("theme", "default"),
    Version("version", 0),
    Token("token", "")
}
