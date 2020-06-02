package info.horriblesubs.sher.data.horrible.api.result

import info.horriblesubs.sher.data.horrible.api.model.ItemRelease
import info.horriblesubs.sher.data.horrible.api.model.ItemShow

class ResultShow(
    var episodes: Result<ItemRelease>? = null,
    var batches: Result<ItemRelease>? = null,
    episodes_some_timestamp: String? = null,
    episodes_all_timestamp: String? = null,
    episodes_timestamp: String? = null,
    batches_timestamp: String? = null,
    info_timestamp: String? = null,
    featured: Boolean = false,
    ongoing: Boolean = false,
    mal_id: String? = null,
    schedule: Time? = null,
    image: String? = null,
    sid: String? = null,
    title: String = "",
    rating: Float = 0f,
    body: String = "",
    link: String = "",
    id: String = "",
    users: Int = 0,
    views: Int = 0,
    favs: Int = 0
): ItemShow(
    episodes_some_timestamp = episodes_some_timestamp, title = title, link = link, views = views,
    episodes_all_timestamp = episodes_all_timestamp, favs = favs, body = body, sid = sid,
    episodes_timestamp = episodes_timestamp, info_timestamp = info_timestamp, id = id,
    batches_timestamp = batches_timestamp, image = image, mal_id = mal_id,
    schedule = schedule, rating = rating, users = users,
    ongoing = ongoing, featured = featured,
)