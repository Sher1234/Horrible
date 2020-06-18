package info.horriblesubs.sher.data.mal.api.model.main.recommendations

import com.google.gson.annotations.SerializedName
import info.horriblesubs.sher.data.mal.api.model.common.base.Base

class Recommendation (
    @SerializedName("recommendation_count") var recommendationCount: Int? = null,
    @SerializedName("recommendation_url") var recommendationUrl: String? = null,
    @SerializedName("image_url") var imageUrl: String? = null,
    @SerializedName("title") var title: String? = null,
    url: String? = null,
    malId: Int? = null,
): Base(malId, url)