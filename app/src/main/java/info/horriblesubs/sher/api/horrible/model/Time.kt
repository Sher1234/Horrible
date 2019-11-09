package info.horriblesubs.sher.api.horrible.model

import info.horriblesubs.sher.common.Constants
import info.horriblesubs.sher.common.Formats
import info.horriblesubs.sher.common.timeLeft
import info.horriblesubs.sher.ui.main.settings.KeySettings
import java.io.Serializable
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters

class Time(
    private val time: String?,
    private val date: String?,
    private val zone: String?,
    val scheduled: Boolean?
): Serializable {
    private var dateTime: ZonedDateTime? = null
        get() {
            return if (field == null) {
                val day = DayOfWeek.from(LocalDate.parse(date, Formats.DATE.formatter))
                val date = LocalDate.now().with(TemporalAdjusters.nextOrSame(day))
                val time = LocalTime.parse(time, Formats.TIME_24H.formatter)
                val dateTime = ZonedDateTime.of(date, time, ZoneId.of(zone))
                field = dateTime.withZoneSameInstant(ZoneId.systemDefault())
                field
            } else field
        }
    fun time(formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(Constants.value(KeySettings.TimeFormat) as String)): String? {
        return dateTime?.format(formatter)
    }
    fun day(): DayOfWeek = DayOfWeek.from(dateTime)
    fun timeLeft(now: ZonedDateTime = ZonedDateTime.now()): String {
        return dateTime.timeLeft(now)
    }
}