package info.horriblesubs.sher.data.mal.api.model.main.top.base

import com.google.gson.annotations.SerializedName
import info.horriblesubs.sher.data.mal.api.model.common.base.Base

open class TopListing(
    @SerializedName("image_url") var imageUrl: String? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("rank") var rank: Int? = null,
    url: String? = null,
    malId: Int? = null,
): Base(malId = malId, url = url)