package com.ivarvisser.cineapp

import android.app.Application
import com.ivarvisser.cineapp.di.initKoin
import net.codinux.log.LogLevel
import net.codinux.log.LoggerFactory
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class CineApp : Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidContext(this@CineApp)
            androidLogger()
        }

        net.codinux.log.android.AndroidContext.applicationContext = this.applicationContext

        if (BuildKonfig.IS_DEBUG) {
            LoggerFactory.config.rootLevel = LogLevel.Debug
        }
    }
}
