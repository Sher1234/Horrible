package info.horriblesubs.sher.data.mal.api.model.common.search

import com.google.gson.annotations.SerializedName

open class MediaSearchItem(
    @SerializedName("start_date") var startDate: String? = null,
    @SerializedName("synopsis") var synopsis: String? = null,
    @SerializedName("end_date") var endDate: String? = null,
    @SerializedName("members") var members: Int? = null,
    @SerializedName("score") var score: Double? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("type") var type: String? = null,
    imageUrl: String? = null,
    url: String? = null,
    malId: Int? = null,
): SearchItem(imageUrl = imageUrl, malId = malId, url = url)