package info.horriblesubs.sher.data.mal.api.model.main.club

import com.google.gson.annotations.SerializedName
import info.horriblesubs.sher.data.mal.api.model.common.base.Base
import info.horriblesubs.sher.data.mal.api.model.common.base.BaseWithType

class ClubPage(
    @SerializedName("character_relations") var characterRelations: ArrayList<BaseWithType>? = null,
    @SerializedName("anime_relations") var animeRelations: ArrayList<BaseWithType>? = null,
    @SerializedName("manga_relations") var mangaRelations: ArrayList<BaseWithType>? = null,
    @SerializedName("staff") var staff: ArrayList<BaseWithType>? = null,
    @SerializedName("pictures_count") var picturesCount: Float? = null,
    @SerializedName("members_count") var membersCount: Float? = null,
    @SerializedName("image_url") var imageUrl: String? = null,
    @SerializedName("category") var category: String? = null,
    @SerializedName("created") var created: String? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("type") var type: String? = null,
    url: String? = null,
    malId: Int? = null,
): Base(malId = malId, url = url)