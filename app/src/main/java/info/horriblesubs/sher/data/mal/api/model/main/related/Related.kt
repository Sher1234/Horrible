package info.horriblesubs.sher.data.mal.api.model.main.related

import com.google.gson.annotations.SerializedName
import info.horriblesubs.sher.data.mal.api.model.common.base.BaseWithType as BWT

data class Related(
    @SerializedName("Alternative setting") var alternativeSettings: ArrayList<BWT>? = null,
    @SerializedName("Alternative version") var alternativeVersions: ArrayList<BWT>? = null,
    @SerializedName("Parent story") var parentStories: ArrayList<BWT>? = null,
    @SerializedName("Adaptation") var adaptations: ArrayList<BWT>? = null,
    @SerializedName("Full story") var fullStories: ArrayList<BWT>? = null,
    @SerializedName("Side story") var sideStories: ArrayList<BWT>? = null,
    @SerializedName("Character") var characters: ArrayList<BWT>? = null,
    @SerializedName("Spin-off") var spinOffs: ArrayList<BWT>? = null,
    @SerializedName("Summary") var summaries: ArrayList<BWT>? = null,
    @SerializedName("Prequel") var prequels: ArrayList<BWT>? = null,
    @SerializedName("Sequel") var sequels: ArrayList<BWT>? = null,
    @SerializedName("Other") var others: ArrayList<BWT>? = null
)