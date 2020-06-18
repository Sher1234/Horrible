package info.horriblesubs.sher.data.mal.api.model.common.page

import com.google.gson.annotations.SerializedName
import info.horriblesubs.sher.data.mal.api.model.common.base.Base
import info.horriblesubs.sher.data.mal.api.model.common.base.BaseWithType
import info.horriblesubs.sher.data.mal.api.model.main.related.Related

open class MediaPage(
    @SerializedName("title_synonyms") var titleSynonyms: ArrayList<String>? = null,
    @SerializedName("title_japanese") var titleJapanese: String? = null,
    @SerializedName("title_english") var titleEnglish: String? = null,
    @SerializedName("image_url") var imageUrl: String? = null,
    @SerializedName("title") var title: String? = null,
    url: String? = null,
    malId: Int? = null,

    @SerializedName("popularity") var popularity: Int? = null,
    @SerializedName("favorites") var favorites: Int? = null,
    @SerializedName("scored_by") var scoredBy: Int? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("members") var members: Int? = null,
    @SerializedName("score") var score: Double? = null,
    @SerializedName("type") var type: String? = null,
    @SerializedName("rank") var rank: Int? = null,

    @SerializedName("related") var related: Related? = null,
    @SerializedName("synopsis") var synopsis: String? = null,
    @SerializedName("background") var background: String? = null,
    @SerializedName("genres") var genres: ArrayList<BaseWithType>? = null
): Base(malId, url)