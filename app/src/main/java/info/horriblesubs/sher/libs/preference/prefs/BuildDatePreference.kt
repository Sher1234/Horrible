package info.horriblesubs.sher.libs.preference.prefs

import info.horriblesubs.sher.BuildConfig
import info.horriblesubs.sher.functions.getRelativeTime
import info.horriblesubs.sher.libs.preference.model.BasePreference
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime

object BuildDatePreference: BasePreference<String>() {

    override val defaultValue = BuildConfig.BUILD_TIME
    override val key: String get() = "build-time"
    override val type: Int get() = DEFAULT
    override var value = defaultValue

    init {
        val time = ZonedDateTime.of(LocalDateTime.parse(value), ZoneOffset.UTC)
            .withZoneSameInstant(ZoneId.systemDefault())
        summaryProvider = object: TextProvider<String> {
            override fun provideText(preference: BasePreference<String>): String {
                return (if (TimeLeftPreference.value) getRelativeTime(ZonedDateTime.now(), time)
                    else TimeFormatPreference.format(time)) ?: value
            }
        }
        title = "Build time"
    }
}