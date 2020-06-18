package info.horriblesubs.sher.data.mal.api.model.main.reviews

import com.google.gson.annotations.SerializedName

open class Scores (
    @SerializedName("character") var character: Int? = null,
    @SerializedName("enjoyment") var enjoyment: Int? = null,
    @SerializedName("overall") var overall: Int? = null,
    @SerializedName("story") var story: Int? = null,
)