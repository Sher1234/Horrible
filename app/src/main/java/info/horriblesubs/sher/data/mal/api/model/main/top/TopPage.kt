package info.horriblesubs.sher.data.mal.api.model.main.top

import com.google.gson.annotations.SerializedName
import info.horriblesubs.sher.data.mal.api.model.main.top.base.TopListing

data class TopPage<T: TopListing>(
    @SerializedName("top") var topListings: ArrayList<TopListing>? = null
)