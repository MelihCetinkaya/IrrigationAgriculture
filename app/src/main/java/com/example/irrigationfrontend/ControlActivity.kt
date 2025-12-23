package com.example.irrigationfrontend

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.irrigationfrontend.databinding.ActivityControlBinding

class ControlActivity : AppCompatActivity() {

    private lateinit var binding: ActivityControlBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityControlBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_control -> switchFragment(ControlFragment())
                R.id.nav_admin -> switchFragment(AdminRoomFragment())
                R.id.nav_profile -> {
                    val prefs = getSharedPreferences("auth", MODE_PRIVATE)
                    val token = prefs.getString("token", null)
                    if (token != null) {
                        val api = com.example.irrigationfrontend.instance.RetrofitInstance.retrofit
                            .create(com.example.irrigationfrontend.api.ValuesApi::class.java)
                        api.enterValuesRoom("Bearer $token").enqueue(object : retrofit2.Callback<okhttp3.ResponseBody> {
                            override fun onResponse(call: retrofit2.Call<okhttp3.ResponseBody>, response: retrofit2.Response<okhttp3.ResponseBody>) {
                                if (response.isSuccessful) {
                                    switchFragment(ValuesFragment())
                                } else {
                                    android.widget.Toast.makeText(this@ControlActivity, "No permission to access values", android.widget.Toast.LENGTH_SHORT).show()
                                    binding.bottomNav.selectedItemId = R.id.nav_control
                                }
                            }

                            override fun onFailure(call: retrofit2.Call<okhttp3.ResponseBody>, t: Throwable) {
                                android.widget.Toast.makeText(this@ControlActivity, "Error: ${t.message}", android.widget.Toast.LENGTH_SHORT).show()
                                binding.bottomNav.selectedItemId = R.id.nav_control
                            }
                        })
                        false // Don't switch fragment until we get a response
                    } else {
                        android.widget.Toast.makeText(this@ControlActivity, "Please login first", android.widget.Toast.LENGTH_SHORT).show()
                        false
                    }
                }
            }
            true
        }
        binding.bottomNav.selectedItemId = R.id.nav_control
    }

    private fun switchFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .commit()
    }
}
