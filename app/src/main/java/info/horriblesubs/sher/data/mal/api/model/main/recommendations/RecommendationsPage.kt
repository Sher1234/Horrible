package info.horriblesubs.sher.data.mal.api.model.main.recommendations

import com.google.gson.annotations.SerializedName

data class RecommendationsPage(
    @SerializedName("recommendations") var recommendations: ArrayList<Recommendation>? = null
)