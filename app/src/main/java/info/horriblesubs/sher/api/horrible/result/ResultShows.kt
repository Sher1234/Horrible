package info.horriblesubs.sher.api.horrible.result

import info.horriblesubs.sher.api.horrible.model.ItemShow

class ResultShows(
    val current: MutableList<ItemShow>?,
    items: MutableList<ItemShow>?,
    time: String?
): Result<ItemShow>(time, items)