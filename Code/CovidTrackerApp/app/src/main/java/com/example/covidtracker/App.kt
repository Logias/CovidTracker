package com.example.covidtracker

import android.app.Application

class App : Application() {

    companion object {
        private lateinit var instance: App
        fun getAppContext() = instance
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}