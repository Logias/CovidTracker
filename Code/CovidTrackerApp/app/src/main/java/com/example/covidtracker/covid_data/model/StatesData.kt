package com.example.covidtracker.covid_data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize


data class StatesDataResponse(
    @field:Json(name = "state") val state: String,
    @field:Json(name = "updated") val updated: Long,
    @field:Json(name = "cases") val cases: Int,
    @field:Json(name = "todayCases") val todayCases: Int,
    @field:Json(name = "deaths") val deaths: Int,
    @field:Json(name = "todayDeaths") val todayDeaths: Int,
    @field:Json(name = "recovered") val recovered: Int,
    @field:Json(name = "active") val active: Int
)

@Parcelize
@Entity(tableName = "states_table")
data class StateDataModel(
    @PrimaryKey
    @ColumnInfo val state: String,
    @ColumnInfo val updated: Long,
    @ColumnInfo val cases: Int,
    @ColumnInfo val todayCases: Int,
    @ColumnInfo val deaths: Int,
    @ColumnInfo val todayDeaths: Int,
    @ColumnInfo val recovered: Int,
    @ColumnInfo val active: Int
) : Parcelable

fun StatesDataResponse.asStateDataModel(): StateDataModel {
    return StateDataModel(state, updated, cases, todayCases, deaths, todayDeaths, recovered, active)
}