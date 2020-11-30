package com.example.covidtracker.repository

import com.example.covidtracker.api.RetrofitInstance
import com.example.covidtracker.db.ArticleDatabase
import retrofit2.Retrofit

//gets data from database reload datasource from api
//have functions that directly query to
class NewsRepository (
    val db: ArticleDatabase
) {
    suspend fun getBreakingNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.searchForNews(searchQuery, pageNumber)


}