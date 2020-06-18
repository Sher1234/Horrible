package info.horriblesubs.sher.data.mal.api.model.common

import info.horriblesubs.sher.functions.getRelativeTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class JikanDateParse(private val s: String?) {
    private var dateTime: ZonedDateTime? = null
        get() {
            if (s == null) return null
            if (field == null)
                field = ZonedDateTime.parse(s, DateTimeFormatter.ISO_DATE_TIME)
                    .withZoneSameInstant(ZoneId.systemDefault())
            return field
        }

    val day get() =  if (dateTime == null) null else
        DateTimeFormatter.ofPattern("EEE").format(dateTime)

    fun format(formatter: DateTimeFormatter, default: String = "?") =
        if (dateTime == null) default else formatter.format(dateTime)?: default

    fun getRelativeTime(now: ZonedDateTime = ZonedDateTime.now()): String {
        return getRelativeTime(now, dateTime) ?: "Never"
    }
}