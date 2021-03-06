package com.example.covidtracker.covid_data.network

import com.example.covidtracker.covid_data.model.StatesDataResponse
import com.example.covidtracker.covid_data.model.USADataResponse
import retrofit2.http.GET

interface CovidDataService {
    @GET("countries/usa")
    suspend fun getUSAData() : USADataResponse

    @GET("states")
    suspend fun getStatesData() : List<StatesDataResponse>
}

