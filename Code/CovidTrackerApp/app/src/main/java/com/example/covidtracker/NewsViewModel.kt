package com.example.covidtracker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.covidtracker.models.NewsResponse
import com.example.covidtracker.repository.NewsRepository
import com.example.covidtracker.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel (val newsRepository: NewsRepository): ViewModel() {
    val breakingNews:MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1

    init {
        getBreakingNews("covid")
    }

    fun getBreakingNews(searchQuery: String) = viewModelScope.launch {
        breakingNews.postValue(Resource.Loading())  //emit loading state and store in live data
        val response = newsRepository.getBreakingNews(searchQuery, breakingNewsPage)
        breakingNews.postValue(handleBreakingNewsResponse(response))
    }

    //emit success state or error state of live data
    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if(response.isSuccessful) {
            response.body()?.let{ resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
    /*private val mText: MutableLiveData<String> = MutableLiveData()
    val text: LiveData<String>
        get() = mText
    init {
        mText.value = "Get News"
    }*/
}