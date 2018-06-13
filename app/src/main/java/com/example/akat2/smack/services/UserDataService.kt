package com.example.akat2.smack.services

import android.graphics.Color
import com.example.akat2.smack.controller.App
import java.util.*

/**
 * Created by Ayush Kataria on 08-06-2018.
 */
object UserDataService {

    var id = ""
    var avatarColor = ""
    var avatarName = ""
    var email = ""
    var name = ""

    fun getAvatarColor(components: String) : Int {

        val strippedColor = components
                .replace("[", "")
                .replace("]", "")
                .replace(",", "")

        var r = 0
        var g = 0
        var b = 0

        val scanner = Scanner(strippedColor)
        if(scanner.hasNext()) {
            r = ((scanner.nextDouble())*255).toInt()
            g = ((scanner.nextDouble())*255).toInt()
            b = ((scanner.nextDouble())*255).toInt()
        }

        return Color.rgb(r, g, b)
    }

    fun logout() {
        id = ""
        avatarColor = ""
        avatarName = ""
        email = ""
        name = ""
        App.prefs.authToken = ""
        App.prefs.userEmail = ""
        App.prefs.isLoggedIn = false
    }
}


