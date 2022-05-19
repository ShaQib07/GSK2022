package com.shakib.gsk2022.common.di

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import com.shakib.gsk2022.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }
}