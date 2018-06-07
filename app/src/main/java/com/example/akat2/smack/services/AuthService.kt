package com.example.akat2.smack.services

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.akat2.smack.utilities.URL_LOGIN
import com.example.akat2.smack.utilities.URL_REGISTER
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by Ayush Kataria on 06-06-2018.
 */
object AuthService {

    var isLogggedIn = false
    var authToken = ""
    var userEmail = ""

    fun registerUser(context: Context, email: String, password: String, complete: (Boolean) -> (Unit)) {

        val jsonBody = JSONObject()
        jsonBody.put("email", email)
        jsonBody.put("password", password)
        val requestBody = jsonBody.toString()

        val registerRequest = object : StringRequest(Method.POST, URL_REGISTER, Response.Listener { response ->
            println(response)
            complete(true)
        }, Response.ErrorListener { error ->
            Log.d("ERROR", "Could not register user: $error")
            Toast.makeText(context, "Registration failed", Toast.LENGTH_SHORT).show()
            complete(false)
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
        }

        Volley.newRequestQueue(context).add(registerRequest)
    }

    fun loginUser(context: Context, email: String, password: String, complete: (Boolean) -> Unit) {

        val jsonBody = JSONObject()
        jsonBody.put("email", email)
        jsonBody.put("password", password)
        val requestBody = jsonBody.toString()

        val loginRequest = object: JsonObjectRequest(Method.POST, URL_LOGIN, null, Response.Listener { response ->
            //Parse JSON
            try {
                userEmail = response.getString("user")
                authToken = response.getString("token")
                isLogggedIn = true
                complete(true)
            } catch(e: JSONException) {
                Log.d("JSON", "EXC: ${e.localizedMessage}")
                complete(false)
            }
        }, Response.ErrorListener { error ->
            //Deal with errors
            Log.d("Error", "Could not login user: $error")
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
        }

        //leads to memory
        //TODO: Have single request queue for whole app
        Volley.newRequestQueue(context).add(loginRequest)
    }

}