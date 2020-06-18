@file:JvmName("HorribleModelFunctions")

package info.horriblesubs.sher.data.horrible.api

import info.horriblesubs.sher.data.horrible.api.model.ItemRelease
import info.horriblesubs.sher.data.horrible.api.model.ItemSchedule
import info.horriblesubs.sher.data.horrible.api.model.ItemShow
import info.horriblesubs.sher.data.horrible.api.result.Result
import info.horriblesubs.sher.functions.*
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.TemporalAdjusters

//ItemRelease
val ItemRelease?.FHD get() = this?.downloads?.get(2) ?: emptyList()
val ItemRelease?.HD get() = this?.downloads?.get(1) ?: emptyList()
val ItemRelease?.SD get() = this?.downloads?.get(0) ?: emptyList()

//ItemSchedule's Time
val ItemSchedule.zonedDateTime: ZonedDateTime? get() {
    return if (schedule?.scheduled == true && isValid) {
        val day = DayOfWeek.from(schedule?.date?.localDate ?: return null)
        val date = LocalDate.now().with(TemporalAdjusters.nextOrSame(day))
        val time = schedule?.time?.localTime ?: return null
        ZonedDateTime.of(date, time, ZoneId.of(schedule?.zone))
            .withZoneSameInstant(ZoneId.systemDefault()) ?: return null
    } else null
}

val ItemSchedule.isValid: Boolean get() {
    return !schedule?.date.isNullOrBlank() && !schedule?.zone.isNullOrBlank() && !schedule?.time.isNullOrBlank()
}

fun ItemSchedule.left(now: ZonedDateTime): String {
    return getRelativeTime(now, zonedDateTime) ?: "Never"
}

val ItemSchedule.day: DayOfWeek? get() {
    return DayOfWeek.from(zonedDateTime ?: return null)
}

val ItemSchedule?.shortDisplay: String get() {
    return this?.zonedDateTime?.let { "${
            it.format(isDefault = false, withDay = true)
        }'s at ${
            it.format(isDefault = false, withTime = true, isAbbreviateAll = true)
        }"
    } ?: "Not Scheduled"
}


//ItemShow & ResultShow
val ItemShow?.someEpisodesTimeStamp get() = this?.episodes_some_timestamp?.zonedDateTimeWithUTC
val ItemShow?.allEpisodesTimeStamp get() = this?.episodes_all_timestamp?.zonedDateTimeWithUTC
val ItemShow?.detailTimeStamp get() = this?.info_timestamp?.zonedDateTimeWithUTC

val ItemShow?.detailAgo: String get() {
    return getRelativeTime(ZonedDateTime.now(), detailTimeStamp) ?: "Never"
}
//ServerResult
val <T> Result<T>?.dateTime get() = this?.time?.zonedDateTimeWithUTC