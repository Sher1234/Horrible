package info.horriblesubs.sher.data.mal.api.model.main.reviews

import com.google.gson.annotations.SerializedName
import info.horriblesubs.sher.data.mal.api.model.common.base.Base

class Reviewer<T: Scores>(
    @SerializedName("image_url") var imageUrl: String? = null,
    @SerializedName("username") var username: String? = null,
    @SerializedName("scores") var scores: T? = null,
    @SerializedName("seen") var seen: Int? = null,
    url: String? = null
): Base(null, url)