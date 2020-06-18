package info.horriblesubs.sher.data.mal.api.model.main.season.archive

import com.google.gson.annotations.SerializedName

data class SeasonArchivePage (
    @SerializedName("archive") val archives: ArrayList<Archive>? = null
)