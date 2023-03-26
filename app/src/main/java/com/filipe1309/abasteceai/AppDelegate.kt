package com.filipe1309.abasteceai

import android.app.Application
import com.filipe1309.abasteceai.ui.comparator.ComparatorModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class AppDelegate: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@AppDelegate)
            modules(appModule)
            ComparatorModules.load()

//            modules(appModule, *ComparatorModules.getModules().toTypedArray())
        }
    }
}
