package com.example.irrigationfrontend

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val usernameEditText = findViewById<EditText>(R.id.usernameEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            
            // API call to authUser
            if (username.isNotEmpty() && password.isNotEmpty()) {
                val api = com.example.irrigationfrontend.instance.RetrofitInstance.retrofit.create(com.example.irrigationfrontend.api.AuthApi::class.java)
                val request = com.example.irrigationfrontend.api.LoginRequest(username, password)
                api.authUser(request).enqueue(object : retrofit2.Callback<com.example.irrigationfrontend.api.AuthResponse> {
                    override fun onResponse(call: retrofit2.Call<com.example.irrigationfrontend.api.AuthResponse>, response: retrofit2.Response<com.example.irrigationfrontend.api.AuthResponse>) {
                        if (response.isSuccessful && response.body()?.token != null) {
                            val token = response.body()!!.token
                            val prefs = getSharedPreferences("auth", MODE_PRIVATE)
                            prefs.edit().putString("token", token?.trim()).apply()
                            Toast.makeText(this@LoginActivity, "Login successful!", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@LoginActivity, ControlActivity::class.java))
                        } else {
                            Toast.makeText(this@LoginActivity, "Login failed!", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: retrofit2.Call<com.example.irrigationfrontend.api.AuthResponse>, t: Throwable) {
                        Toast.makeText(this@LoginActivity, "An error occurred!", Toast.LENGTH_SHORT).show()
                    }
                })
                            } else {
                Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show()
            }
        }
        val registerButton = findViewById<Button>(R.id.registerButton)
        registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
