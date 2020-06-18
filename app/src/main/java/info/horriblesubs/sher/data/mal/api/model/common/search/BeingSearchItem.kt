package info.horriblesubs.sher.data.mal.api.model.common.search

import com.google.gson.annotations.SerializedName

open class BeingSearchItem(
    @SerializedName("alternative_names") var alternativeNames: ArrayList<String>? = null,
    @SerializedName("name") var name: String? = null,
    imageUrl: String? = null,
    url: String? = null,
    malId: Int? = null,
): SearchItem(imageUrl = imageUrl, malId = malId, url = url)