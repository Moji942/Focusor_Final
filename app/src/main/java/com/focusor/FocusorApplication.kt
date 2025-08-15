package com.focusor

import android.app.Application
import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.WorkManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class FocusorApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        instance = this
        setupWorkManager()
    }

    private fun setupWorkManager() {
        WorkManager.initialize(
            this,
            Configuration.Builder()
                .setWorkerFactory(workerFactory)
                .build()
        )
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }

    companion object {
        lateinit var instance: FocusorApplication
            private set

        fun getContext(): Context = instance.applicationContext
    }
}