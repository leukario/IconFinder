package com.example.iconfinder.di

import com.example.iconfinder.MainActivity
import com.example.iconfinder.viewModel.MainActivityViewModel
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [NetworkModule::class])
interface Retrocomponent {
    fun inject(mainActivityViewModel: MainActivityViewModel)
}
