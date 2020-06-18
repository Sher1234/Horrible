package info.horriblesubs.sher.data.mal.api.model.main.top

import com.google.gson.annotations.SerializedName
import info.horriblesubs.sher.data.mal.api.model.common.base.BaseWithType
import info.horriblesubs.sher.data.mal.api.model.main.top.base.TopListingBeing

class TopCharacterItem(
    @SerializedName("animeography") var animes: ArrayList<BaseWithType>? = null,
    @SerializedName("mangaography") var mangas: ArrayList<BaseWithType>? = null,
    nameKanji: String? = null,
    imageUrl: String? = null,
    favorites: Int? = null,
    title: String? = null,
    malId: Int? = null,
    url: String? = null,
    rank: Int? = null
): TopListingBeing(nameKanji, favorites, imageUrl, title, malId, url, rank)