package info.horriblesubs.sher.data.mal.api.model.main.anime

import com.google.gson.annotations.SerializedName
import info.horriblesubs.sher.data.mal.api.model.common.search.MediaSearchItem

class AnimeSearchItem(
    @SerializedName("airing") var airing: Boolean? = null,
    @SerializedName("episodes") var episodes: Int? = null,
    @SerializedName("source") var source: String? = null,
    @SerializedName("rated") var rated: String? = null,
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