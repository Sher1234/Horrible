package info.horriblesubs.sher.data.mal.api.model.main.club.members

import com.google.gson.annotations.SerializedName

data class ClubMembersPage (
    @SerializedName("members") var clubMembers: ArrayList<ClubMember>? = null
)
