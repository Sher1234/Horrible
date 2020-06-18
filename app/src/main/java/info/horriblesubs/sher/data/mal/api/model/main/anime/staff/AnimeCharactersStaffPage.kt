package info.horriblesubs.sher.data.mal.api.model.main.anime.staff

import com.google.gson.annotations.SerializedName
import info.horriblesubs.sher.data.mal.api.model.common.media_character.MediaCharactersPage

class AnimeCharactersStaffPage (
    @SerializedName("staff") var staff: ArrayList<AnimeStaff>? = null,
    characters: ArrayList<AnimeCharacter>? = null,
): MediaCharactersPage<AnimeCharacter>(characters)