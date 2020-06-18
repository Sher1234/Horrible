package info.horriblesubs.sher.data.mal.api.model.main.user.lists.manga

import com.google.gson.annotations.SerializedName

data class UserMangaListPage(
    @SerializedName("manga") var mangas: ArrayList<UserMangaListItem>? = null
)