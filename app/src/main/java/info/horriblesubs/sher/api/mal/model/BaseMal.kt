package info.horriblesubs.sher.api.mal.model

import com.google.gson.annotations.SerializedName
import info.horriblesubs.sher.common.Formats
import info.horriblesubs.sher.common.timeLeft
import info.horriblesubs.sher.common.timePassed
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

open class BaseMal(
    @SerializedName("mal_id")
    val malId: String?,
    val type: String?,
    val name: String?,
    val url: String?
)

class DateMAL(private val s: String?) {
    private var dateTime: ZonedDateTime? = null
        get() {
            if (s == null) return null
            if (field == null)
                field = ZonedDateTime.parse(s, DateTimeFormatter.ISO_DATE_TIME)
                    .withZoneSameInstant(ZoneId.systemDefault())
            return field
        }
    val day: String get() =  if (dateTime == null) "?" else Formats.DAY.formatter.format(dateTime)
    fun format(formats: Formats): String {
        return if (dateTime == null) "?" else formats.formatter.format(dateTime) ?: "?"
    }
    fun timeZ(now: ZonedDateTime = ZonedDateTime.now()): String {
        return when {
            dateTime == null -> "?"
            now.isAfter(dateTime) -> dateTime.timePassed(now)
            now.isBefore(dateTime) -> dateTime.timeLeft(now)
            else -> "?"
        }
    }
}

