package com.example.covidtracker.models

import com.example.covidtracker.models.Article

data class NewsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)