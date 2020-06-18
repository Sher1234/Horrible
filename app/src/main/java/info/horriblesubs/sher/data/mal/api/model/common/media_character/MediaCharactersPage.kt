package info.horriblesubs.sher.data.mal.api.model.common.media_character

import com.google.gson.annotations.SerializedName

open class MediaCharactersPage<T: MediaCharacter> (
    @SerializedName("characters") var characters: ArrayList<T>? = null
)