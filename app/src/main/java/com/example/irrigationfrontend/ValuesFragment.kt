package com.example.irrigationfrontend

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.irrigationfrontend.api.ValuesApi
import com.example.irrigationfrontend.databinding.FragmentValuesBinding
import com.example.irrigationfrontend.instance.RetrofitInstance
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ValuesFragment : Fragment() {
    private var _binding: FragmentValuesBinding? = null
    private val binding get() = _binding!!

    private val handler = Handler(Looper.getMainLooper())
    private val refreshIntervalMillis = 3000L

    private val refreshRunnable = object : Runnable {
        override fun run() {
            // Fetch current irrigation mode
            fetchCurrentMode()
            
            // Fetch flow time
            fetchFlowTime()
            
            // Temperature (0th table)
            fetchTemperature()
            fetchTemperatureStatus()
            fetchTemperatureMinThreshold()
            fetchTemperatureMinTime()
            fetchTemperatureMaxThreshold()
            fetchTemperatureMaxTime()
            fetchTemperatureMidTime()
            
            // Humidity (1st table)
            fetchHumidity()
            fetchHumidityStatus()
            fetchHumidityMinThreshold()
            fetchHumidityMinTime()
            fetchHumidityMaxThreshold()
            fetchHumidityMaxTime()
            fetchHumidityMidTime()
            
            // Soil Moisture (2nd table)
            fetchSoil()
            fetchSoilStatus()
            fetchSoilMinThreshold()
            fetchSoilMinTime()
            fetchSoilMaxThreshold()
            fetchSoilMaxTime()
            fetchSoilMidTime()
            
            // Water Level (3rd table)
            fetchWaterLevel()
            fetchWaterLevelStatus()
            fetchWaterLevelMinThreshold()
            fetchWaterLevelMinTime()
            fetchWaterLevelMaxThreshold()
            fetchWaterLevelMaxTime()
            fetchWaterLevelMidTime()
            
            // Weather (4th table)
            fetchWeather()
            fetchWeatherStatus()
            fetchWeatherMinThreshold()
            fetchWeatherMinTime()
            fetchWeatherMaxThreshold()
            fetchWeatherMaxTime()
            fetchWeatherMidTime()
            
            // Common notifications
            fetchMailStatus()
            fetchTelegramStatus()
            handler.postDelayed(this, refreshIntervalMillis)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentValuesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up the refresh timer
        setupRefreshTimer()

        // Set up mode button click listener
        binding.orangeBox.setOnClickListener {
            fetchCurrentMode()
        }

        // Initial data fetch
        fetchAllData()
    }

    override fun onResume() {
        super.onResume()
        startRefreshing()
    }

    override fun onPause() {
        super.onPause()
        stopRefreshing()
    }

    private fun setupRefreshTimer() {
        // Remove any existing callbacks
        handler.removeCallbacks(refreshRunnable)
        // Post the first refresh immediately
        handler.post(refreshRunnable)
    }
    
    private fun fetchAllData() {
        // Fetch all data for the first time
        handler.post(refreshRunnable)
    }

    private fun startRefreshing() {
        handler.removeCallbacks(refreshRunnable)
        handler.post(refreshRunnable)
    }

    private fun stopRefreshing() {
        handler.removeCallbacks(refreshRunnable)
    }

    private fun fetchTemperature() {
        val context = context ?: return
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token = prefs.getString("token", null) ?: return

        val api = RetrofitInstance.retrofit.create(ValuesApi::class.java)
        api.getTemperature("Bearer $token", "temp").enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (!isAdded || _binding == null) return

                if (response.isSuccessful) {
                    val bodyString = response.body()?.string() ?: ""
                    binding.textTemperatureValue.text = bodyString
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Hata durumunda şimdilik ekrana yazmıyoruz, istekler devam etsin
            }
        })
    }

    private fun fetchTelegramStatus() {
        val context = context ?: return
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token = prefs.getString("token", null) ?: return

        val api = RetrofitInstance.retrofit.create(ValuesApi::class.java)
        
        // Temperature table telegram status
        api.getNotificationStatus("Bearer $token", "telegram", "temp").enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (!isAdded || _binding == null) return

                if (response.isSuccessful) {
                    val bodyString = response.body()?.string()?.trim() ?: ""
                    val isActive = bodyString.equals("true", ignoreCase = true)

                    if (isActive) {
                        binding.textTelegramStatus.text = "active"
                        binding.imageTelegramStatus.setImageResource(R.drawable.tick)
                    } else {
                        binding.textTelegramStatus.text = "inactive"
                        binding.imageTelegramStatus.setImageResource(R.drawable.wrong)
                    }
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {}
        })
        
        // Humidity table telegram status
        api.getNotificationStatus("Bearer $token", "telegram", "hum").enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (!isAdded || _binding == null) return

                if (response.isSuccessful) {
                    val bodyString = response.body()?.string()?.trim() ?: ""
                    val isActive = bodyString.equals("true", ignoreCase = true)

                    if (isActive) {
                        binding.textHumidityTelegramStatus.text = "active"
                        binding.imageHumidityTelegramStatus.setImageResource(R.drawable.tick)
                    } else {
                        binding.textHumidityTelegramStatus.text = "inactive"
                        binding.imageHumidityTelegramStatus.setImageResource(R.drawable.wrong)
                    }
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {}
        })
        
        // Soil table telegram status
        api.getNotificationStatus("Bearer $token", "telegram", "soil").enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (!isAdded || _binding == null) return

                if (response.isSuccessful) {
                    val bodyString = response.body()?.string()?.trim() ?: ""
                    val isActive = bodyString.equals("true", ignoreCase = true)

                    if (isActive) {
                        binding.textSoilTelegramStatus.text = "active"
                        binding.imageSoilTelegramStatus.setImageResource(R.drawable.tick)
                    } else {
                        binding.textSoilTelegramStatus.text = "inactive"
                        binding.imageSoilTelegramStatus.setImageResource(R.drawable.wrong)
                    }
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {}
        })
        
        // Water Level table telegram status
        api.getNotificationStatus("Bearer $token", "telegram", "waterLevel").enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (!isAdded || _binding == null) return

                if (response.isSuccessful) {
                    val bodyString = response.body()?.string()?.trim() ?: ""
                    val isActive = bodyString.equals("true", ignoreCase = true)

                    if (isActive) {
                        binding.textWaterLevelTelegramStatus.text = "active"
                        binding.imageWaterLevelTelegramStatus.setImageResource(R.drawable.tick)
                    } else {
                        binding.textWaterLevelTelegramStatus.text = "inactive"
                        binding.imageWaterLevelTelegramStatus.setImageResource(R.drawable.wrong)
                    }
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {}
        })
        
        // Weather table telegram status
        api.getNotificationStatus("Bearer $token", "telegram", "wth").enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (!isAdded || _binding == null) return

                if (response.isSuccessful) {
                    val bodyString = response.body()?.string()?.trim() ?: ""
                    val isActive = bodyString.equals("true", ignoreCase = true)

                    if (isActive) {
                        binding.textWeatherTelegramStatus.text = "active"
                        binding.imageWeatherTelegramStatus.setImageResource(R.drawable.tick)
                    } else {
                        binding.textWeatherTelegramStatus.text = "inactive"
                        binding.imageWeatherTelegramStatus.setImageResource(R.drawable.wrong)
                    }
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {}
        })
    }

    private fun fetchMailStatus() {
        val context = context ?: return
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token = prefs.getString("token", null) ?: return

        val api = RetrofitInstance.retrofit.create(ValuesApi::class.java)
        
        // Temperature table mail status
        api.getNotificationStatus("Bearer $token", "mail", "temp").enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (!isAdded || _binding == null) return

                if (response.isSuccessful) {
                    val bodyString = response.body()?.string()?.trim() ?: ""
                    val isActive = bodyString.equals("true", ignoreCase = true)

                    if (isActive) {
                        binding.textMailStatus.text = "active"
                        binding.imageMailStatus.setImageResource(R.drawable.tick)
                    } else {
                        binding.textMailStatus.text = "inactive"
                        binding.imageMailStatus.setImageResource(R.drawable.wrong)
                    }
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {}
        })
        
        // Humidity table mail status
        api.getNotificationStatus("Bearer $token", "mail", "hum").enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (!isAdded || _binding == null) return

                if (response.isSuccessful) {
                    val bodyString = response.body()?.string()?.trim() ?: ""
                    val isActive = bodyString.equals("true", ignoreCase = true)

                    if (isActive) {
                        binding.textHumidityMailStatus.text = "active"
                        binding.imageHumidityMailStatus.setImageResource(R.drawable.tick)
                    } else {
                        binding.textHumidityMailStatus.text = "inactive"
                        binding.imageHumidityMailStatus.setImageResource(R.drawable.wrong)
                    }
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {}
        })
        
        // Soil table mail status
        api.getNotificationStatus("Bearer $token", "mail", "soil").enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (!isAdded || _binding == null) return

                if (response.isSuccessful) {
                    val bodyString = response.body()?.string()?.trim() ?: ""
                    val isActive = bodyString.equals("true", ignoreCase = true)

                    if (isActive) {
                        binding.textSoilMailStatus.text = "active"
                        binding.imageSoilMailStatus.setImageResource(R.drawable.tick)
                    } else {
                        binding.textSoilMailStatus.text = "inactive"
                        binding.imageSoilMailStatus.setImageResource(R.drawable.wrong)
                    }
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {}
        })
        
        // Water Level table mail status
        api.getNotificationStatus("Bearer $token", "mail", "waterLevel").enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (!isAdded || _binding == null) return

                if (response.isSuccessful) {
                    val bodyString = response.body()?.string()?.trim() ?: ""
                    val isActive = bodyString.equals("true", ignoreCase = true)

                    if (isActive) {
                        binding.textWaterLevelMailStatus.text = "active"
                        binding.imageWaterLevelMailStatus.setImageResource(R.drawable.tick)
                    } else {
                        binding.textWaterLevelMailStatus.text = "inactive"
                        binding.imageWaterLevelMailStatus.setImageResource(R.drawable.wrong)
                    }
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {}
        })
        
        // Weather table mail status
        api.getNotificationStatus("Bearer $token", "mail", "wth").enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (!isAdded || _binding == null) return

                if (response.isSuccessful) {
                    val bodyString = response.body()?.string()?.trim() ?: ""
                    val isActive = bodyString.equals("true", ignoreCase = true)

                    if (isActive) {
                        binding.textWeatherMailStatus.text = "active"
                        binding.imageWeatherMailStatus.setImageResource(R.drawable.tick)
                    } else {
                        binding.textWeatherMailStatus.text = "inactive"
                        binding.imageWeatherMailStatus.setImageResource(R.drawable.wrong)
                    }
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {}
        })
    }

    private fun fetchTemperatureMidTime() {
        val context = context ?: return
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token = prefs.getString("token", null) ?: return

        val api = RetrofitInstance.retrofit.create(ValuesApi::class.java)
        api.getMinmaxTime("Bearer $token", "temp", "mid").enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (!isAdded || _binding == null) return

                if (response.isSuccessful) {
                    val bodyString = response.body()?.string()?.trim() ?: ""
                    binding.textTempMidTime.text = if (bodyString.isNotEmpty()) "$bodyString s" else ""
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Hata durumunda şimdilik ekrana yazmıyoruz, istekler devam etsin
            }
        })
    }

    private fun fetchTemperatureMaxThreshold() {
        val context = context ?: return
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token = prefs.getString("token", null) ?: return

        val api = RetrofitInstance.retrofit.create(ValuesApi::class.java)
        api.getThreshold("Bearer $token", "temp", "max").enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (!isAdded || _binding == null) return

                if (response.isSuccessful) {
                    val bodyString = response.body()?.string()?.trim() ?: ""
                    binding.textTempMaxThreshold.text = bodyString
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Hata durumunda şimdilik ekrana yazmıyoruz, istekler devam etsin
            }
        })
    }

    private fun fetchTemperatureMaxTime() {
        val context = context ?: return
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token = prefs.getString("token", null) ?: return

        val api = RetrofitInstance.retrofit.create(ValuesApi::class.java)
        api.getMinmaxTime("Bearer $token", "temp", "max").enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (!isAdded || _binding == null) return

                if (response.isSuccessful) {
                    val bodyString = response.body()?.string()?.trim() ?: ""
                    binding.textTempMaxTime.text = if (bodyString.isNotEmpty()) "$bodyString s" else ""
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Hata durumunda şimdilik ekrana yazmıyoruz, istekler devam etsin
            }
        })
    }

    private fun fetchTemperatureMinTime() {
        val context = context ?: return
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token = prefs.getString("token", null) ?: return

        val api = RetrofitInstance.retrofit.create(ValuesApi::class.java)
        api.getMinmaxTime("Bearer $token", "temp", "min").enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (!isAdded || _binding == null) return

                if (response.isSuccessful) {
                    val bodyString = response.body()?.string()?.trim() ?: ""
                    binding.textTempMinTime.text = if (bodyString.isNotEmpty()) "$bodyString s" else ""
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Hata durumunda şimdilik ekrana yazmıyoruz, istekler devam etsin
            }
        })
    }

    private fun fetchTemperatureMinThreshold() {
        val context = context ?: return
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token = prefs.getString("token", null) ?: return

        val api = RetrofitInstance.retrofit.create(ValuesApi::class.java)
        api.getThreshold("Bearer $token", "temp", "min").enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (!isAdded || _binding == null) return

                if (response.isSuccessful) {
                    val bodyString = response.body()?.string()?.trim() ?: ""
                    binding.textTempMinThreshold.text = bodyString
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Hata durumunda şimdilik ekrana yazmıyoruz, istekler devam etsin
            }
        })
    }

    private fun fetchTemperatureStatus() {
        val context = context ?: return
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token = prefs.getString("token", null) ?: return

        val api = RetrofitInstance.retrofit.create(ValuesApi::class.java)
        api.checkFactor("Bearer $token", "temp").enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (!isAdded || _binding == null) return

                if (response.isSuccessful) {
                    val bodyString = response.body()?.string()?.trim() ?: ""
                    val isActive = bodyString.equals("true", ignoreCase = true)

                    if (isActive) {
                        binding.textTempStatus.text = "active"
                        binding.imageTempStatus.setImageResource(R.drawable.tick)
                    } else {
                        binding.textTempStatus.text = "inactive"
                        binding.imageTempStatus.setImageResource(R.drawable.wrong)
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Hata durumunda şimdilik ekrana yazmıyoruz, istekler devam etsin
            }
        })
    }

    // Humidity fetch methods (1st table)
    private fun fetchHumidity() {
        val context = context ?: return
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token = prefs.getString("token", null) ?: return

        val api = RetrofitInstance.retrofit.create(ValuesApi::class.java)
        api.getTemperature("Bearer $token", "hum").enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (!isAdded || _binding == null) return

                if (response.isSuccessful) {
                    val bodyString = response.body()?.string() ?: ""
                    binding.textHumidityValue.text = bodyString
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Hata durumunda şimdilik ekrana yazmıyoruz, istekler devam etsin
            }
        })
    }

    private fun fetchHumidityStatus() {
        val context = context ?: return
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token = prefs.getString("token", null) ?: return

        val api = RetrofitInstance.retrofit.create(ValuesApi::class.java)
        api.checkFactor("Bearer $token", "hum").enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (!isAdded || _binding == null) return

                if (response.isSuccessful) {
                    val bodyString = response.body()?.string()?.trim() ?: ""
                    val isActive = bodyString.equals("true", ignoreCase = true)

                    if (isActive) {
                        binding.textHumidityStatus.text = "active"
                        binding.imageHumidityStatus.setImageResource(R.drawable.tick)
                    } else {
                        binding.textHumidityStatus.text = "inactive"
                        binding.imageHumidityStatus.setImageResource(R.drawable.wrong)
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Hata durumunda şimdilik ekrana yazmıyoruz, istekler devam etsin
            }
        })
    }

    private fun fetchHumidityMinThreshold() {
        val context = context ?: return
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token = prefs.getString("token", null) ?: return

        val api = RetrofitInstance.retrofit.create(ValuesApi::class.java)
        api.getThreshold("Bearer $token", "hum", "min").enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (!isAdded || _binding == null) return

                if (response.isSuccessful) {
                    val bodyString = response.body()?.string()?.trim() ?: ""
                    binding.textHumidityMinThreshold.text = bodyString
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Hata durumunda şimdilik ekrana yazmıyoruz, istekler devam etsin
            }
        })
    }

    private fun fetchHumidityMaxThreshold() {
        val context = context ?: return
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token = prefs.getString("token", null) ?: return

        val api = RetrofitInstance.retrofit.create(ValuesApi::class.java)
        api.getThreshold("Bearer $token", "hum", "max").enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (!isAdded || _binding == null) return

                if (response.isSuccessful) {
                    val bodyString = response.body()?.string()?.trim() ?: ""
                    binding.textHumidityMaxThreshold.text = bodyString
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Hata durumunda şimdilik ekrana yazmıyoruz, istekler devam etsin
            }
        })
    }

    private fun fetchHumidityMinTime() {
        val context = context ?: return
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token = prefs.getString("token", null) ?: return

        val api = RetrofitInstance.retrofit.create(ValuesApi::class.java)
        api.getMinmaxTime("Bearer $token", "hum", "min").enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (!isAdded || _binding == null) return

                if (response.isSuccessful) {
                    val bodyString = response.body()?.string()?.trim() ?: ""
                    binding.textHumidityMinTime.text = if (bodyString.isNotEmpty()) "$bodyString s" else ""
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Hata durumunda şimdilik ekrana yazmıyoruz, istekler devam etsin
            }
        })
    }

    private fun fetchHumidityMaxTime() {
        val context = context ?: return
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token = prefs.getString("token", null) ?: return

        val api = RetrofitInstance.retrofit.create(ValuesApi::class.java)
        api.getMinmaxTime("Bearer $token", "hum", "max").enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (!isAdded || _binding == null) return

                if (response.isSuccessful) {
                    val bodyString = response.body()?.string()?.trim() ?: ""
                    binding.textHumidityMaxTime.text = if (bodyString.isNotEmpty()) "$bodyString s" else ""
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Hata durumunda şimdilik ekrana yazmıyoruz, istekler devam etsin
            }
        })
    }

    private fun fetchHumidityMidTime() {
        val context = context ?: return
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token = prefs.getString("token", null) ?: return

        val api = RetrofitInstance.retrofit.create(ValuesApi::class.java)
        api.getMinmaxTime("Bearer $token", "hum", "mid").enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (!isAdded || _binding == null) return

                if (response.isSuccessful) {
                    val bodyString = response.body()?.string()?.trim() ?: ""
                    binding.textHumidityMidTime.text = if (bodyString.isNotEmpty()) "$bodyString s" else ""
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Hata durumunda şimdilik ekrana yazmıyoruz, istekler devam etsin
            }
        })
    }

    // Soil Moisture fetch methods (2nd table)
    private fun fetchSoil() {
        val context = context ?: return
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token = prefs.getString("token", null) ?: return

        val api = RetrofitInstance.retrofit.create(ValuesApi::class.java)
        api.getTemperature("Bearer $token", "soil").enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (!isAdded || _binding == null) return

                if (response.isSuccessful) {
                    val bodyString = response.body()?.string() ?: ""
                    binding.textSoilValue.text = bodyString
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Hata durumunda şimdilik ekrana yazmıyoruz, istekler devam etsin
            }
        })
    }

    private fun fetchSoilStatus() {
        val context = context ?: return
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token = prefs.getString("token", null) ?: return

        val api = RetrofitInstance.retrofit.create(ValuesApi::class.java)
        api.checkFactor("Bearer $token", "soil").enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (!isAdded || _binding == null) return

                if (response.isSuccessful) {
                    val bodyString = response.body()?.string()?.trim() ?: ""
                    val isActive = bodyString.equals("true", ignoreCase = true)

                    if (isActive) {
                        binding.textSoilStatus.text = "active"
                        binding.imageSoilStatus.setImageResource(R.drawable.tick)
                    } else {
                        binding.textSoilStatus.text = "inactive"
                        binding.imageSoilStatus.setImageResource(R.drawable.wrong)
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Hata durumunda şimdilik ekrana yazmıyoruz, istekler devam etsin
            }
        })
    }

    private fun fetchSoilMinThreshold() {
        val context = context ?: return
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token = prefs.getString("token", null) ?: return

        val api = RetrofitInstance.retrofit.create(ValuesApi::class.java)
        api.getThreshold("Bearer $token", "soil", "min").enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (!isAdded || _binding == null) return

                if (response.isSuccessful) {
                    val bodyString = response.body()?.string()?.trim() ?: ""
                    binding.textSoilMinThreshold.text = bodyString
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Hata durumunda şimdilik ekrana yazmıyoruz, istekler devam etsin
            }
        })
    }

    private fun fetchSoilMaxThreshold() {
        val context = context ?: return
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token = prefs.getString("token", null) ?: return

        val api = RetrofitInstance.retrofit.create(ValuesApi::class.java)
        api.getThreshold("Bearer $token", "soil", "max").enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (!isAdded || _binding == null) return

                if (response.isSuccessful) {
                    val bodyString = response.body()?.string()?.trim() ?: ""
                    binding.textSoilMaxThreshold.text = bodyString
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Hata durumunda şimdilik ekrana yazmıyoruz, istekler devam etsin
            }
        })
    }

    private fun fetchSoilMinTime() {
        val context = context ?: return
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token = prefs.getString("token", null) ?: return

        val api = RetrofitInstance.retrofit.create(ValuesApi::class.java)
        api.getMinmaxTime("Bearer $token", "soil", "min").enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (!isAdded || _binding == null) return

                if (response.isSuccessful) {
                    val bodyString = response.body()?.string()?.trim() ?: ""
                    binding.textSoilMinTime.text = if (bodyString.isNotEmpty()) "$bodyString s" else ""
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Hata durumunda şimdilik ekrana yazmıyoruz, istekler devam etsin
            }
        })
    }

    private fun fetchSoilMaxTime() {
        val context = context ?: return
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token = prefs.getString("token", null) ?: return

        val api = RetrofitInstance.retrofit.create(ValuesApi::class.java)
        api.getMinmaxTime("Bearer $token", "soil", "max").enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (!isAdded || _binding == null) return

                if (response.isSuccessful) {
                    val bodyString = response.body()?.string()?.trim() ?: ""
                    binding.textSoilMaxTime.text = if (bodyString.isNotEmpty()) "$bodyString s" else ""
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Hata durumunda şimdilik ekrana yazmıyoruz, istekler devam etsin
            }
        })
    }

    private fun fetchSoilMidTime() {
        val context = context ?: return
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token = prefs.getString("token", null) ?: return

        val api = RetrofitInstance.retrofit.create(ValuesApi::class.java)
        api.getMinmaxTime("Bearer $token", "soil", "mid").enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (!isAdded || _binding == null) return

                if (response.isSuccessful) {
                    val bodyString = response.body()?.string()?.trim() ?: ""
                    binding.textSoilMidTime.text = if (bodyString.isNotEmpty()) "$bodyString s" else ""
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Hata durumunda şimdilik ekrana yazmıyoruz, istekler devam etsin
            }
        })
    }

    // Water Level fetch methods (3rd table)
    private fun fetchWaterLevel() {
        val context = context ?: return
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token = prefs.getString("token", null) ?: return

        val api = RetrofitInstance.retrofit.create(ValuesApi::class.java)
        api.getTemperature("Bearer $token", "waterLevel").enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (!isAdded || _binding == null) return

                if (response.isSuccessful) {
                    val bodyString = response.body()?.string() ?: ""
                    binding.textWaterLevelValue.text = bodyString
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Hata durumunda şimdilik ekrana yazmıyoruz, istekler devam etsin
            }
        })
    }

    private fun fetchWaterLevelStatus() {
        val context = context ?: return
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token = prefs.getString("token", null) ?: return

        val api = RetrofitInstance.retrofit.create(ValuesApi::class.java)
        api.checkFactor("Bearer $token", "waterLevel").enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (!isAdded || _binding == null) return

                if (response.isSuccessful) {
                    val bodyString = response.body()?.string()?.trim() ?: ""
                    val isActive = bodyString.equals("true", ignoreCase = true)

                    if (isActive) {
                        binding.textWaterLevelStatus.text = "active"
                        binding.imageWaterLevelStatus.setImageResource(R.drawable.tick)
                    } else {
                        binding.textWaterLevelStatus.text = "inactive"
                        binding.imageWaterLevelStatus.setImageResource(R.drawable.wrong)
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Hata durumunda şimdilik ekrana yazmıyoruz, istekler devam etsin
            }
        })
    }

    private fun fetchWaterLevelMinThreshold() {
        val context = context ?: return
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token = prefs.getString("token", null) ?: return

        val api = RetrofitInstance.retrofit.create(ValuesApi::class.java)
        api.getThreshold("Bearer $token", "waterLevel", "min").enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (!isAdded || _binding == null) return

                if (response.isSuccessful) {
                    val bodyString = response.body()?.string()?.trim() ?: ""
                    binding.textWaterLevelMinThreshold.text = bodyString
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Hata durumunda şimdilik ekrana yazmıyoruz, istekler devam etsin
            }
        })
    }

    private fun fetchWaterLevelMaxThreshold() {
        val context = context ?: return
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token = prefs.getString("token", null) ?: return

        val api = RetrofitInstance.retrofit.create(ValuesApi::class.java)
        api.getThreshold("Bearer $token", "waterLevel", "max").enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (!isAdded || _binding == null) return

                if (response.isSuccessful) {
                    val bodyString = response.body()?.string()?.trim() ?: ""
                    binding.textWaterLevelMaxThreshold.text = bodyString
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Hata durumunda şimdilik ekrana yazmıyoruz, istekler devam etsin
            }
        })
    }

    private fun fetchWaterLevelMinTime() {
        val context = context ?: return
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token = prefs.getString("token", null) ?: return

        val api = RetrofitInstance.retrofit.create(ValuesApi::class.java)
        api.getMinmaxTime("Bearer $token", "waterLevel", "min").enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (!isAdded || _binding == null) return

                if (response.isSuccessful) {
                    val bodyString = response.body()?.string()?.trim() ?: ""
                    binding.textWaterLevelMinTime.text = if (bodyString.isNotEmpty()) "$bodyString s" else ""
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Hata durumunda şimdilik ekrana yazmıyoruz, istekler devam etsin
            }
        })
    }

    private fun fetchWaterLevelMaxTime() {
        val context = context ?: return
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token = prefs.getString("token", null) ?: return

        val api = RetrofitInstance.retrofit.create(ValuesApi::class.java)
        api.getMinmaxTime("Bearer $token", "waterLevel", "max").enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (!isAdded || _binding == null) return

                if (response.isSuccessful) {
                    val bodyString = response.body()?.string()?.trim() ?: ""
                    binding.textWaterLevelMaxTime.text = if (bodyString.isNotEmpty()) "$bodyString s" else ""
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Hata durumunda şimdilik ekrana yazmıyoruz, istekler devam etsin
            }
        })
    }

    private fun fetchWaterLevelMidTime() {
        val context = context ?: return
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token = prefs.getString("token", null) ?: return

        val api = RetrofitInstance.retrofit.create(ValuesApi::class.java)
        api.getMinmaxTime("Bearer $token", "waterLevel", "mid").enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (!isAdded || _binding == null) return

                if (response.isSuccessful) {
                    val bodyString = response.body()?.string()?.trim() ?: ""
                    binding.textWaterLevelMidTime.text = if (bodyString.isNotEmpty()) "$bodyString s" else ""
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Hata durumunda şimdilik ekrana yazmıyoruz, istekler devam etsin
            }
        })
    }

    // Weather fetch methods (4th table)
    private fun fetchWeather() {
        val context = context ?: return
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token = prefs.getString("token", null) ?: return

        val api = RetrofitInstance.retrofit.create(ValuesApi::class.java)
        api.getTemperature("Bearer $token", "wth").enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (!isAdded || _binding == null) return

                if (response.isSuccessful) {
                    val bodyString = response.body()?.string() ?: ""
                    binding.textWeatherValue.text = bodyString
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Hata durumunda şimdilik ekrana yazmıyoruz, istekler devam etsin
            }
        })
    }

    private fun fetchWeatherStatus() {
        val context = context ?: return
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token = prefs.getString("token", null) ?: return

        val api = RetrofitInstance.retrofit.create(ValuesApi::class.java)
        api.checkFactor("Bearer $token", "wth").enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (!isAdded || _binding == null) return

                if (response.isSuccessful) {
                    val bodyString = response.body()?.string()?.trim() ?: ""
                    val isActive = bodyString.equals("true", ignoreCase = true)

                    if (isActive) {
                        binding.textWeatherStatus.text = "active"
                        binding.imageWeatherStatus.setImageResource(R.drawable.tick)
                    } else {
                        binding.textWeatherStatus.text = "inactive"
                        binding.imageWeatherStatus.setImageResource(R.drawable.wrong)
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Hata durumunda şimdilik ekrana yazmıyoruz, istekler devam etsin
            }
        })
    }

    private fun fetchWeatherMinThreshold() {
        val context = context ?: return
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token = prefs.getString("token", null) ?: return

        val api = RetrofitInstance.retrofit.create(ValuesApi::class.java)
        api.getThreshold("Bearer $token", "wth", "min").enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (!isAdded || _binding == null) return

                if (response.isSuccessful) {
                    val bodyString = response.body()?.string()?.trim() ?: ""
                    binding.textWeatherMinThreshold.text = bodyString
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Hata durumunda şimdilik ekrana yazmıyoruz, istekler devam etsin
            }
        })
    }

    private fun fetchWeatherMaxThreshold() {
        val context = context ?: return
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token = prefs.getString("token", null) ?: return

        val api = RetrofitInstance.retrofit.create(ValuesApi::class.java)
        api.getThreshold("Bearer $token", "wth", "max").enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (!isAdded || _binding == null) return

                if (response.isSuccessful) {
                    val bodyString = response.body()?.string()?.trim() ?: ""
                    binding.textWeatherMaxThreshold.text = bodyString
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Hata durumunda şimdilik ekrana yazmıyoruz, istekler devam etsin
            }
        })
    }

    private fun fetchWeatherMinTime() {
        val context = context ?: return
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token = prefs.getString("token", null) ?: return

        val api = RetrofitInstance.retrofit.create(ValuesApi::class.java)
        api.getMinmaxTime("Bearer $token", "wth", "min").enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (!isAdded || _binding == null) return

                if (response.isSuccessful) {
                    val bodyString = response.body()?.string()?.trim() ?: ""
                    binding.textWeatherMinTime.text = if (bodyString.isNotEmpty()) "$bodyString s" else ""
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Hata durumunda şimdilik ekrana yazmıyoruz, istekler devam etsin
            }
        })
    }

    private fun fetchWeatherMaxTime() {
        val context = context ?: return
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token = prefs.getString("token", null) ?: return

        val api = RetrofitInstance.retrofit.create(ValuesApi::class.java)
        api.getMinmaxTime("Bearer $token", "wth", "max").enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (!isAdded || _binding == null) return

                if (response.isSuccessful) {
                    val bodyString = response.body()?.string()?.trim() ?: ""
                    binding.textWeatherMaxTime.text = if (bodyString.isNotEmpty()) "$bodyString s" else ""
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Hata durumunda şimdilik ekrana yazmıyoruz, istekler devam etsin
            }
        })
    }

    private fun fetchWeatherMidTime() {
        val context = context ?: return
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token = prefs.getString("token", null) ?: return

        val api = RetrofitInstance.retrofit.create(ValuesApi::class.java)
        api.getMinmaxTime("Bearer $token", "wth", "mid").enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (!isAdded || _binding == null) return

                if (response.isSuccessful) {
                    val bodyString = response.body()?.string()?.trim() ?: ""
                    binding.textWeatherMidTime.text = if (bodyString.isNotEmpty()) "$bodyString s" else ""
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // Hata durumunda şimdilik ekrana yazmıyoruz, istekler devam etsin
            }
        })
    }

    private fun fetchCurrentMode() {
        val context = context ?: return
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token = prefs.getString("token", null) ?: return

        val api = RetrofitInstance.retrofit.create(ValuesApi::class.java)
        api.getIrrigationMode("Bearer $token").enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (!isAdded || _binding == null) return

                if (response.isSuccessful) {
                    val mode = response.body()?.string()?.trim()?.replace("\"", "") ?: "unknown"
                    val modeText = "${mode.capitalize()} mode is selected"
                    (binding.orangeBox.getChildAt(0) as? android.widget.TextView)?.text = modeText
                } else {
                    (binding.orangeBox.getChildAt(0) as? android.widget.TextView)?.text = "Error loading mode"
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                if (isAdded && _binding != null) {
                    (binding.orangeBox.getChildAt(0) as? android.widget.TextView)?.text = "Error: ${t.message}"
                }
            }
        })
    }

    private fun fetchFlowTime() {
        val context = context ?: return
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token = prefs.getString("token", null) ?: return

        val api = RetrofitInstance.retrofit.create(ValuesApi::class.java)
        api.getFlowTime("Bearer $token").enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (!isAdded || _binding == null) return

                if (response.isSuccessful) {
                    val flowTime = response.body()?.string()?.trim()?.replace("\"", "") ?: "N/A"
                    val flowTimeText = "Flow Time: $flowTime"
                    binding.textFlowTime.text = flowTimeText
                } else {
                    binding.textFlowTime.text = "Error loading flow time"
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                if (isAdded && _binding != null) {
                    binding.textFlowTime.text = "Error: ${t.message}"
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stopRefreshing()
        _binding = null
    }
}
