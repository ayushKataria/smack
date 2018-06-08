package com.example.akat2.smack.controller

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.example.akat2.smack.R
import com.example.akat2.smack.services.AuthService
import kotlinx.android.synthetic.main.activity_create_user.*
import java.util.*

class CreateUserActivity : AppCompatActivity() {

    var userAvatar = "profileDefault"
    var avatarColor = "[0.5, 0.5, 0.5, 1]"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)
        createProgressSpinner.visibility = View.INVISIBLE
    }

    fun generateUserAvatar(view: View) {
        val random = Random()
        val color = random.nextInt(2)
        val avatar = random.nextInt(28)

        if(color == 0){
            userAvatar = "light$avatar"
        }else {
            userAvatar = "dark$avatar"
        }

        val resourceId = resources.getIdentifier(userAvatar, "drawable", packageName)
        createAvatarImageView.setImageResource(resourceId)
    }

    fun generateColorClicked(view: View){
        val random = Random()
        val r = random.nextInt(256)
        val g = random.nextInt(256)
        val b = random.nextInt(256)

        createAvatarImageView.setBackgroundColor(Color.rgb(r,g,b))

        val savedR = r.toDouble() / 255
        val savedG = g.toDouble() / 255
        val savedB = b.toDouble() / 255

        avatarColor = "[$savedR, $savedG, $savedB, 1]"
        println(avatarColor)
    }

    fun createUserClicked(view: View) {
        enableProgressSpinner(true)
        val userName = createUserNameText.text.toString()
        val email = createEmailText.text.toString()
        val password = createPasswordText.text.toString()

        AuthService.registerUser(this, email, password) { registerSuccess ->
            if(registerSuccess){
                AuthService.loginUser(this, email, password) { loginSuccess ->
                    if(loginSuccess) {
                        //Successful Login
                        AuthService.createUser(this, userName, email, userAvatar, avatarColor) {createSuccess ->
                            if(createSuccess) {
                                val mainIntent = Intent(this, MainActivity::class.java)
                                mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                startActivity(mainIntent)
                                enableProgressSpinner(false)
                                finish()
                            }else {
                                errorToast()
                            }
                        }
                    }else {
                        errorToast()
                    }
                }
            }else {
                errorToast()
            }
        }
    }

    fun errorToast() {
        Toast.makeText(this, "Not able to create user, please try again", Toast.LENGTH_LONG).show()
        enableProgressSpinner(false)
    }

    fun enableProgressSpinner(enable: Boolean) {
        if(enable) {
            createProgressSpinner.visibility = View.VISIBLE
        }else {
            createProgressSpinner.visibility = View.INVISIBLE
        }
        createUserButton.isEnabled = !enable
        createAvatarImageView.isEnabled = !enable
        backGrroundColorButton.isEnabled = !enable
    }
}
