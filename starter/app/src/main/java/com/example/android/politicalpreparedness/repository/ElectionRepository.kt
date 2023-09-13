package com.example.android.politicalpreparedness.repository

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.CivicsApiService
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.ElectionResponse
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import retrofit2.Response
import javax.inject.Inject

class ElectionRepository @Inject constructor(private val civicsApiService: CivicsApiService, private val electionDao: ElectionDao) {

    suspend fun getRemoteUpcomingElections(): Response<ElectionResponse> {
        return civicsApiService.getElectionResponse()
    }

     fun getSavedLocalElections(): LiveData<List<Election>> {
        return electionDao.getAllElections()
    }

    suspend fun getVoterInfo(address: String, electionId: Int): Response<VoterInfoResponse> {
        return civicsApiService.getVoterInfo(address, electionId)
    }

    suspend fun removeElection(election: Election) {
        electionDao.deleteElection(election)

    }

    suspend fun insertElection(election: Election) {
        electionDao.insertElection(election)
    }
    suspend fun getElectionById(id:Int): Election {
       return electionDao.getElectionById(id)
    }
}