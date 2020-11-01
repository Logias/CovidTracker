package com.example.covidtracker.covid_data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.covidtracker.covid_data.model.StateDataModel

@Database(entities = [StateDataModel::class], version = 1, exportSchema = false)
abstract class StatesDatabase : RoomDatabase() {

    abstract val stateDao: StateDao

    companion object {
        @Volatile
        private var INSTANCE: StatesDatabase? = null

        /**
         * Get existing database, otherwise create new database
         */
        fun getInstance(context: Context): StatesDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        StatesDatabase::class.java,
                        "states_database"
                    ).fallbackToDestructiveMigration().build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}