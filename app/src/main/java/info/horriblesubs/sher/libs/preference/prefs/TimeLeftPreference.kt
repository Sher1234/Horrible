package info.horriblesubs.sher.libs.preference.prefs

import androidx.core.content.edit
import info.horriblesubs.sher.R
import info.horriblesubs.sher.libs.preference.model.BasePreference
import info.horriblesubs.sher.libs.preference.model.SwitchPreference

object TimeLeftPreference: SwitchPreference() {

    override val key: String = "formats-time-left-passed"
    override val defaultValue: Boolean = true

    init {
        summary = "This will display remaining time for next airing episodes or the time passed" +
                " since the cache was last updated from the server."
        summaryProvider = object: TextProvider<Boolean> {
            override fun provideText(preference: BasePreference<Boolean>): String? {
                return if (preference.value) "Relative time is currently enabled."
                    else "This will display relative time for next airing episodes or" +
                            " time since the server cache was last updated."
            }
        }
        setIcon(R.drawable.ic_remaining)
        title = "Relative Time"
    }

    override fun migrate() {
        val value = sharedPreferences?.getBoolean("time_left", defaultValue) ?: defaultValue
        sharedPreferences?.edit {
            remove("time_left")
            putBoolean(key, value)
        }
    }
}