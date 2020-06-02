package info.horriblesubs.sher.libs.preference.prefs

import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import info.horriblesubs.sher.R
import info.horriblesubs.sher.libs.preference.model.BasePreference
import info.horriblesubs.sher.libs.preference.model.ListPreference

object ThemePreference: ListPreference<Int>() {

    override val defaultValue: Int get() =
        if (VERSION.SDK_INT >= VERSION_CODES.Q) AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        else AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
    override val key: String = "theme"
    override var value: Int
        get() = sharedPreferences?.getInt(key, defaultValue) ?: defaultValue
        set(value) { sharedPreferences?.edit()?.putInt(key, value)?.apply() }

    init {
        addEntryValues(
            AppCompatDelegate.MODE_NIGHT_YES,
            AppCompatDelegate.MODE_NIGHT_NO,
            defaultValue
        )
        addEntries(
            "Dark", "Light",
            if (VERSION.SDK_INT >= VERSION_CODES.Q)
                "System default" else "Follow battery saver"
        )
        setIcon(R.drawable.ic_theme)
        title = "Application theme"
        summaryProvider = object: TextProvider<Int> {
            override fun provideText(preference: BasePreference<Int>): String? {
                return when (preference.value) {
                    AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY -> "Following Battery Saver Settings"
                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> "Following System Settings"
                    AppCompatDelegate.MODE_NIGHT_YES -> "Dark Mode Enabled"
                    AppCompatDelegate.MODE_NIGHT_NO -> "Dark Mode Disabled"
                    else -> "Following System Settings"
                }
            }
        }
    }

    override fun migrate() {
        val value = when(sharedPreferences?.getString("theme", null)) {
                "light" -> entryValues[1]
                "dark" -> entryValues[0]
                else -> entryValues[2]
        }
        sharedPreferences?.edit {
            remove("theme")
            putInt(key, value)
        }
    }
}