package com.example.akat2.smack.controller

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.akat2.smack.R
import com.example.akat2.smack.services.AuthService
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loginProgressSpinner.visibility = View.INVISIBLE
    }

    fun loginLoginBtnClicked(view:View) {
        enableProgressSpinner(true)
        hideKeyboard()
        val email = loginEmailText.text.toString()
        val password = loginPasswordText.text.toString()

        if(email.isNotEmpty() && password.isNotEmpty()) {
            AuthService.loginUser(this, email, password) { loginSuccess ->
                if (loginSuccess) {
                    AuthService.findUserByEmail(this) { findSuccess ->
                        if (findSuccess) {
                            enableProgressSpinner(false)
                            finish()
                        } else {
                            enableProgressSpinner(false)
                            errorToast()
                        }
                    }
                } else {
                    enableProgressSpinner(false)
                    errorToast()
                }
            }
        }else {
            enableProgressSpinner(false)
            Toast.makeText(this, "Please make sure both email and password are filled in", Toast.LENGTH_LONG).show()
        }
    }

    fun loginCreateUserBtnClicked(view: View) {
        val createUserIntent = Intent(this, CreateUserActivity::class.java)
        startActivity(createUserIntent)
        finish()
    }

    fun errorToast() {
        Toast.makeText(this, "Unable to login user, please try again", Toast.LENGTH_LONG).show()
    }

    fun enableProgressSpinner(enable: Boolean) {
        if(enable) {
            loginProgressSpinner.visibility = View.VISIBLE
        }else {
            loginProgressSpinner.visibility = View.INVISIBLE
        }
        loginLoginBtn.isEnabled = !enable
        loginCreateUserBtn.isEnabled = !enable
    }

    fun hideKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if(inputManager.isAcceptingText) {
            inputManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
        }
    }
}
