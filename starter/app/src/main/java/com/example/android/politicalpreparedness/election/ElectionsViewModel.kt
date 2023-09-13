package com.example.android.politicalpreparedness.election

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.repository.ElectionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

//Construct ViewModel and provide election datasource
@HiltViewModel
class ElectionsViewModel @Inject constructor(private val dataSource : ElectionRepository) : ViewModel() {

    //live data val for upcoming elections
    private val _upcomingElections = MutableLiveData<List<Election>>()
    val upcomingElections: LiveData<List<Election>>
        get() = _upcomingElections

    // live data val for saved elections
    val savedElections: LiveData<List<Election>> = dataSource.getSavedLocalElections()

    init {
        getRemoteUpcomingElections()
    }

    private fun getRemoteUpcomingElections(){
        viewModelScope.launch {
            val response =dataSource.getRemoteUpcomingElections()
            if (response.isSuccessful){
                _upcomingElections.value=response.body()?.elections
            }else{
                Timber.e("getRemoteUpcomingElections: " + response.errorBody()?.string())
            }

        }



    }

}