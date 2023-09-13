package com.example.android.politicalpreparedness.network

import com.example.android.politicalpreparedness.network.models.ElectionResponse
import com.example.android.politicalpreparedness.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


const val BASE_URL = "https://www.googleapis.com/civicinfo/v2/"


interface CivicsApiService {
    @GET("elections")
    suspend fun getElectionResponse():Response<ElectionResponse>

    @GET("voterinfo")
    suspend fun getVoterInfo(
        @Query("address") address: String,
        @Query("electionId") electionId: Int,
    ): Response<VoterInfoResponse>

    @GET("representatives")
    suspend fun getRepresentatives(@Query("address") address: String): Response<RepresentativeResponse>
}


