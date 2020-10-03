package com.example.covidtracker.db

import androidx.room.TypeConverter
import com.example.covidtracker.models.Source
//Tells Room how to interpret; Converts Source to String and String to Source class
class Converters {
    @TypeConverter  //tells room this is a converter function
    fun fromSource(source: Source): String {    //converts source to string
        return source.name  //returns the name of the source
    }

    @TypeConverter
    fun toSource(name: String): Source {
        return Source(name, name) //returns the source class with its name as the parameters
    }
}