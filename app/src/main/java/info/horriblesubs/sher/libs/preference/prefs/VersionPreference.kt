package info.horriblesubs.sher.libs.preference.prefs

import androidx.core.content.edit
import info.horriblesubs.sher.BuildConfig
import info.horriblesubs.sher.libs.preference.model.BasePreference

object VersionPreference: BasePreference<Int>() {

    override val defaultValue = BuildConfig.VERSION_CODE
    override val key: String get() = "version"
    override val type: Int get() = DEFAULT
    override var value = defaultValue

    init {
        summary = BuildConfig.VERSION_NAME
        title = "Application version"
    }

    override fun migrate() {
        sharedPreferences?.edit {
            remove("version")
            putInt(key, value)
        }
    }
}