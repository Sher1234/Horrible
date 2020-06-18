package info.horriblesubs.sher.data.mal.api.model.main.pictures

import com.google.gson.annotations.SerializedName

data class Picture (
    @SerializedName("large") var large: String? = null,
    @SerializedName("small") var small: String? = null
)