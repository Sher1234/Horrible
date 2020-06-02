package info.horriblesubs.sher.data.horrible.api.model

class ItemLatest(
    var quality: ArrayList<Boolean>? = arrayListOf(false, false, false),
    var release: String? = null,
    image: String? = null,
    sid: String? = null,
    title: String = "",
    rating: Float = 0f,
    link: String = "",
    id: String = "",
    users: Int = 0,
    views: Int = 0,
    favs: Int = 0
): ItemBase(
    title = title, image = image, link = link, sid = sid,
    id = id, favs = favs, users = users, views = views,
    rating = rating
)