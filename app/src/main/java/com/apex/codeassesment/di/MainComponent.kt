package com.apex.codeassesment.di

import com.apex.codeassesment.ui.main.MainActivityCopy
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [MainModule::class])
interface MainComponent {

    @Component.Factory
    interface Factory {
        fun create(): MainComponent
    }

    interface Injector {
        val mainComponent: MainComponent
    }

    fun inject(mainActivity: MainActivityCopy)
}