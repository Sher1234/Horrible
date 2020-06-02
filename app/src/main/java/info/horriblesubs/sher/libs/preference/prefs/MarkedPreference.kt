package info.horriblesubs.sher.libs.preference.prefs

import androidx.core.content.edit
import info.horriblesubs.sher.R
import info.horriblesubs.sher.libs.preference.model.BasePreference
import info.horriblesubs.sher.libs.preference.model.SwitchPreference

object MarkedPreference: SwitchPreference() {

    override val defaultValue: Boolean = true
    override val key: String = "marked"

    init {
        summaryProvider = object : TextProvider<Boolean> {
            override fun provideText(preference: BasePreference<Boolean>): String? {
                return if (preference.value)
                    "A marker is being displayed in the front of latest releases that are present in the library. Available on Latest Releases only (for now*)."
                else
                    "This will display a marker in front of releases that are present in the bookmarks."
            }
        }
        setIcon(R.drawable.ic_library)
        title = "Mark releases"
    }

    override fun migrate() {
        val value = sharedPreferences?.getBoolean("marked_fav", defaultValue) ?: defaultValue
        sharedPreferences?.edit {
            remove("marked_fav")
            putBoolean(key, value)
        }
    }
}