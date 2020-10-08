package com.example.covidtracker.covid_data.repo

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.covidtracker.covid_data.database.USADao
import com.example.covidtracker.covid_data.model.USADataModel
import com.example.covidtracker.covid_data.model.asUSADataModel
import com.example.covidtracker.covid_data.network.Service

class CovidDataRepository(private val usaDao: USADao) {

    suspend fun fetchUSAData() {

        try {
            val usaResponse = Service.covidApi.getUSAData()
            val usaData = usaResponse.asUSADataModel()
            usaDao.insert(usaData)
        } catch (error: Exception) {
            Log.e("DEBUG:CovidDataRepository", error.message.toString())
        }
    }

    fun getUSAData(): LiveData<USADataModel> {
        return usaDao.getUSAdata()
    }

}