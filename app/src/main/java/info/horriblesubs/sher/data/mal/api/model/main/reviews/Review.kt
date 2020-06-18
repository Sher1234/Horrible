package info.horriblesubs.sher.data.mal.api.model.main.reviews

import com.google.gson.annotations.SerializedName

data class Review<T: Scores>(
    @SerializedName("helpful_count") var helpfulCount: Int? = null,
    @SerializedName("reviewer") var reviewer: Reviewer<T>? = null,
    @SerializedName("content") var content: String? = null,
    @SerializedName("date") var date: String? = null,
    @SerializedName("mal_id") var malId: Int? = null,
    @SerializedName("url") var url: String? = null
)