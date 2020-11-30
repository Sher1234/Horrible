package info.horriblesubs.sher.data.subsplease.api.model

data class ItemSchedule(
    var schedule: LinkedHashMap<String, ArrayList<Show>> = linkedMapOf(),
    var tz: String = ""
) {
    data class Show(
        var image_url: String = "",
        var title: String = "",
        var page: String = "",
        var time: String = ""
    )
}