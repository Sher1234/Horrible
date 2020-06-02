package info.horriblesubs.sher.data.horrible.api.model

open class ItemShow(
    var episodes_some_timestamp: String? = null,
    var episodes_all_timestamp: String? = null,
    var episodes_timestamp: String? = null,
    var batches_timestamp: String? = null,
    var info_timestamp: String? = null,
    var featured: Boolean = false,
    ongoing: Boolean = false,
    mal_id: String? = null,
    schedule: Time? = null,
    image: String? = null,
    sid: String? = null,
    rating: Float = 0f,
    title: String = "",
    link: String = "",
    body: String = "",
    id: String = "",
    users: Int = 0,
    views: Int = 0,
    favs: Int = 0,
): ItemList(
    image = image, title = title, link = link, sid = sid, id = id,
    favs = favs, users = users, views = views, rating = rating,
    mal_id = mal_id, schedule = schedule,
    body = body, ongoing = ongoing
)