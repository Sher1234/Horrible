package info.horriblesubs.sher.data.mal.api.model.common.search

import com.google.gson.annotations.SerializedName

data class SearchPage<T: SearchItem> (
    @SerializedName("results") var results: ArrayList<T>? = null,
    @SerializedName("last_page") var lastPage: Int? = null
)