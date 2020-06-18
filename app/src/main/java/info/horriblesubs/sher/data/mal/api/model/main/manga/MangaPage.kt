package info.horriblesubs.sher.data.mal.api.model.main.manga

import com.google.gson.annotations.SerializedName
import info.horriblesubs.sher.data.mal.api.model.common.base.BaseWithType
import info.horriblesubs.sher.data.mal.api.model.common.date.Dated
import info.horriblesubs.sher.data.mal.api.model.common.page.MediaPage
import info.horriblesubs.sher.data.mal.api.model.main.related.Related

class MangaPage(
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
    @SerializedName("volumes") var volumes: Int? = null,
    @SerializedName("chapters") var chapters: Int? = null,
    @SerializedName("published") var published: Dated? = null,
    @SerializedName("publishing") var publishing: Boolean? = null,
    @SerializedName("authors") var authors: ArrayList<BaseWithType>? = null,
    @SerializedName("serializations") var serializations: ArrayList<BaseWithType>? = null
): MediaPage(imageUrl = imageUrl, malId = malId, url = url, titleSynonyms = titleSynonyms,
    titleJapanese = titleJapanese, titleEnglish = titleEnglish, title = title, status = status,
    type = type, popularity = popularity, scoredBy = scoredBy, favorites = favorites,
    members = members, score = score, rank = rank, related = related, synopsis = synopsis,
    background = background, genres = genres)