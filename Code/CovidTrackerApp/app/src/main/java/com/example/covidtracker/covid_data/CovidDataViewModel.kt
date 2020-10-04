package com.example.covidtracker.covid_data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.covidtracker.covid_data.model.USADataModel
import com.example.covidtracker.covid_data.repo.CovidDataRepository
import kotlinx.coroutines.launch

class CovidDataViewModel(private val repository: CovidDataRepository) : ViewModel() {

    var usaData : LiveData<USADataModel>? = null

    init {
        getUpdatedData()
        usaData = repository.getUSAData()
    }

    fun getUpdatedData() {
        viewModelScope.launch {
            repository.fetchUSAData()
        }
    }
}