package com.example.irrigationfrontend

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val nameEditText = findViewById<EditText>(R.id.nameEditText)
        val usernameEditText = findViewById<EditText>(R.id.usernameEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val registerButton = findViewById<Button>(R.id.registerButton)

        registerButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (name.isNotEmpty() && username.isNotEmpty() && password.isNotEmpty()) {
                val api = com.example.irrigationfrontend.instance.RetrofitInstance.retrofit.create(com.example.irrigationfrontend.api.AuthApi::class.java)
                val request = com.example.irrigationfrontend.api.RegisterRequest(name, username, password)
                api.registerUser(request).enqueue(object : retrofit2.Callback<okhttp3.ResponseBody> {
                    override fun onResponse(call: retrofit2.Call<okhttp3.ResponseBody>, response: retrofit2.Response<okhttp3.ResponseBody>) {
                        val responseMessage = try {
                            response.errorBody()?.string() ?: response.body()?.string() ?: 
                            if (response.isSuccessful) "Kayıt başarılı!" else "Kayıt işlemi başarısız oldu"
                        } catch (e: Exception) {
                            "Hata: ${e.message}"
                        }
                        
                        runOnUiThread {
                            android.widget.Toast.makeText(this@RegisterActivity, responseMessage.trim(), android.widget.Toast.LENGTH_LONG).show()
                            if (response.isSuccessful) {
                                finish()
                            }
                        }
                    }
                    override fun onFailure(call: retrofit2.Call<okhttp3.ResponseBody>, t: Throwable) {
                        runOnUiThread {
                            android.widget.Toast.makeText(this@RegisterActivity, "Bağlantı hatası: ${t.message}", android.widget.Toast.LENGTH_LONG).show()
                        }
                    }
                })
            } else {
                android.widget.Toast.makeText(this, "Tüm alanları doldurun", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
    }
}
