package com.example.covidtracker.covid_data.repo

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.covidtracker.covid_data.database.StateDao
import com.example.covidtracker.covid_data.database.USADao
import com.example.covidtracker.covid_data.model.StateDataModel
import com.example.covidtracker.covid_data.model.USADataModel
import com.example.covidtracker.covid_data.model.asStateDataModel
import com.example.covidtracker.covid_data.model.asUSADataModel
import com.example.covidtracker.covid_data.network.Service

class CovidDataRepository(private val usaDao: USADao, private val stateDao: StateDao) {

    suspend fun fetchUSAData(): Boolean {

        return try {
            val usaResponse = Service.covidApi.getUSAData()
            val usaData = usaResponse.asUSADataModel()
            usaDao.insert(usaData)
            true
        } catch (error: Exception) {
            Log.e("DEBUG:CovidDataRepository", error.message.toString())
            false
        }
    }

    fun getUSAData(): LiveData<USADataModel> {
        return usaDao.getUSAdata()
    }

    suspend fun fetchStatesData(): Boolean {

        val excludedStates = mutableListOf<String>(
            "Diamond Princess Ship",
            "Wuhan Repatriated",
            "Grand Princess Ship",
            "Navajo Nation",
            "Federal Prisons",
            "Veteran Affairs",
            "US Military",
            "Northern Mariana Islands",
            "United States Virgin Islands",
            "Guam",
            "Puerto Rico",
            "District Of Columbia"
        )

        return try {
            val statesResponse = Service.covidApi.getStatesData()

            for (response in statesResponse) {
                val stateData = response.asStateDataModel()

                if (stateData.state !in excludedStates) {
                    stateDao.insert(stateData)
                }

            }

            true
        } catch (error: Exception) {
            Log.e("DEBUG:CovidDataRepository", error.message.toString())
            false
        }
    }

    fun getStatesData(): LiveData<List<StateDataModel>> {
        return stateDao.getStatesData()
    }

}