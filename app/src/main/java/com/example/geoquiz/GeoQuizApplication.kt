package com.example.geoquiz

import android.app.Application
import com.example.geoquiz.data.AppContainer
import com.example.geoquiz.data.DefaultAppContainer

class GeoQuizApplication: Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container=DefaultAppContainer()
    }
}