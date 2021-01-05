package info.horriblesubs.sher.data.subsplease.api.model

data class ItemReleasePage(
    var episode: LinkedHashMap<String, ItemRelease>? = null,
    var batch: LinkedHashMap<String, ItemRelease>? = null
)