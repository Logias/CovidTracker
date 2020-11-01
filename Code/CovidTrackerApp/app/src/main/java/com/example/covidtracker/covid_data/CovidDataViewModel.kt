package com.example.covidtracker.covid_data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.covidtracker.covid_data.repo.CovidDataRepository
import kotlinx.coroutines.launch


class CovidDataViewModel(private val repository: CovidDataRepository) : ViewModel() {

    var usaData = repository.getUSAData()
    var statesData = repository.getStatesData()

    var updateStatus = MutableLiveData<Boolean?>()

    init {
        getUpdatedData()
    }

    fun getUpdatedData() {
        viewModelScope.launch {
            val success1 = repository.fetchUSAData()
            val success2 = repository.fetchStatesData()
            updateStatus.value = success1 && success2
        }
    }
}