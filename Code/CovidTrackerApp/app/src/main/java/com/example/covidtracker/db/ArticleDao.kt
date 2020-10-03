package com.example.covidtracker.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.covidtracker.models.Article

//annotate so room knows that this is an interface that defines our functions
@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)    //determines if the article already exists in our database which we will replace if for another article
    suspend fun upsert(article: Article): Long  //return the id that was inserted

    @Query("SELECT * FROM articles")
    fun getAllArticles(): LiveData<List<Article>>   //return live data(android libray) which enables fragments to subscribe to our database and updates the views of our observers

    @Delete
    suspend fun  deleteArticle(article: Article) //deletes the article from our database
}