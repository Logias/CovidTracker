package com.example.covidtracker.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.covidtracker.models.Article

@Database (     //tells Room that this class is a Database
    entities = [Article::class],
    version = 1
)
@TypeConverters(Converters::class)  //adds the type converters to this database
abstract class ArticleDatabase : RoomDatabase(){

    abstract fun getArticleDAO(): ArticleDao //abstract function that returns an article dao

    companion object {  //create object for database
        private var instance: ArticleDatabase? = null   //create instance of our db and initialize to null
        private val LOCK = Any()    //synchronize the instance to make sure we only have one instance

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {    //executes when we initialize the database object, when instance is null then start synchronize to prevent access from other threads
            instance ?: createDatabase(context).also { instance = it}   //call instance again and check if still null then call the createDatabase function
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
               context.applicationContext,
                ArticleDatabase::class.java,
                "article_db.db"
            ).build()   //create/build the database
    }

}