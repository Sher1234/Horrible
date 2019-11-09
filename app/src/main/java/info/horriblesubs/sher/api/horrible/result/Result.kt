package info.horriblesubs.sher.api.horrible.result

import info.horriblesubs.sher.common.Formats
import info.horriblesubs.sher.common.timePassed
import info.horriblesubs.sher.common.toZonedDateTime
import java.time.ZonedDateTime

open class Result<T>(private val time: String?, val items: List<T>?) {
    internal val dateTime: ZonedDateTime?
        get() =  time?.toZonedDateTime()

    fun dateTime(formats: Formats): String? {
        return dateTime?.format(formats.formatter)
    }

    fun timePassed(): String {
        return dateTime?.timePassed(ZonedDateTime.now()) ?: "Never"
    }

    override fun toString(): String {
        return "time: $time\nitems: $items"
    }
}

fun <T> Result<T>?.isNull(): Boolean {
    return this == null || this.dateTime == null
}