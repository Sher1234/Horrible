package info.horriblesubs.sher.data.mal.api.model.common

import com.google.gson.annotations.SerializedName
import info.horriblesubs.sher.data.mal.api.model.common.base.Base
import info.horriblesubs.sher.data.mal.api.model.common.base.BaseWithType

open class AnimeItem(
    @SerializedName("producers") var producers: ArrayList<BaseWithType>? = null,
    @SerializedName("licensors") var licensors: ArrayList<BaseWithType>? = null,
    @SerializedName("genres") var genres: ArrayList<BaseWithType>? = null,
    @SerializedName("airing_start") var airingStart: String? = null,
    @SerializedName("image_url") var imageUrl: String? = null,
    @SerializedName("synopsis") var synopsis: String? = null,
    @SerializedName("episodes") var episodes: Int? = null,
    @SerializedName("source") var source: String? = null,
    @SerializedName("members") var members: Int? = null,
    @SerializedName("score") var score: Double? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("kids") var kids: Boolean? = null,
    @SerializedName("type") var type: String? = null,
    @SerializedName("r18") var r18: Boolean? = null,
    url: String? = null,
    malId: Int? = null,
): Base(malId, url)