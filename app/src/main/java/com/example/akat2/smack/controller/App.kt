package com.example.akat2.smack.controller

import android.app.Application
import com.example.akat2.smack.utilities.SharedPrefs

/**
 * Created by Ayush Kataria on 13-06-2018.
 */
class App: Application() {

    companion object {
        lateinit var prefs: SharedPrefs
    }

    override fun onCreate() {
        prefs = SharedPrefs(applicationContext)
        super.onCreate()
    }
}