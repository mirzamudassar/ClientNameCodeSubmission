package com.apex.codeassesment

import android.app.Application
import com.apex.codeassesment.di.MainComponent

class RandomUserApplication : Application() {
  lateinit var mainComponent: MainComponent
    private set

  override fun onCreate() {
    super.onCreate()
//    mainComponent = DaggerMainComponent.create()
  }
}