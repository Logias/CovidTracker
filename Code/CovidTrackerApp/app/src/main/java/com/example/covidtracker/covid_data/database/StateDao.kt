package com.example.covidtracker.covid_data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.covidtracker.covid_data.model.StateDataModel

@Dao
interface StateDao {

    @Query("SELECT * FROM states_table")
    fun getStatesData(): LiveData<List<StateDataModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(stateData: StateDataModel)
}