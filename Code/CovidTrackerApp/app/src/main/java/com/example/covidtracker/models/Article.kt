package com.example.covidtracker.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (   //tells android studio that this class is a table in our Room database
    tableName = "articles"
)

data class Article(
    @PrimaryKey(autoGenerate =  true)
    var id: Int? = null, //unique id for each table, set to null since not every table is going to be stored
    val author: String,
    val content: String,
    val description: String,
    val publishedAt: String,
    val source: Source,
    val title: String,
    val url: String,
    val urlToImage: String
)