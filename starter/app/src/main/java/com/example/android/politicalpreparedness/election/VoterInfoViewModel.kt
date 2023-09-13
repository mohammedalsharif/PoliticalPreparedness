package com.example.android.politicalpreparedness.election

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfo
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.network.models.toVoterInfo
import com.example.android.politicalpreparedness.repository.ElectionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VoterInfoViewModel @Inject constructor(private val dataSource: ElectionRepository) :
    ViewModel() {

    //    live data to hold voter info
    private val _voterInfo = MutableLiveData<VoterInfo>()
    val voterInfo: LiveData<VoterInfo> get() = _voterInfo
    private val _location = MutableLiveData<String>()
    val location: LiveData<String>
        get() = _location

    private val _isSaveElection = MutableLiveData<Election>()
    val savedElection: LiveData<Election>
        get() = _isSaveElection


    private val _openUrl = MutableLiveData<String>()
    val openUrl: LiveData<String>
        get() = _openUrl


    private var electionId = 0
    var electionIsInLocal=false
    var election:Election?=null

    fun getVoterInfoResponse(location: String, electionId: Int) {
        viewModelScope.launch {
            val response = dataSource.getVoterInfo(location, electionId)
            if (response.isSuccessful) {
                _voterInfo.value = response.body()?.toVoterInfo()
            } else {
                Log.e("", "getVoterInfoResponse:${response.errorBody()?.string()} ")
            }

        }

    }

    fun checkIfElectionInLocal(electionId: Int) {
        viewModelScope.launch {
            _isSaveElection.value = dataSource.getElectionById(electionId)
        }
    }



    fun openUrl(url: String) {
        _openUrl.value = url
    }

    fun addElectionToLocal(election: Election) {
        viewModelScope.launch {
            dataSource.insertElection(election)
        }
    }

    fun removeElection(election: Election) {
        viewModelScope.launch {
            dataSource.removeElection(election)
        }
    }

    fun setElectionId(id: Int) {
        electionId = id
    }

    fun setLocation(strLocation: String) {
        _location.postValue(strLocation)
    }

    fun saveElection(election : Election){
        viewModelScope.launch {
            dataSource.insertElection(election)
            checkIfElectionInLocal(election.id)
        }
    }
    fun deleteElection(election: Election){
        viewModelScope.launch {
            dataSource.removeElection(election)
            checkIfElectionInLocal(election.id)
        }
    }

    fun onClickButton(election: Election){
        if (electionIsInLocal){
            deleteElection(election)
        }else{
            saveElection(election)
        }
    }


    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */

}