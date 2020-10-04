package com.example.covidtracker.covid_data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.covidtracker.covid_data.model.USADataModel

@Dao
interface USADao {

    @Query("SELECT * FROM usa_table")
    fun getUSAdata(): LiveData<USADataModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(usaData: USADataModel)
}