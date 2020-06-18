package info.horriblesubs.sher.data.mal.api.model.main.club.members

import com.google.gson.annotations.SerializedName
import info.horriblesubs.sher.api.mal.model.common.base.BaseWithImage

class ClubMember(
    @SerializedName("username") var username: String? = null,
    imageUrl: String? = null,
    url: String? = null
): BaseWithImage(imageUrl = imageUrl, name = username, url = url, malId = null)