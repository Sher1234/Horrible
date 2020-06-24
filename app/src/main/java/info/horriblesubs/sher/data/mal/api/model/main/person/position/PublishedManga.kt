package info.horriblesubs.sher.data.mal.api.model.main.person.position

import com.google.gson.annotations.SerializedName
import info.horriblesubs.sher.data.mal.api.model.common.base.BaseWithImage

class PublishedManga (
    @SerializedName("manga") var mangaRelated: BaseWithImage? = null,
    position: String? = null
): StaffPosition(position = position, related = mangaRelated)