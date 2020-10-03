package com.example.covidtracker.api
import com.example.covidtracker.util.Constants.Companion.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
    companion object {
        private val retrofit by lazy {  //initialize once
            val logging = HttpLoggingInterceptor()   //log response for retrofit for debugging
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())     //GsonConverter converts response to Kotlin object
                .client(client)
                .build()
        }

        val api by lazy {
            retrofit.create(NewsAPI::class.java)    //api for our new request
        }
    }
}