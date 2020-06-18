package info.horriblesubs.sher.data.mal.api.model.main.season

import com.google.gson.annotations.SerializedName
import info.horriblesubs.sher.data.mal.api.model.common.AnimeItem
import info.horriblesubs.sher.data.mal.api.model.common.base.BaseWithType

class SeasonItem(
    producers: ArrayList<BaseWithType>? = null,
    licensors: ArrayList<BaseWithType>? = null,
    genres: ArrayList<BaseWithType>? = null,
    airingStart: String? = null,
    synopsis: String? = null,
    imageURL: String? = null,
    source: String? = null,
    episodes: Int? = null,
    score: Double? = null,
    kids: Boolean? = null,
    title: String? = null,
    type: String? = null,
    members: Int? = null,
    r18: Boolean? = null,
    malId: Int? = null,
    url: String? = null,
    @SerializedName("continuing") var continuing: Boolean? = null
): AnimeItem(imageUrl = imageURL, title = title, malId = malId, url = url, episodes = episodes,
    airingStart = airingStart, synopsis = synopsis, score = score, source = source,
    members =  members, kids = kids, type = type, r18 =  r18, licensors =  licensors,
    producers = producers, genres = genres)