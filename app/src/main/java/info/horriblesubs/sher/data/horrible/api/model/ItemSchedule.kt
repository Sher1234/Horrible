package info.horriblesubs.sher.data.horrible.api.model

open class ItemSchedule(
    var schedule: Time? = null,
    var mal_id: String? = null,
    image: String? = null,
    sid: String? = null,
    title: String = "",
    rating: Float = 0f,
    link: String = "",
    id: String = "",
    views: Int = 0,
    users: Int = 0,
    favs: Int = 0
): ItemBase(
    title = title, image = image, link = link, sid = sid,
    id = id, favs = favs, users = users, views = views,
    rating = rating
) {
    data class Time(
        var scheduled: Boolean = false,
        var date: String?,
        var zone: String?,
        var time: String?
    )
}