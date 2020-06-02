package info.horriblesubs.sher.libs.preference.prefs

import info.horriblesubs.sher.libs.preference.model.BasePreference

object TokenPreference: BasePreference<String>() {

    override val key: String get() = "token"
    override val type: Int get() = DEFAULT
    override val defaultValue = ""
    override var value: String
        get() = sharedPreferences?.getString(key, defaultValue) ?: defaultValue
        set(value) { sharedPreferences?.edit()?.putString(key, value)?.apply() }

    init {
        summaryProvider = object: TextProvider<String> {
            override fun provideText(preference: BasePreference<String>): String? {
                return if (value.isBlank())
                    "Notification ID not set., Tap to set Notification ID." else
                    "Tap to reset Notification ID."
            }
        }
        title = "App Notification ID"
    }
}