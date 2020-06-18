package info.horriblesubs.sher

import androidx.appcompat.app.AppCompatDelegate
import info.horriblesubs.sher.libs.preference.model.BasePreference
import info.horriblesubs.sher.libs.preference.prefs.*

object SettingsMigrationHelper: BasePreference<Int>() {

    override val key: String = "migrate"
    override val defaultValue: Int = 9
    override val type: Int = DEFAULT

    override var value: Int
        get() = sharedPreferences?.getInt(key, defaultValue) ?: defaultValue
        set(value) { sharedPreferences?.edit()?.putInt(key, value)?.apply() }

    private val isAvailable = sharedPreferences?.let {
        it.contains("marked_fav") || it.contains("notifications") ||
                it.contains("fav_notifications") || it.getInt(key, Int.MAX_VALUE) < value
    } ?: false

    fun onMigrate() {
        val notificationsPreference = NotificationPreference
        val themePreference = ThemePreference
        if (isAvailable) {
            notificationsPreference.migrate()
            TimeFormatPreference.migrate()
            TimeLeftPreference.migrate()
            VersionPreference.migrate()
            MarkedPreference.migrate()
            TokenPreference.migrate()
            themePreference.migrate()

            sharedPreferences?.edit()?.putInt(key, value)?.apply()
        }
        AppCompatDelegate.setDefaultNightMode(themePreference.value)
        NotificationPreference.setDefaultNotificationMode(notificationsPreference.value)
    }
}
