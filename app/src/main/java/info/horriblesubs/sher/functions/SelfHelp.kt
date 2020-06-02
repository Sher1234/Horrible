@file:JvmName("SelfHelp")

package info.horriblesubs.sher.functions

import android.content.res.Resources
import android.text.format.DateUtils
import androidx.core.text.HtmlCompat
import androidx.core.text.parseAsHtml
import info.horriblesubs.sher.App
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.temporal.Temporal
import java.util.*
import java.util.regex.Pattern
import kotlin.math.abs

val String.zonedDateTimeWithUTC get() = if (DateTimePattern.matcher(this).matches())
    ZonedDateTime.of(localDateTime, ZoneOffset.UTC).withZoneSameInstant(ZoneId.systemDefault())
else null

val String.localDateTime get() = if (DateTimePattern.matcher(this).matches())
    LocalDateTime.parse(replace(" ", "T")) else null

val String.localTime get() = when {
    TimeServerPattern.matcher(this).matches() || TimePattern.matcher(this).matches() ->
        LocalTime.parse(this)
    else -> null
}

val String.localDate get() = when {
    DatePatternISO.matcher(this).matches() -> LocalDate.parse(this)
    DateServerPattern.matcher(this).matches() -> LocalDate.parse(this,
        DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ROOT))
    else -> null
}

val DateTimePattern: Pattern = Pattern.compile("^((19|20)[0-9]{2})-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])[ Tt](([0-1][0-9])|(2[0-4])):([0-5][0-9]):([0-5][0-9])\$")
val DateServerPattern: Pattern = Pattern.compile("^((0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[0-2])-(19|20)[0-9]{2})\$")
val DatePatternISO: Pattern = Pattern.compile("^((19|20)[0-9]{2})-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])\$")
val TimePattern: Pattern = Pattern.compile("^(([0-1][0-9])|(2[0-4])):([0-5][0-9]):([0-5][0-9])\$")
val TimeServerPattern: Pattern = Pattern.compile("^(([0-1][0-9])|(2[0-4])):([0-5][0-9])\$")

val String?.parseAsHtml get() = (this?:"").parseAsHtml(HtmlCompat.FROM_HTML_MODE_COMPACT)

fun <T> orientation(portrait: T, landscape: T) =
    if (Resources.getSystem().configuration?.orientation == 2) landscape else portrait

@Suppress("deprecation")
fun ZonedDateTime?.format(
    isDefault: Boolean = true,
    withDate: Boolean = false,
    withTime: Boolean = false,
    withDay: Boolean = false,
    is24Hour: Boolean? = null,
    isAbbreviateAll: Boolean = false
) = this?.let {
    DateUtils.formatDateTime(
        App.get(), it.toInstant().toEpochMilli(), {
            var flags = if (isDefault or isAbbreviateAll) DateUtils.FORMAT_ABBREV_ALL else 0
            if (isDefault or withDay) flags = flags or DateUtils.FORMAT_SHOW_WEEKDAY
            if (isDefault or withDate) flags = flags or DateUtils.FORMAT_SHOW_DATE
            if (isDefault or withTime) flags = flags or when(is24Hour) {
                null -> DateUtils.FORMAT_SHOW_TIME
                true -> DateUtils.FORMAT_24HOUR or DateUtils.FORMAT_SHOW_TIME
                false -> DateUtils.FORMAT_12HOUR or DateUtils.FORMAT_SHOW_TIME
            }
            flags
        }()
    )?.toString()
}

fun getRelativeTime(startTime: Temporal?, endTime: Temporal?, isDetailed: Boolean = true): String? {
    if (startTime == null || endTime == null) return null
    val duration = Duration.between(endTime, startTime) ?: return null
    val s = if (duration.seconds > 0) "ago" else ""
    val p = if (duration.seconds > 0) "" else "In "
    val minutes = abs(duration.toMinutes())%60
    val seconds = abs(duration.seconds)%60
    val hours = abs(duration.toHours())%24
    val days = abs(duration.toDays())
    return (p +
            (if (days > 0) if (isDetailed) "${days}d " else return "$p$days days $s".trim() else "") +
            (if (hours > 0) if (isDetailed) "${hours}h " else return "$p$hours hours $s".trim() else "") +
            (if (minutes > 0) if (isDetailed) "${minutes}m " else return "$p$minutes minutes $s".trim() else "") +
            (if (seconds > 0) if (isDetailed) "${seconds}s " else return "$p$seconds seconds $s".trim() else "") +
            s).trim()
}