package com.example.covidtracker.util

//Handles Responses to check for success or error
sealed class Resource <T>(  //defines which classes can inherit this resource class
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T): Resource<T>(data)    //has data and body and inherits the Resource class and pass the data as in the constructor
    class Error<T>(message: String, data: T? = null): Resource<T>(data, message)
    class Loading<T>: Resource<T>() //when we request a response execute the loading state
}