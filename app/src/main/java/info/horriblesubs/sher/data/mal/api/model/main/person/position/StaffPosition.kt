package info.horriblesubs.sher.data.mal.api.model.main.person.position

import com.google.gson.annotations.SerializedName
import info.horriblesubs.sher.api.mal.model.common.base.BaseWithImage

open class StaffPosition (
    @SerializedName("related") var related: BaseWithImage? = null,
    @SerializedName("position") var position: String? = null,
)