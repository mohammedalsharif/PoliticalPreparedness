package com.example.android.politicalpreparedness.representative

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.repository.RepresentativeRepository
import com.example.android.politicalpreparedness.representative.model.Representative
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RepresentativeViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle,
    private val representativeRepository: RepresentativeRepository,
) : ViewModel() {
    val addressLine1 = MutableLiveData<String>()
    val addressLine2 = MutableLiveData<String>()
    val city = MutableLiveData<String>()
    val state = MutableLiveData<String>()
    val zip = MutableLiveData<String>()


    private var _representativesResponse = MutableLiveData<RepresentativeResponse>()
    val representativeResponse: LiveData<RepresentativeResponse> get() = _representativesResponse

    private var _representatives = MutableLiveData<List<Representative>>()
    val representatives: LiveData<List<Representative>>
        get() = _representatives

    private var _userAddress = MutableLiveData<Address>()
    val addressOfUser: LiveData<Address>
        get() = _userAddress


    fun getRepresentative() {
        viewModelScope.launch {
            val response =
                representativeRepository.getRepresentatives(getUserAddress().toFormattedString())
            if (response.isSuccessful) {
                _representativesResponse.value = response.body()
                val offices = _representativesResponse.value!!.offices
                val officials = _representativesResponse.value!!.officials
                _representatives.value =
                    offices.flatMap { office -> office.getRepresentatives(officials) }
                savedStateHandle[DATA_LIST_KEY] =
                    offices.flatMap { office -> office.getRepresentatives(officials) }
            } else {
                _representatives.value = emptyList()
                Timber.e("getRepresentative:" + response.errorBody()?.string() + " ")
            }


        }
    }

    fun getRepresentativeFromState(): LiveData<List<Representative>> {
        return savedStateHandle.getLiveData(DATA_LIST_KEY)
    }

    fun setStateSelected(newState: String) {
        state.value = newState
    }

    fun setAddressOfUser(address: Address) {
        addressLine1.value = address.line1
        addressLine2.value = address.line2 ?: ""
        city.value = address.city
        state.value = address.state
        zip.value = address.zip
    }

    private fun getUserAddress() = Address(
        addressLine1.value.toString(),
        addressLine2.value.toString(),
        city.value.toString(),
        state.value.toString(),
        zip.value.toString()
    )

    companion object {
        const val DATA_LIST_KEY = "representatives"
    }

}
