package info.horriblesubs.sher.data.subsplease.api.model

data class ItemTodaySchedule(
    var schedule: List<Show> = arrayListOf(),
    var tz: String = ""
) {
    data class Show(
        var aired: Boolean = false,
        var image_url: String = "",
        var title: String = "",
        var page: String = "",
        var time: String = ""
    )
}