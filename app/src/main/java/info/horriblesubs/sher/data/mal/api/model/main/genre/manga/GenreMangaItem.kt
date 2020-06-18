package info.horriblesubs.sher.data.mal.api.model.main.genre.manga

import com.google.gson.annotations.SerializedName
import info.horriblesubs.sher.data.mal.api.model.common.base.Base
import info.horriblesubs.sher.data.mal.api.model.common.base.BaseWithType

class GenreMangaItem(
    @SerializedName("serialization") var serializations: List<String>? = null,
    @SerializedName("publishing_start") var publishingStart: String? = null,
    @SerializedName("authors") var authors: List<BaseWithType>? = null,
    @SerializedName("genres") var genres: List<BaseWithType>? = null,
    @SerializedName("image_url") var imageUrl: String? = null,
    @SerializedName("synopsis") var synopsis: String? = null,
    @SerializedName("volumes") var volumes: Int? = null,
    @SerializedName("members") var members: Int? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("type") var type: String? = null,
    @SerializedName("score") var score: Float? = null,
    url: String? = null,
    malId: Int? = null,
): Base(malId = malId, url = url)