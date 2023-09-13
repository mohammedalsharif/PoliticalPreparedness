package com.example.android.politicalpreparedness.repository

import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.CivicsApiService
import com.example.android.politicalpreparedness.network.models.RepresentativeResponse
import retrofit2.Response
import javax.inject.Inject

class RepresentativeRepository @Inject constructor(
    private val civicsApiService: CivicsApiService,
    private val electionDao: ElectionDao,
) {

    suspend fun getRepresentatives(address: String):  Response<RepresentativeResponse> {
        return civicsApiService.getRepresentatives(address)
    }
}