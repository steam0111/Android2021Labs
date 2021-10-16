package com.share.art

import android.app.Application
import com.share.art.core.DI

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        DI.init(this)
    }
}