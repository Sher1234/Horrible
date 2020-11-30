package info.horriblesubs.sher.libs.preference.prefs

import androidx.core.content.edit
import com.google.firebase.messaging.FirebaseMessaging
import info.horriblesubs.sher.App
import info.horriblesubs.sher.R
import info.horriblesubs.sher.libs.preference.model.BasePreference
import info.horriblesubs.sher.libs.preference.model.ListPreference

object NotificationPreference: ListPreference<Int>() {

    override val key: String = "notification"
    override val defaultValue: Int = -100
    override var value: Int
        get() = sharedPreferences?.getInt(key, defaultValue) ?: defaultValue
        set(value) { sharedPreferences?.edit()?.putInt(key, value)?.apply() }

    init {
        addEntries("Every Release", "Bookmarked Only", "Never/Off")
        addEntryValues(100, 0, -100)
        summaryProvider = object: TextProvider<Int> {
            override fun provideText(preference: BasePreference<Int>) = when (preference.value) {
                100 -> {
                    setIcon(R.drawable.ic_notifications_all)
                    "You will be notified for every release available on SubsPlease."
                }
                0 -> {
                    setIcon(R.drawable.ic_notifications_some)
                    "You will only be notified for every release present in your Library."
                }
                else -> {
                    setIcon(R.drawable.ic_notifications_off)
                    App.get().getString(R.string.notification_desc)
                }
            }
        }
        title = "Release notifications"
    }

    override fun migrate() {
        val value = sharedPreferences?.let {
            if(it.getBoolean("notifications", false)) {
                if (it.getBoolean("fav_notifications", false)) entryValues[1]
                else entryValues[0]
            } else entryValues[2]
        } ?: defaultValue
        sharedPreferences?.edit {
            remove("fav_notifications")
            remove("notifications")
            putInt(key, value)
        }
    }

    fun setDefaultNotificationMode(value: Int) = when (value) {
        100, 0 -> FirebaseMessaging.getInstance().subscribeToTopic("anime.ongoing")
        else -> FirebaseMessaging.getInstance().unsubscribeFromTopic("anime.ongoing")
    }
}