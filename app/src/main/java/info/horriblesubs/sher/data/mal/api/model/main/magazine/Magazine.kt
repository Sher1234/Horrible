package info.horriblesubs.sher.data.mal.api.model.main.magazine

import com.google.gson.annotations.SerializedName
import info.horriblesubs.sher.data.mal.api.model.common.base.Base
import info.horriblesubs.sher.data.mal.api.model.common.base.BaseWithType

class Magazine(
    url: String? = null,
    malId: Int? = null,

    @SerializedName("image_url") var imageUrl: String? = null,
    @SerializedName("synopsis") var synopsis: String? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("type") var type: String? = null,

    @SerializedName("publishing_start") var publishingStart: String? = null,
    @SerializedName("volumes") var volumes: Int? = null,
    @SerializedName("members") var members: Int? = null,
    @SerializedName("score") var score: Double? = null,

    @SerializedName("genres") var genres: ArrayList<BaseWithType>? = null,
    @SerializedName("authors") var authors: ArrayList<BaseWithType>? = null,
    @SerializedName("serialization") var serializations: ArrayList<String>? = null
): Base(malId, url)