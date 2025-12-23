package com.example.irrigationfrontend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.irrigationfrontend.databinding.ControlTemperatureBoxBinding
import com.example.irrigationfrontend.databinding.FragmentControlBinding

class ControlFragment : Fragment() {
    private var _binding: FragmentControlBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentControlBinding.inflate(inflater, container, false)

        // Setup button actions
        val prefs = requireActivity().getSharedPreferences("auth", android.content.Context.MODE_PRIVATE)
        val token = prefs.getString("token", null)

        if (token == null) {
            android.widget.Toast.makeText(requireContext(), "No token found", android.widget.Toast.LENGTH_SHORT).show()
        } else {
            setupButtons(token)
        }
        return binding.root
    }

    private fun setupButtons(token: String) {
        val api = com.example.irrigationfrontend.instance.RetrofitInstance.retrofit
            .create(com.example.irrigationfrontend.api.IrrigationApi::class.java)

        // Bind the temperature box
        bindTemperatureBox(binding.tempBox1, api, token, "temp")
        
        // Bind the humidity box
        bindTemperatureBox(binding.humidityBox, api, token, "hum")
        
        // Bind the soil box
        bindTemperatureBox(binding.soilBox, api, token, "soil")
        
        // Bind the water level box
        bindTemperatureBox(binding.waterLevelBox, api, token, "waterLevel")
        
        // Bind the weather box
        bindTemperatureBox(binding.weatherBox, api, token, "wth")

        // New time send button
        binding.sendTimeButton.setOnClickListener {
            val time = binding.timeInput.text.toString().trim()
            if (time.isNotEmpty()) {
                api.setIrrigationTime("Bearer $token", time).enqueue(object : retrofit2.Callback<okhttp3.ResponseBody> {
                    override fun onResponse(call: retrofit2.Call<okhttp3.ResponseBody>, response: retrofit2.Response<okhttp3.ResponseBody>) {
                        val responseMessage = try {
                            response.errorBody()?.string() ?: response.body()?.string() ?: 
                            if (response.isSuccessful) "Time sent successfully" else "Failed to send time"
                        } catch (e: Exception) {
                            "Error: ${e.message}"
                        }
                        
                        activity?.runOnUiThread {
                            android.widget.Toast.makeText(requireContext(), responseMessage.trim(), android.widget.Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: retrofit2.Call<okhttp3.ResponseBody>, t: Throwable) {
                        val errorMessage = "Error: ${t.message}"
                        activity?.runOnUiThread {
                            android.widget.Toast.makeText(requireContext(), errorMessage, android.widget.Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            } else {
                android.widget.Toast.makeText(requireContext(), "Please enter a time value", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun bindTemperatureBox(
        box: ControlTemperatureBoxBinding,
        api: com.example.irrigationfrontend.api.IrrigationApi,
        token: String,
        factor: String
    ) {
        // Update the title based on the factor
        when (factor) {
            "hum" -> box.temperatureLabel.text = "Humidity"
            "soil" -> box.temperatureLabel.text = "Soil Moisture"
            "waterLevel" -> box.temperatureLabel.text = "Water Level"
            "wth" -> box.temperatureLabel.text = "Weather"
            else -> box.temperatureLabel.text = "Temperature"
        }
        
        box.activateButton.setOnClickListener {
            api.setIrrigationStatus("Bearer $token", true, factor).enqueue(commonCallback(true))
        }

        box.deactivateButton.setOnClickListener {
            api.setIrrigationStatus("Bearer $token", false, factor).enqueue(commonCallback(false))
        }

        box.buttonUpdateMinThreshold.setOnClickListener {
            val threshold = box.editMinThreshold.text.toString().trim()
            if (threshold.isNotEmpty()) {
                api.setThreshold("Bearer $token", factor, threshold, "min").enqueue(object : retrofit2.Callback<okhttp3.ResponseBody> {
                    override fun onResponse(call: retrofit2.Call<okhttp3.ResponseBody>, response: retrofit2.Response<okhttp3.ResponseBody>) {
                        val responseMessage = try {
                            response.errorBody()?.string() ?: response.body()?.string() ?: 
                            if (response.isSuccessful) "Threshold updated successfully" else "Failed to update threshold"
                        } catch (e: Exception) {
                            "Error: ${e.message}"
                        }

                        activity?.runOnUiThread {
                            android.widget.Toast.makeText(requireContext(), responseMessage.trim(), android.widget.Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: retrofit2.Call<okhttp3.ResponseBody>, t: Throwable) {
                        val errorMessage = "Error: ${t.message}"
                        activity?.runOnUiThread {
                            android.widget.Toast.makeText(requireContext(), errorMessage, android.widget.Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            } else {
                android.widget.Toast.makeText(requireContext(), "Please enter a min value", android.widget.Toast.LENGTH_SHORT).show()
            }
        }

        box.buttonUpdateMaxThreshold.setOnClickListener {
            val threshold = box.editMaxThreshold.text.toString().trim()
            if (threshold.isNotEmpty()) {
                api.setThreshold("Bearer $token", factor, threshold, "max").enqueue(object : retrofit2.Callback<okhttp3.ResponseBody> {
                    override fun onResponse(call: retrofit2.Call<okhttp3.ResponseBody>, response: retrofit2.Response<okhttp3.ResponseBody>) {
                        val responseMessage = try {
                            response.errorBody()?.string() ?: response.body()?.string() ?: 
                            if (response.isSuccessful) "Threshold updated successfully" else "Failed to update threshold"
                        } catch (e: Exception) {
                            "Error: ${e.message}"
                        }

                        activity?.runOnUiThread {
                            android.widget.Toast.makeText(requireContext(), responseMessage.trim(), android.widget.Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: retrofit2.Call<okhttp3.ResponseBody>, t: Throwable) {
                        val errorMessage = "Error: ${t.message}"
                        activity?.runOnUiThread {
                            android.widget.Toast.makeText(requireContext(), errorMessage, android.widget.Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            } else {
                android.widget.Toast.makeText(requireContext(), "Please enter a max value", android.widget.Toast.LENGTH_SHORT).show()
            }
        }

        box.buttonUpdateMinTime.setOnClickListener {
            val timeText = box.editMinTime.text.toString().trim()
            if (timeText.isNotEmpty()) {
                val timeInt = timeText.toIntOrNull()
                if (timeInt != null) {
                    api.setMinmaxTime("Bearer $token", "min", factor, timeInt).enqueue(object : retrofit2.Callback<okhttp3.ResponseBody> {
                        override fun onResponse(call: retrofit2.Call<okhttp3.ResponseBody>, response: retrofit2.Response<okhttp3.ResponseBody>) {
                            val responseMessage = try {
                                response.errorBody()?.string() ?: response.body()?.string() ?: 
                                if (response.isSuccessful) "Time updated successfully" else "Failed to update time"
                            } catch (e: Exception) {
                                "Error: ${e.message}"
                            }

                            activity?.runOnUiThread {
                                android.widget.Toast.makeText(requireContext(), responseMessage.trim(), android.widget.Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: retrofit2.Call<okhttp3.ResponseBody>, t: Throwable) {
                            val errorMessage = "Error: ${t.message}"
                            activity?.runOnUiThread {
                                android.widget.Toast.makeText(requireContext(), errorMessage, android.widget.Toast.LENGTH_SHORT).show()
                            }
                        }
                    })
                } else {
                    android.widget.Toast.makeText(requireContext(), "Please enter a valid time", android.widget.Toast.LENGTH_SHORT).show()
                }
            } else {
                android.widget.Toast.makeText(requireContext(), "Please enter a min time", android.widget.Toast.LENGTH_SHORT).show()
            }
        }

        box.buttonUpdateMaxTime.setOnClickListener {
            val timeText = box.editMaxTime.text.toString().trim()
            if (timeText.isNotEmpty()) {
                val timeInt = timeText.toIntOrNull()
                if (timeInt != null) {
                    api.setMinmaxTime("Bearer $token", "max", factor, timeInt).enqueue(object : retrofit2.Callback<okhttp3.ResponseBody> {
                        override fun onResponse(call: retrofit2.Call<okhttp3.ResponseBody>, response: retrofit2.Response<okhttp3.ResponseBody>) {
                            val responseMessage = try {
                                response.errorBody()?.string() ?: response.body()?.string() ?: 
                                if (response.isSuccessful) "Time updated successfully" else "Failed to update time"
                            } catch (e: Exception) {
                                "Error: ${e.message}"
                            }

                            activity?.runOnUiThread {
                                android.widget.Toast.makeText(requireContext(), responseMessage.trim(), android.widget.Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: retrofit2.Call<okhttp3.ResponseBody>, t: Throwable) {
                            val errorMessage = "Error: ${t.message}"
                            activity?.runOnUiThread {
                                android.widget.Toast.makeText(requireContext(), errorMessage, android.widget.Toast.LENGTH_SHORT).show()
                            }
                        }
                    })
                } else {
                    android.widget.Toast.makeText(requireContext(), "Please enter a valid time", android.widget.Toast.LENGTH_SHORT).show()
                }
            } else {
                android.widget.Toast.makeText(requireContext(), "Please enter a max time", android.widget.Toast.LENGTH_SHORT).show()
            }
        }

        box.buttonUpdateMidTime.setOnClickListener {
            val timeText = box.editMidTime.text.toString().trim()
            if (timeText.isNotEmpty()) {
                val timeInt = timeText.toIntOrNull()
                if (timeInt != null) {
                    api.setMinmaxTime("Bearer $token", "mid", factor, timeInt).enqueue(object : retrofit2.Callback<okhttp3.ResponseBody> {
                        override fun onResponse(call: retrofit2.Call<okhttp3.ResponseBody>, response: retrofit2.Response<okhttp3.ResponseBody>) {
                            val responseMessage = try {
                                response.errorBody()?.string() ?: response.body()?.string() ?: 
                                if (response.isSuccessful) "Time updated successfully" else "Failed to update time"
                            } catch (e: Exception) {
                                "Error: ${e.message}"
                            }

                            activity?.runOnUiThread {
                                android.widget.Toast.makeText(requireContext(), responseMessage.trim(), android.widget.Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: retrofit2.Call<okhttp3.ResponseBody>, t: Throwable) {
                            val errorMessage = "Error: ${t.message}"
                            activity?.runOnUiThread {
                                android.widget.Toast.makeText(requireContext(), errorMessage, android.widget.Toast.LENGTH_SHORT).show()
                            }
                        }
                    })
                } else {
                    android.widget.Toast.makeText(requireContext(), "Please enter a valid time", android.widget.Toast.LENGTH_SHORT).show()
                }
            } else {
                android.widget.Toast.makeText(requireContext(), "Please enter an average time", android.widget.Toast.LENGTH_SHORT).show()
            }
        }

        box.buttonMailActivate.setOnClickListener {
            api.setNotification("Bearer $token", "mail", true).enqueue(commonCallback(true))
        }

        box.buttonMailDeactivate.setOnClickListener {
            api.setNotification("Bearer $token", "mail", false).enqueue(commonCallback(false))
        }

        box.buttonTelegramActivate.setOnClickListener {
            api.setNotification("Bearer $token", "telegram", true).enqueue(commonCallback(true))
        }

        box.buttonTelegramDeactivate.setOnClickListener {
            api.setNotification("Bearer $token", "telegram", false).enqueue(commonCallback(false))
        }
    }

    private fun commonCallback(status: Boolean) = object : retrofit2.Callback<okhttp3.ResponseBody> {
        override fun onResponse(
            call: retrofit2.Call<okhttp3.ResponseBody>,
            response: retrofit2.Response<okhttp3.ResponseBody>
        ) {
            val responseMessage = try {
                response.errorBody()?.string() ?: response.body()?.string() ?: 
                if (response.isSuccessful) {
                    if (status) "Activated successfully" else "Deactivated successfully"
                } else {
                    "Failed to change status"
                }
            } catch (e: Exception) {
                "Error: ${e.message}"
            }
            
            activity?.runOnUiThread {
                android.widget.Toast.makeText(requireContext(), responseMessage.trim(), android.widget.Toast.LENGTH_SHORT).show()
            }
        }

        override fun onFailure(
            call: retrofit2.Call<okhttp3.ResponseBody>,
            t: Throwable
        ) {
            val errorMessage = "Error: ${t.message}"
            activity?.runOnUiThread {
                android.widget.Toast.makeText(requireContext(), errorMessage, android.widget.Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
