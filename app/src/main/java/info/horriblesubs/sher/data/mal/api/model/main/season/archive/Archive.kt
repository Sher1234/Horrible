package info.horriblesubs.sher.data.mal.api.model.main.season.archive

import com.google.gson.annotations.SerializedName

data class Archive(
    @SerializedName("seasons") var seasons: ArrayList<String>? = null,
    @SerializedName("year") var year: Int? = null
)