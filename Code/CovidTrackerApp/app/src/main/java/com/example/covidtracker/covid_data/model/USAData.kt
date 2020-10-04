package com.example.covidtracker.covid_data.model


import com.squareup.moshi.Json

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

data class USADataResponse(
    @field:Json(name = "active") val active: Int,
    @field:Json(name = "cases") val cases: Int,
    @field:Json(name = "country") val country: String,
    @field:Json(name = "countryInfo") val countryInfo: CountryInfo,
    @field:Json(name = "critical") val critical: Int,
    @field:Json(name = "deaths") val deaths: Int,
    @field:Json(name = "population") val population: Int,
    @field:Json(name = "recovered") val recovered: Int,
    @field:Json(name = "todayCases") val todayCases: Int,
    @field:Json(name = "todayDeaths") val todayDeaths: Int,
    @field:Json(name = "todayRecovered") val todayRecovered: Int,
    @field:Json(name = "updated") val updated: Long
)

data class CountryInfo(
    @field:Json(name = "flag") val flag: String
)

@Parcelize
@Entity(tableName = "usa_table")
data class USADataModel(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo val active: Int,
    @ColumnInfo val cases: Int,
    @ColumnInfo val country: String,
    @ColumnInfo val flagUrl: String,
    @ColumnInfo val critical: Int,
    @ColumnInfo val deaths: Int,
    @ColumnInfo val population: Int,
    @ColumnInfo val recovered: Int,
    @ColumnInfo val todayCases: Int,
    @ColumnInfo val todayDeaths: Int,
    @ColumnInfo val todayRecovered: Int,
    @ColumnInfo val updated: Long
) : Parcelable

fun USADataResponse.asUSADataModel(): USADataModel {
    return USADataModel(0, active, cases, country, countryInfo.flag, critical, deaths, population,
        recovered, todayCases, todayDeaths, todayRecovered, updated )
}