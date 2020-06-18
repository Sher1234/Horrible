package info.horriblesubs.sher.data.mal.api.model.main.manga

import com.google.gson.annotations.SerializedName
import info.horriblesubs.sher.data.mal.api.model.common.search.MediaSearchItem

class MangaSearchItem(
    @SerializedName("publishing") var publishing: Boolean? = null,
    @SerializedName("chapters") var chapters: Int? = null,
    @SerializedName("volumes") var volumes: Int? = null,
    startDate: String? = null,
    imageUrl: String? = null,
    synopsis: String? = null,
    endDate: String? = null,
    score: Double? = null,
    title: String? = null,
    type: String? = null,
    members: Int? = null,
    url: String? = null,
    malId: Int? = null,
): MediaSearchItem(startDate = startDate, synopsis = synopsis, endDate = endDate, members = members,
    score = score, title = title, type = type, url = url, malId = malId, imageUrl = imageUrl)