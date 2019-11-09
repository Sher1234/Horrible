package info.horriblesubs.sher.api.horrible.model

class ItemRecent(
    val quality: List<Boolean>?,
    val release: String?,
    featured: Boolean?,
    mal_id: String?,
    rating: Float?,
    image: String?,
    title: String?,
    link: String?,
    body: String?,
    sid: String?,
    id: String?,
    users: Int?,
    views: Int?,
    favs: Int?
): ItemBase(featured, mal_id, rating, title, image, body, link, sid, views, users, id, favs)