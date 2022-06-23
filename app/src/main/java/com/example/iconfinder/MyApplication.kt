package com.example.iconfinder

import android.app.Application
import com.example.iconfinder.di.DaggerRetrocomponent
import com.example.iconfinder.di.NetworkModule
import com.example.iconfinder.di.Retrocomponent

class MyApplication: Application() {
    private lateinit var retrocomponent: Retrocomponent

    override fun onCreate() {
        super.onCreate()
        retrocomponent= DaggerRetrocomponent.builder()
            .networkModule(NetworkModule())
            .build()
    }

    fun getRetroComponent(): Retrocomponent {
        return retrocomponent
    }
}