package com.example.akat2.smack.controller

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.example.akat2.smack.R
import com.example.akat2.smack.services.AuthService
import com.example.akat2.smack.utilities.BROADCAST_USER_DATA_CHANGE
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

        if(isUserInfoValid()) {

            AuthService.registerUser(email, password) { registerSuccess ->
                if (registerSuccess) {
                    AuthService.loginUser(email, password) { loginSuccess ->
                        if (loginSuccess) {
                            //Successful Login
                            AuthService.createUser(userName, email, userAvatar, avatarColor) { createSuccess ->
                                if (createSuccess) {

                                    val userDataChange = Intent(BROADCAST_USER_DATA_CHANGE)
                                    LocalBroadcastManager.getInstance(this).sendBroadcast(userDataChange)

                                    enableProgressSpinner(false)
                                    finish()
                                }else {
                                    errorToast()
                                    enableProgressSpinner(false)
                                }
                            }
                        }else {
                            errorToast()
                            enableProgressSpinner(false)
                        }
                    }
                }else {
                    errorToast()
                    enableProgressSpinner(false)
                }
            }
        }else {
            enableProgressSpinner(false)
        }
    }

    fun errorToast() {
        Toast.makeText(this, "Unable to create user, please try again", Toast.LENGTH_LONG).show()
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

    fun isUserInfoValid(): Boolean {
        val userName = createUserNameText.text.toString()
        val email = createEmailText.text.toString()
        val password = createPasswordText.text.toString()
        var isValid = true

        if(userName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            isValid = false
            Toast.makeText(this, "Make sure all the fields are filled in.", Toast.LENGTH_LONG).show()
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            isValid = false
            Toast.makeText(this, "Not a valid email", Toast.LENGTH_LONG).show()
        }

        if(password.length < 6) {
            isValid = false
            Toast.makeText(this, "Password length should be more than 6", Toast.LENGTH_LONG).show()
        }

        return isValid
    }
}
