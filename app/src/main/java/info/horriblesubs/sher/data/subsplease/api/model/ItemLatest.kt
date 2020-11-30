package info.horriblesubs.sher.data.subsplease.api.model

data class ItemLatest(
    var downloads: List<Links> = arrayListOf(),
    var image_url: String = "",
    var episode: String = "",
    var time: String = "",
    var show: String = "",
    var xdcc: String = "",
    var page: String = ""
) {
    data class Links(
        var magnet: String = "",
        var res: String = ""
    )
}