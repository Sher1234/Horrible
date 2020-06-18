package info.horriblesubs.sher.data.mal.api.model.main.anime

import com.google.gson.annotations.SerializedName
import info.horriblesubs.sher.data.mal.api.model.common.base.BaseWithType
import info.horriblesubs.sher.data.mal.api.model.common.date.Dated
import info.horriblesubs.sher.data.mal.api.model.common.page.MediaPage
import info.horriblesubs.sher.data.mal.api.model.main.related.Related

class AnimePage (
    titleSynonyms: ArrayList<String>? = null,
    genres: ArrayList<BaseWithType>? = null,
    titleJapanese: String? = null,
    titleEnglish: String? = null,
    background: String? = null,
    synopsis: String? = null,
    imageUrl: String? = null,
    related: Related? = null,
    popularity: Int? = null,
    status: String? = null,
    favorites: Int? = null,
    scoredBy: Int? = null,
    title: String? = null,
    score: Double? = null,
    members: Int? = null,
    type: String? = null,
    url: String? = null,
    malId: Int? = null,
    rank: Int? = null,

    @SerializedName("trailer_url") var trailerUrl: String? = null,
    @SerializedName("episodes") var episodes: Int? = null,
    @SerializedName("source") var source: String? = null,

    @SerializedName("duration") var duration: String? = null,
    @SerializedName("airing") var airing: Boolean? = null,
    @SerializedName("rating") var rating: String? = null,
    @SerializedName("aired") var aired: Dated? = null,

    @SerializedName("broadcast") var broadcast: String? = null,
    @SerializedName("premiered") var premiered: String? = null,

    @SerializedName("licensors") var licensors: ArrayList<BaseWithType>? = null,
    @SerializedName("producers") var producers: ArrayList<BaseWithType>? = null,
    @SerializedName("studios") var studios: ArrayList<BaseWithType>? = null,

    @SerializedName("ending_themes") var endings: ArrayList<String>? = null,
    @SerializedName("opening_themes") var openings: ArrayList<String>? = null
): MediaPage(imageUrl = imageUrl, malId = malId, url = url, titleSynonyms = titleSynonyms,
    titleJapanese = titleJapanese, titleEnglish = titleEnglish, title = title, status = status,
    type = type, popularity = popularity, scoredBy = scoredBy, favorites = favorites,
    members = members, score = score, rank = rank, related = related, synopsis = synopsis,
    background = background, genres = genres)