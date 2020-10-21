package com.example.covidtracker.covid_data

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.covidtracker.MainActivity
import com.example.covidtracker.covid_data.model.USADataModel
import com.example.covidtracker.covid_data.repo.CovidDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.wait

class CovidDataViewModel(private val repository: CovidDataRepository) : ViewModel() {

    var usaData = repository.getUSAData()

    var updateStatus = MutableLiveData<Boolean?>()

    init {
        getUpdatedData()
    }

    fun getUpdatedData() {
        viewModelScope.launch {
            val success = repository.fetchUSAData()
            updateStatus.value = success
        }
    }
}