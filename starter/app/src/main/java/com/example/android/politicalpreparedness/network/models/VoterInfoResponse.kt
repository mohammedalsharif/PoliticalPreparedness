package com.example.android.politicalpreparedness.network.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class VoterInfoResponse(
    val election: Election,
    val pollingLocations: String? = null,
    val contests: String? = null,
    val state: List<State>? = null,
    val electionElectionOfficials: List<ElectionOfficial>? = null,
)

fun VoterInfoResponse.toVoterInfo(): VoterInfo {
    val electionInfo = this.state?.first()?.electionAdministrationBody
    return VoterInfo(
        name = electionInfo?.name ?: "",
        electionInfoUrl = electionInfo?.electionInfoUrl ?: "",
        votingLocationFinderUrl = electionInfo?.votingLocationFinderUrl ?: "",
        ballotInfoUrl = electionInfo?.ballotInfoUrl ?: "",
        correspondenceAddress = electionInfo?.correspondenceAddress?.toFormattedString() ?: "",
        address = electionInfo?.correspondenceAddress?.state ?: ""

    )
}