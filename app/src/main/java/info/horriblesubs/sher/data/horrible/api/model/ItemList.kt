package info.horriblesubs.sher.data.horrible.api.model

open class ItemList(
    var ongoing: Boolean = false,
    schedule: Time? = null,
    mal_id: String? = null,
    var body: String = "",
    image: String? = null,
    sid: String? = null,
    title: String = "",
    rating: Float = 0f,
    link: String = "",
    id: String = "",
    views: Int = 0,
    users: Int = 0,
    favs: Int = 0
): ItemSchedule(
    image = image, title = title, link = link, sid = sid, id = id,
    favs = favs, users = users, views = views, rating = rating,
    mal_id = mal_id, schedule = schedule
)