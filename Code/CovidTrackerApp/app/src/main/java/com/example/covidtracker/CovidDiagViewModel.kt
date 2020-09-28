package com.example.covidtracker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CovidDiagViewModel : ViewModel() {
    private val mText: MutableLiveData<String> = MutableLiveData()
    val text: LiveData<String>
        get() = mText

    init {
        mText.value = "Do I Have Covid-19?"
    }
}