package info.horriblesubs.sher.data.mal.api.model.main.reviews

import com.google.gson.annotations.SerializedName

data class ReviewPage<T: Scores>(
    @SerializedName("reviews") val reviews: ArrayList<Review<T>>? = null
)