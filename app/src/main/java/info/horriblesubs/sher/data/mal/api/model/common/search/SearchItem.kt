package info.horriblesubs.sher.data.mal.api.model.common.search

import com.google.gson.annotations.SerializedName
import info.horriblesubs.sher.data.mal.api.model.common.base.Base

open class SearchItem(
    @SerializedName("image_url") var imageUrl: String? = null,
    url: String? = null,
    malId: Int? = null,
): Base(malId = malId, url = url)