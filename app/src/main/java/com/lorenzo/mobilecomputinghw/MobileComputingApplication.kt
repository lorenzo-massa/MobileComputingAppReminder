package com.lorenzo.mobilecomputinghw

import android.app.Application
import com.lorenzo.mobilecomputinghw.Graph

/**
 * This application class sets up our dependency [Graph] with a context
 */
class MobileComputingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Graph.provide(this)
    }
}