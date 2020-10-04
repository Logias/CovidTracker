package com.example.covidtracker.covid_data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.covidtracker.covid_data.model.USADataModel
import com.example.covidtracker.covid_data.model.USADataResponse

@Database(entities = [USADataModel::class], version = 1, exportSchema = false)
abstract class USADatabase : RoomDatabase() {

    abstract val usaDao: USADao

    companion object {
        @Volatile
        private var INSTANCE: USADatabase? = null

        /**
         * Get existing database, otherwise create new database
         */
        fun getInstance(context: Context): USADatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        USADatabase::class.java,
                        "usa_database"
                    ).fallbackToDestructiveMigration().build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}