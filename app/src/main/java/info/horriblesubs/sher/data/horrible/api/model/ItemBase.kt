package info.horriblesubs.sher.data.horrible.api.model


open class ItemBase (
    var image: String? = null,
    var sid: String? = null,
    var title: String = "",
    var rating: Float = 0f,
    var link: String = "",
    var id: String = "",
    var views: Int = 0,
    var users: Int = 0,
    var favs: Int = 0
)