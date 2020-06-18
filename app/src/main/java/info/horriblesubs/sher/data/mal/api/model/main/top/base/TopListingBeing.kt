package info.horriblesubs.sher.data.mal.api.model.main.top.base

import com.google.gson.annotations.SerializedName

open class TopListingBeing(
    @SerializedName("name_kanji") var nameKanji: String? = null,
    @SerializedName("favorites") var favorites: Int? = null,
    imageUrl: String? = null,
    title: String? = null,
    malId: Int? = null,
    url: String? = null,
    rank: Int? = null,
): TopListing(imageUrl = imageUrl, title = title, rank = rank, url = url, malId = malId)