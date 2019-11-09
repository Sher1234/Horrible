package info.horriblesubs.sher.api.horrible.model

import info.horriblesubs.sher.common.Formats
import info.horriblesubs.sher.common.timePassed
import info.horriblesubs.sher.common.toZonedDateTime
import java.time.ZonedDateTime

open class ItemShow(
    val episodes: MutableList<Release>?,
    val batches: MutableList<Release>?,
    private val time: String?,
    val ongoing: Boolean?,
    featured: Boolean?,
    schedule: Time?,
    mal_id: String?,
    rating: Float?,
    title: String?,
    image: String?,
    body: String?,
    link: String?,
    sid: String?,
    views: Int?,
    users: Int?,
    id: String?,
    favs: Int?
): ItemSchedule(schedule, featured, mal_id, rating, title, image, body, link, sid, views, users, id, favs) {
    private fun time(): ZonedDateTime? {
        return time?.toZonedDateTime()
    }
    fun timePassed(): String? {
        return time()?.timePassed(ZonedDateTime.now())
    }
    fun time(formats: Formats): String? {
        return time()?.format(formats.formatter)
    }
}