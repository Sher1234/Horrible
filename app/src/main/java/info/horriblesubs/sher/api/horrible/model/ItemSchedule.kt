package info.horriblesubs.sher.api.horrible.model

open class ItemSchedule(
    val schedule: Time?,
    featured: Boolean?,
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
): ItemBase(featured, mal_id, rating, title, image, body, link, sid, views, users, id, favs)