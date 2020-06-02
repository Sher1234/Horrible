package info.horriblesubs.sher.data.horrible.api.model

data class ItemRelease (
    internal var downloads: ArrayList<ArrayList<Download>>? = arrayListOf(arrayListOf(), arrayListOf(), arrayListOf()),
    var quality: ArrayList<Boolean>? = arrayListOf(false, false, false),
    var release: String? = null,
    var title: String? = null,
    var sid: String? = null,
    var id: String? = null
) {
    data class Download (
        var source: String? = null,
        var link: String? = null,
    )
}

