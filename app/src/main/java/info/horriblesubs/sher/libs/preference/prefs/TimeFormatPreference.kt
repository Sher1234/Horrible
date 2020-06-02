package info.horriblesubs.sher.libs.preference.prefs

import androidx.core.content.edit
import info.horriblesubs.sher.App
import info.horriblesubs.sher.R
import info.horriblesubs.sher.functions.format
import info.horriblesubs.sher.libs.preference.model.BasePreference
import info.horriblesubs.sher.libs.preference.model.ListPreference
import java.time.ZonedDateTime

object TimeFormatPreference: ListPreference<Int>() {

    override val key: String = "formats-time"
    override val defaultValue: Int = -100
    override var value: Int
        get() = sharedPreferences?.getInt(key, defaultValue) ?: defaultValue
        set(value) { sharedPreferences?.edit()?.putInt(key, value)?.apply() }

    init {
        addEntryValues(0, 100, -100)
        addEntries(
            "System Default", "16:54", "04:54 PM"
        )
        summaryProvider = object: TextProvider<Int> {
            override fun provideText(preference: BasePreference<Int>): String? {
                return when (preference.value) {
                    100 -> "Using 24 hour time format."
                    -100 -> "Using 12 hour time format with AM/PM."
                    else -> App.get().getString(R.string.def_time_desc)
                }
            }
        }
        setIcon(R.drawable.ic_format)
        title = "In App Time Format"
    }

    fun format(dateTime: ZonedDateTime?, isTimeOnly: Boolean = false): String? {
        return when (value) {
            100 -> {
                if (isTimeOnly) dateTime.format(
                    isDefault = false,
                    withTime = true,
                    is24Hour = true
                ) else dateTime.format(is24Hour = true)
            }
            -100 -> {
                if (isTimeOnly) dateTime.format(
                    isDefault = false,
                    is24Hour = false,
                    withTime = true
                ) else dateTime.format(is24Hour = false)
            }
            else -> {
                if (isTimeOnly) dateTime.format(
                    isDefault = false,
                    withTime = true,
                    is24Hour = null
                ) else dateTime.format(is24Hour = null)
            }
        }
    }

    override fun migrate() {
        val value = when(sharedPreferences?.getString("time_format", null)) {
            "hh:mm a" -> entryValues[2]
            "HH:mm" -> entryValues[1]
            else -> entryValues[0]
        }
        sharedPreferences?.edit {
            remove("time_format")
            putInt(key, value)
        }
    }
}