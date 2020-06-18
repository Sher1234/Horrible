package info.horriblesubs.sher.data.mal.api.model.main.pictures

import com.google.gson.annotations.SerializedName

data class PicturesPage(
    @SerializedName("pictures") var pictures: ArrayList<Picture>? = null
)