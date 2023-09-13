package com.example.android.politicalpreparedness.network.models

data class VoterInfo(
    val name: String = "",
    val electionInfoUrl: String = "",
    val votingLocationFinderUrl: String = "",
    val ballotInfoUrl: String = "",
    val correspondenceAddress: String = "",
    val address: String
)