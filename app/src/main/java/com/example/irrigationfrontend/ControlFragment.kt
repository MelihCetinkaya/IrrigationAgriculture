package com.example.irrigationfrontend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.example.irrigationfrontend.api.IrrigationApi
import com.example.irrigationfrontend.databinding.ControlTemperatureBoxBinding
import com.example.irrigationfrontend.databinding.FragmentControlBinding
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

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
            setupIntervalSpinner()
            setupToggleSwitches(token)
            setupAIButton(token)
        }
        return binding.root
    }

    private fun setupButtons(token: String) {
        val api = com.example.irrigationfrontend.instance.RetrofitInstance.retrofit
            .create(com.example.irrigationfrontend.api.IrrigationApi::class.java)

        // Set up irrigation mode buttons
        binding.btnAutoMode.setOnClickListener { setIrrigationMode(api, token, "auto") }
        binding.btnSelfMode.setOnClickListener { setIrrigationMode(api, token, "self") }
        binding.btnHybridMode.setOnClickListener { setIrrigationMode(api, token, "hybrid") }

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
            api.setNotification("Bearer $token", "mail", true, factor).enqueue(commonCallback(true))
        }

        box.buttonMailDeactivate.setOnClickListener {
            api.setNotification("Bearer $token", "mail", false, factor).enqueue(commonCallback(false))
        }

        box.buttonTelegramActivate.setOnClickListener {
            api.setNotification("Bearer $token", "telegram", true, factor).enqueue(commonCallback(true))
        }

        box.buttonTelegramDeactivate.setOnClickListener {
            api.setNotification("Bearer $token", "telegram", false, factor).enqueue(commonCallback(false))
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
    
    private fun setupIntervalSpinner() {
        // Add a prompt as the first item
        val prompt = "Please choose"
        val intervalOptions = arrayOf(prompt, "x24", "x48", "x72")
        
        val adapter = object : ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            intervalOptions
        ) {
            // This prevents the prompt from being selected
            override fun isEnabled(position: Int): Boolean {
                return position != 0
            }
            
            // Set the color of the prompt to gray
            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getDropDownView(position, convertView, parent)
                val textView = view as android.widget.TextView
                if (position == 0) {
                    // Gray out the prompt
                    textView.setTextColor(android.graphics.Color.GRAY)
                } else {
                    // Set normal text color for other items
                    textView.setTextColor(android.graphics.Color.BLACK)
                }
                return view
            }
        }
        
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.intervalSpinner.adapter = adapter
        
        // Set default selection to prompt
        binding.intervalSpinner.setSelection(0, false)
        
        // Handle item selection
        binding.intervalSpinner.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            private var isFirstSelection = true
            
            override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (isFirstSelection) {
                    isFirstSelection = false
                    return
                }
                
                if (position > 0) {
                    val selectedInterval = intervalOptions[position]
                    // You can add your logic here to handle the selected interval
                    // For example: update a variable or make an API call
                }
            }
            
            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {
                // Show warning if nothing is selected
                android.widget.Toast.makeText(
                    requireContext(),
                    "Please choose an interval",
                    android.widget.Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    
    private fun setupToggleSwitches(token: String) {
        val api = com.example.irrigationfrontend.instance.RetrofitInstance.retrofit
            .create(com.example.irrigationfrontend.api.IrrigationApi::class.java)
        
        binding.switchTemperature.setOnCheckedChangeListener { _, isChecked ->
            // Handle temperature switch
            if (isChecked) {
                // Temperature turned on
            } else {
                // Temperature turned off
            }
        }

        binding.switchHumidity.setOnCheckedChangeListener { _, isChecked ->
            // Handle humidity switch
            if (isChecked) {
                // Humidity turned on
            } else {
                // Humidity turned off
            }
        }

        binding.switchSoilMoisture.setOnCheckedChangeListener { _, isChecked ->
            // Handle soil moisture switch
            if (isChecked) {
                // Soil moisture turned on
            } else {
                // Soil moisture turned off
            }
        }

        binding.btnAskAIPlantSuggestions.setOnClickListener {
            // Get switch states (same as Ask AI button)
            val temperatureState = try {
                if (binding.switchTemperature?.isChecked == true) 1 else 0
            } catch (e: Exception) {
                android.util.Log.e("AI_DEBUG", "Error reading temperature switch", e)
                0
            }
            
            val humidityState = try {
                if (binding.switchHumidity?.isChecked == true) 1 else 0
            } catch (e: Exception) {
                android.util.Log.e("AI_DEBUG", "Error reading humidity switch", e)
                0
            }
            
            val soilMoistureState = try {
                if (binding.switchSoilMoisture?.isChecked == true) 1 else 0
            } catch (e: Exception) {
                android.util.Log.e("AI_DEBUG", "Error reading soil moisture switch", e)
                0
            }
            
            val rainState = try {
                if (binding.switchRain?.isChecked == true) 1 else 0
            } catch (e: Exception) {
                android.util.Log.e("AI_DEBUG", "Error reading rain switch", e)
                0
            }
            
            // Get plant name from hyacinth input
            val plantName = binding.hyacinthInput.text.toString().trim()
            
            // Get interval selection and convert to number
            val intervalSelection = binding.intervalSpinner.selectedItem.toString()
            val number = when {
                intervalSelection.contains("24") -> 24
                intervalSelection.contains("48") -> 48
                intervalSelection.contains("72") -> 72
                else -> 24 // default
            }
            
            android.util.Log.d("AI_DEBUG", "Plant name: '$plantName'")
            android.util.Log.d("AI_DEBUG", "Interval selection: $intervalSelection, Number: $number")
            
            // Create JSON object with arr array and number
            val json = JSONObject()
            val selections = JSONArray()
            selections.put(temperatureState)
            selections.put(humidityState)
            selections.put(soilMoistureState)
            selections.put(rainState)
            json.put("arr", selections)
            json.put("number", number)
            
            android.util.Log.d("AI_DEBUG", "Plant suitability JSON: $json")
            
            // Create request body
            val jsonString = json.toString()
            val requestBody = jsonString.toRequestBody("application/json".toMediaType())
            
            // Show loading state
            binding.aiResponse.text = "Sending..."
            
            // Call Plant Suitability API
            api.checkPlantSuitability("Bearer $token", requestBody, plantName.ifEmpty { null }).enqueue(object : retrofit2.Callback<okhttp3.ResponseBody> {
                override fun onResponse(call: retrofit2.Call<okhttp3.ResponseBody>, response: retrofit2.Response<okhttp3.ResponseBody>) {
                    activity?.runOnUiThread {
                        android.util.Log.d("AI_DEBUG", "Plant suitability response code: ${response.code()}")
                        
                        if (response.isSuccessful) {
                            val aiResponse = response.body()?.string() ?: "No response from AI"
                            android.util.Log.d("AI_DEBUG", "Plant suitability response: $aiResponse")
                            binding.aiResponse.text = aiResponse
                        } else {
                            val errorResponse = try {
                                response.errorBody()?.string() ?: "Unknown error"
                            } catch (e: Exception) {
                                "Error parsing response: ${e.message}"
                            }
                            android.util.Log.d("AI_DEBUG", "Plant suitability error: $errorResponse")
                            binding.aiResponse.text = "Error: $errorResponse"
                        }
                    }
                }
                
                override fun onFailure(call: retrofit2.Call<okhttp3.ResponseBody>, t: Throwable) {
                    activity?.runOnUiThread {
                        binding.aiResponse.text = "Network error: ${t.message}"
                    }
                }
            })
        }

        binding.switchRain.setOnCheckedChangeListener { _, isChecked ->
            // Handle rain switch
            if (isChecked) {
                // Rain turned on
            } else {
                // Rain turned off
            }
        }

        // Setup AI Control button
        binding.btnAIControl.setOnClickListener {
            // Get switch states (same as other AI buttons)
            val temperatureState = try {
                if (binding.switchTemperature?.isChecked == true) 1 else 0
            } catch (e: Exception) {
                android.util.Log.e("AI_DEBUG", "Error reading temperature switch", e)
                0
            }
            
            val humidityState = try {
                if (binding.switchHumidity?.isChecked == true) 1 else 0
            } catch (e: Exception) {
                android.util.Log.e("AI_DEBUG", "Error reading humidity switch", e)
                0
            }
            
            val soilMoistureState = try {
                if (binding.switchSoilMoisture?.isChecked == true) 1 else 0
            } catch (e: Exception) {
                android.util.Log.e("AI_DEBUG", "Error reading soil moisture switch", e)
                0
            }
            
            val rainState = try {
                if (binding.switchRain?.isChecked == true) 1 else 0
            } catch (e: Exception) {
                android.util.Log.e("AI_DEBUG", "Error reading rain switch", e)
                0
            }
            
            // Get plant name from hyacinth input
            val plantName = binding.hyacinthInput.text.toString().trim()
            
            // Get interval selection and convert to number
            val intervalSelection = binding.intervalSpinner.selectedItem.toString()
            val number = when {
                intervalSelection.contains("24") -> 24
                intervalSelection.contains("48") -> 48
                intervalSelection.contains("72") -> 72
                else -> 24 // default
            }
            
            android.util.Log.d("AI_DEBUG", "AI Control - Plant name: '$plantName'")
            android.util.Log.d("AI_DEBUG", "AI Control - Interval selection: $intervalSelection, Number: $number")
            
            // Create JSON object with arr array and number
            val json = JSONObject()
            val selections = JSONArray()
            selections.put(temperatureState)
            selections.put(humidityState)
            selections.put(soilMoistureState)
            selections.put(rainState)
            json.put("arr", selections)
            json.put("number", number)
            
            android.util.Log.d("AI_DEBUG", "AI Control JSON: $json")
            
            // Create request body
            val jsonString = json.toString()
            val requestBody = jsonString.toRequestBody("application/json".toMediaType())
            
            // Show loading state
            binding.aiResponse.text = "Managing irrigation with AI..."
            
            // Call AI Control API
            api.manageIrrigation("Bearer $token", requestBody, plantName.ifEmpty { null }).enqueue(object : retrofit2.Callback<okhttp3.ResponseBody> {
                override fun onResponse(call: retrofit2.Call<okhttp3.ResponseBody>, response: retrofit2.Response<okhttp3.ResponseBody>) {
                    activity?.runOnUiThread {
                        android.util.Log.d("AI_DEBUG", "AI Control response code: ${response.code()}")
                        
                        if (response.isSuccessful) {
                            android.util.Log.d("AI_DEBUG", "AI Control response: ${response.body()?.string()}")
                            binding.aiResponse.text = "Changes were made"
                        } else {
                            val errorResponse = try {
                                response.errorBody()?.string() ?: "Unknown error"
                            } catch (e: Exception) {
                                "Error parsing response: ${e.message}"
                            }
                            android.util.Log.d("AI_DEBUG", "AI Control error: $errorResponse")
                            binding.aiResponse.text = if (errorResponse.isNotBlank()) errorResponse else "Changes could not be made."
                        }
                    }
                }
                
                override fun onFailure(call: retrofit2.Call<okhttp3.ResponseBody>, t: Throwable) {
                    activity?.runOnUiThread {
                        binding.aiResponse.text = "Network error: ${t.message}"
                    }
                }
            })
        }
    }
    
    private fun setupAIButton(token: String) {
        val api = com.example.irrigationfrontend.instance.RetrofitInstance.retrofit
            .create(com.example.irrigationfrontend.api.IrrigationApi::class.java)
        
        binding.btnAskAI.setOnClickListener {
            android.util.Log.d("AI_DEBUG", "Ask AI button clicked!")
            
            // Debug: Check if switches are null
            android.util.Log.d("AI_DEBUG", "switchTemperature: ${binding.switchTemperature}")
            android.util.Log.d("AI_DEBUG", "switchHumidity: ${binding.switchHumidity}")
            android.util.Log.d("AI_DEBUG", "switchSoilMoisture: ${binding.switchSoilMoisture}")
            android.util.Log.d("AI_DEBUG", "switchRain: ${binding.switchRain}")
            
            // Get switch states with debug info
            val temperatureState = try {
                if (binding.switchTemperature?.isChecked == true) 1 else 0
            } catch (e: Exception) {
                android.util.Log.e("AI_DEBUG", "Error reading temperature switch", e)
                0
            }
            
            val humidityState = try {
                if (binding.switchHumidity?.isChecked == true) 1 else 0
            } catch (e: Exception) {
                android.util.Log.e("AI_DEBUG", "Error reading humidity switch", e)
                0
            }
            
            val soilMoistureState = try {
                if (binding.switchSoilMoisture?.isChecked == true) 1 else 0
            } catch (e: Exception) {
                android.util.Log.e("AI_DEBUG", "Error reading soil moisture switch", e)
                0
            }
            
            val rainState = try {
                if (binding.switchRain?.isChecked == true) 1 else 0
            } catch (e: Exception) {
                android.util.Log.e("AI_DEBUG", "Error reading rain switch", e)
                0
            }
            
            // Debug: Log switch states
            android.util.Log.d("AI_DEBUG", "Switch states - Temp: $temperatureState, Hum: $humidityState, Soil: $soilMoistureState, Rain: $rainState")
            
            // Get interval selection and convert to number
            val intervalSelection = binding.intervalSpinner.selectedItem.toString()
            val number = when {
                intervalSelection.contains("24") -> 24
                intervalSelection.contains("48") -> 48
                intervalSelection.contains("72") -> 72
                else -> 24 // default
            }
            
            android.util.Log.d("AI_DEBUG", "Interval selection: $intervalSelection, Number: $number")
            
            // Create JSON object with selections array - use "arr" as key
            val json = JSONObject()
            val selections = JSONArray()
            selections.put(temperatureState)
            selections.put(humidityState)
            selections.put(soilMoistureState)
            selections.put(rainState)
            
            // Backend expects "arr" key and "number" in request body
            json.put("arr", selections)
            json.put("number", number)
            
            android.util.Log.d("AI_DEBUG", "Full JSON: $json")
            
            // Debug: Log JSON being sent
            val jsonString = json.toString()
            android.util.Log.d("AI_DEBUG", "JSON being sent: $jsonString")
            
            // Create request body with explicit content type
            val requestBody = jsonString.toRequestBody("application/json".toMediaType())
            android.util.Log.d("AI_DEBUG", "Content type: ${requestBody.contentType()}")
            
            // Show loading state with debug info
            binding.aiResponse.text = "Sending..."
            
            // Call AI API (number is now in request body)
            api.recommendPlants("Bearer $token", requestBody).enqueue(object : retrofit2.Callback<okhttp3.ResponseBody> {
                override fun onResponse(call: retrofit2.Call<okhttp3.ResponseBody>, response: retrofit2.Response<okhttp3.ResponseBody>) {
                    activity?.runOnUiThread {
                        android.util.Log.d("AI_DEBUG", "Response code: ${response.code()}")
                        android.util.Log.d("AI_DEBUG", "Response successful: ${response.isSuccessful}")
                        
                        if (response.isSuccessful) {
                            val aiResponse = response.body()?.string() ?: "No response from AI"
                            android.util.Log.d("AI_DEBUG", "AI Response: $aiResponse")
                            binding.aiResponse.text = aiResponse
                        } else {
                            val errorResponse = try {
                                response.errorBody()?.string() ?: "Unknown error"
                            } catch (e: Exception) {
                                "Error parsing response: ${e.message}"
                            }
                            android.util.Log.d("AI_DEBUG", "Error Response: $errorResponse")
                            binding.aiResponse.text = "Error: $errorResponse"
                        }
                    }
                }
                
                override fun onFailure(call: retrofit2.Call<okhttp3.ResponseBody>, t: Throwable) {
                    activity?.runOnUiThread {
                        binding.aiResponse.text = "Network error: ${t.message}"
                    }
                }
            })
        }
    }

    private fun setIrrigationMode(api: com.example.irrigationfrontend.api.IrrigationApi, token: String, mode: String) {
        api.setIrrigationMode("Bearer $token", mode).enqueue(object : retrofit2.Callback<okhttp3.ResponseBody> {
            override fun onResponse(
                call: retrofit2.Call<okhttp3.ResponseBody>,
                response: retrofit2.Response<okhttp3.ResponseBody>
            ) {
                if (response.isSuccessful) {
                    // Show success message
                    android.widget.Toast.makeText(
                        requireContext(),
                        "Irrigation mode set to $mode",
                        android.widget.Toast.LENGTH_SHORT
                    ).show()
                } else {
                    // Show error message
                    android.widget.Toast.makeText(
                        requireContext(),
                        "Failed to set irrigation mode: ${response.message()}",
                        android.widget.Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: retrofit2.Call<okhttp3.ResponseBody>, t: Throwable) {
                // Show error message
                android.widget.Toast.makeText(
                    requireContext(),
                    "Error: ${t.message}",
                    android.widget.Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
