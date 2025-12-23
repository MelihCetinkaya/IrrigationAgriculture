package com.example.irrigationfrontend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.irrigationfrontend.databinding.FragmentAdminRoomBinding

class AdminRoomFragment : Fragment() {
    private var _binding: FragmentAdminRoomBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminRoomBinding.inflate(inflater, container, false)
        checkPermission()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun checkPermission() {
        val prefs = requireActivity().getSharedPreferences("auth", android.content.Context.MODE_PRIVATE)
        val token = prefs.getString("token", null)
        if (token == null) {
            showNoPermission()
            return
        }
        val api = com.example.irrigationfrontend.instance.RetrofitInstance.retrofit.create(com.example.irrigationfrontend.api.AdminApi::class.java)
        api.enterRoom("Bearer $token").enqueue(object : retrofit2.Callback<okhttp3.ResponseBody> {
            override fun onResponse(call: retrofit2.Call<okhttp3.ResponseBody>, response: retrofit2.Response<okhttp3.ResponseBody>) {
                if (response.isSuccessful) {
                    binding.lockIcon.visibility = View.GONE
                    binding.messageText.visibility = View.GONE
                    binding.usernameField.visibility = View.VISIBLE
                    binding.grantButton.visibility = View.VISIBLE
                    binding.revokeButton.visibility = View.VISIBLE
                    binding.addUserButton.visibility = View.VISIBLE
                    binding.timezoneContainer.visibility = View.VISIBLE
                    binding.changeTzButton.visibility = View.VISIBLE
                    setupButtons(token)
                } else {
                    showNoPermission()
                }
            }

            override fun onFailure(call: retrofit2.Call<okhttp3.ResponseBody>, t: Throwable) {
                showNoPermission()
            }
        })
    }

    private fun setupButtons(token: String) {
        binding.addUserButton.setOnClickListener {
            val user = binding.usernameField.text.toString()
            if (user.isEmpty()) {
                android.widget.Toast.makeText(requireContext(), "Kullanıcı adı girin", android.widget.Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val api = com.example.irrigationfrontend.instance.RetrofitInstance.retrofit.create(com.example.irrigationfrontend.api.AdminApi::class.java)
            api.addUser("Bearer $token", user).enqueue(object : retrofit2.Callback<okhttp3.ResponseBody> {
                override fun onResponse(call: retrofit2.Call<okhttp3.ResponseBody>, response: retrofit2.Response<okhttp3.ResponseBody>) {
                    val responseMessage = try {
                        response.errorBody()?.string() ?: response.body()?.string() ?: 
                        if (response.isSuccessful) "Kullanıcı eklendi" else "İşlem başarısız oldu"
                    } catch (e: Exception) {
                        "Hata: ${e.message}"
                    }
                    android.widget.Toast.makeText(requireContext(), responseMessage.trim(), android.widget.Toast.LENGTH_LONG).show()
                }
                override fun onFailure(call: retrofit2.Call<okhttp3.ResponseBody>, t: Throwable) {
                    android.widget.Toast.makeText(requireContext(), "Bağlantı hatası: ${t.message}", android.widget.Toast.LENGTH_LONG).show()
                }
            })
        }

        binding.grantButton.setOnClickListener {
            sendChangeCommand(token, true)
        }
        binding.changeTzButton.setOnClickListener {
            val tz1 = binding.timezoneField1.text.toString()
            val tz2 = binding.timezoneField2.text.toString()
            val user = binding.usernameField.text.toString()
            if (tz1.isEmpty() || tz2.isEmpty() || user.isEmpty()) {
                android.widget.Toast.makeText(requireContext(), "Tüm alanları doldurun", android.widget.Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val api = com.example.irrigationfrontend.instance.RetrofitInstance.retrofit.create(com.example.irrigationfrontend.api.AdminApi::class.java)
            api.assignScheduler("Bearer $token", user, tz1, tz2).enqueue(object: retrofit2.Callback<okhttp3.ResponseBody> {
                override fun onResponse(call: retrofit2.Call<okhttp3.ResponseBody>, response: retrofit2.Response<okhttp3.ResponseBody>) {
                    val responseMessage = try {
                        response.errorBody()?.string() ?: response.body()?.string() ?: 
                        if (response.isSuccessful) "Zamanlayıcı ayarlandı" else "İşlem başarısız oldu"
                    } catch (e: Exception) {
                        "Hata: ${e.message}"
                    }
                    android.widget.Toast.makeText(requireContext(), responseMessage.trim(), android.widget.Toast.LENGTH_LONG).show()
                }
                override fun onFailure(call: retrofit2.Call<okhttp3.ResponseBody>, t: Throwable) {
                    android.widget.Toast.makeText(requireContext(), "Bağlantı hatası: ${t.message}", android.widget.Toast.LENGTH_LONG).show()
                }
            })
        }

        binding.revokeButton.setOnClickListener {
            sendChangeCommand(token, false)
        }
    }

    private fun sendChangeCommand(token: String, grant: Boolean) {
        val user = binding.usernameField.text.toString()
        if (user.isEmpty()) {
            android.widget.Toast.makeText(requireContext(), "Kullanıcı adı girin", android.widget.Toast.LENGTH_SHORT).show()
            return
        }
        val api = com.example.irrigationfrontend.instance.RetrofitInstance.retrofit.create(com.example.irrigationfrontend.api.AdminApi::class.java)
        api.changeCommand("Bearer $token", user, grant).enqueue(object : retrofit2.Callback<okhttp3.ResponseBody> {
            override fun onResponse(call: retrofit2.Call<okhttp3.ResponseBody>, response: retrofit2.Response<okhttp3.ResponseBody>) {
                val responseMessage = try {
                    response.errorBody()?.string() ?: response.body()?.string() ?: 
                    if (response.isSuccessful) {
                        if (grant) "Yetki verildi" else "Yetki alındı"
                    } else "İşlem başarısız oldu"
                } catch (e: Exception) {
                    "Hata: ${e.message}"
                }
                android.widget.Toast.makeText(requireContext(), responseMessage.trim(), android.widget.Toast.LENGTH_LONG).show()
            }
            override fun onFailure(call: retrofit2.Call<okhttp3.ResponseBody>, t: Throwable) {
                android.widget.Toast.makeText(requireContext(), "Bağlantı hatası: ${t.message}", android.widget.Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun showNoPermission() {
        binding.lockIcon.setImageResource(android.R.drawable.ic_lock_lock)
        binding.messageText.text = "You don't have permission to view this page"

    }
}
