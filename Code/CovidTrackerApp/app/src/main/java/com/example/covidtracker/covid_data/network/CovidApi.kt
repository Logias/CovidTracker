package com.example.covidtracker.covid_data.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

const val BASE_URL = "https://disease.sh/v3/covid-19/"

object Service {
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(OkHttpClient.Builder().build())
        .addConverterFactory(MoshiConverterFactory.create().asLenient())
        .build()

    val covidApi: CovidDataService = retrofit.create(
        CovidDataService::class.java)
}

