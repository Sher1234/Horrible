package info.horriblesubs.sher.data.subsplease.api.model

data class ItemRelease(
    var downloads : List<Links> = arrayListOf(),
    var episode : String = "",
    var time : String = "",
    var show : String = ""
) {
    data class Links(
        var torrent: String = "",
        var magnet: String = "",
        var xdcc: String = "",
        var res: String = ""
    )
}