package com.example.covidtracker.covid_data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.covidtracker.covid_data.repo.CovidDataRepository

class CovidDataViewModelFactory(
    private val repository: CovidDataRepository) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CovidDataViewModel::class.java)) {
            return CovidDataViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}