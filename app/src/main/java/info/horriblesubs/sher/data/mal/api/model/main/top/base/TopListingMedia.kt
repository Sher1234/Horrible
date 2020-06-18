package info.horriblesubs.sher.data.mal.api.model.main.top.base

import com.google.gson.annotations.SerializedName

open class TopListingMedia(
    @SerializedName("start_date") var startDate: String? = null,
    @SerializedName("end_date") var endDate: String? = null,
    @SerializedName("members") var members: Int? = null,
    @SerializedName("score") var score: Float? = null,
    @SerializedName("type") var type: String? = null,
    imageUrl: String? = null,
    title: String? = null,
    malId: Int? = null,
    url: String? = null,
    rank: Int? = null,
): TopListing(imageUrl = imageUrl, title = title, rank = rank, url = url, malId = malId)