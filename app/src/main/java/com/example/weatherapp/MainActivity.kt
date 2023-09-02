package com.example.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import com.example.weatherapp.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//265e793b838d7ee5233e55055c87eb7f

class MainActivity : AppCompatActivity() {
            private val binding : ActivityMainBinding by lazy {
                ActivityMainBinding.inflate(layoutInflater)
            }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        fetchWeatherData("karachi")
        SearchCity()
    }

    private fun SearchCity() {
        val searchView = binding.searchView
        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(query != null) {
                    fetchWeatherData(query)
                }
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
            return true

            }

        })
    }

    private fun fetchWeatherData(cityName : String) {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .build().create(ApiInterface::class.java)
            val response = retrofit.getWeatherData(cityName,"265e793b838d7ee5233e55055c87eb7f","metric")
            response.enqueue(object : Callback<weatherApp> {
                override fun onResponse(call: Call<weatherApp>, response: Response<weatherApp>){
                    val responseBody = response.body()
                    if(response.isSuccessful && responseBody != null ){
                        val temperature = responseBody.main.temp.toString()
                            val humidity = responseBody.main.humidity
                        val windSpeed = responseBody.wind.speed
                        val sunRise = responseBody.sys.sunrise.toLong()
                        val sunSet = responseBody.sys.sunset.toLong()
                        val seaLevel = responseBody.main.pressure
                        val condition = responseBody.weather.firstOrNull()?.main?:"unknown"
//                        Log.d("TAG","onResponse : $temperature")
                        val maxTemp = responseBody.main.temp_max
                        val minTemp = responseBody.main.temp_min
                    binding.temperature.text="$temperature ℃"
                   binding.wheather.text = condition
                   binding.maxTemp.text = "Max Temp: $maxTemp ℃"
                   binding.minTemp.text = "Min Temp: $minTemp ℃"
                    binding.Humidity.text = "$humidity %"
                    binding.windSpeed.text = "$windSpeed m/s"
                        binding.sunRise.text = "${time(sunRise)}"
                        binding.sunset.text = "${time(sunSet)}"
                        binding.sea.text = "    $seaLevel nPa"
                        binding.condition.text = condition
                        binding.date.text = date()
                            binding.day.text = dayName(System.currentTimeMillis())
                            binding.cityName.text = "$cityName"

                changeImgeAccToWeatherCond(condition)

                    }
                }
                override fun onFailure (call : Call<weatherApp>, t : Throwable) {

                }

            })


    }

    private fun changeImgeAccToWeatherCond(conditions:String) {
        when (conditions) {
            "Clear Sky", "Sunny", "Clear" -> {
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView.setAnimation(R.raw.sun)
            }

            "Partly Clouds", "Clouds", "Overcast", "Mist", "Foggy" -> {
                binding.root.setBackgroundResource(R.drawable.colud_background)
                binding.lottieAnimationView.setAnimation(R.raw.cloud)

            }


            "Light Rain", "Drizzle", "Moderate Rain", "Showers", "Heavy Rain" -> {
                binding.root.setBackgroundResource(R.drawable.rain_background)
                binding.lottieAnimationView.setAnimation(R.raw.rain)
            }

            "Light Snow", "Moderate Snow", "Heavy Snow", "Blizzard" -> {
                binding.root.setBackgroundResource(R.drawable.snow_background)
                binding.lottieAnimationView.setAnimation(R.raw.snow)
            }else ->{
            binding.root.setBackgroundResource(R.drawable.sunny_background)
            binding.lottieAnimationView.setAnimation(R.raw.sun)

        }

        }
        binding.lottieAnimationView.playAnimation()

    }
    private fun date() :String{
        val sdf = SimpleDateFormat("dd MMMM yyyy",Locale.getDefault())
        return sdf.format((Date()))
    }

    private fun time(timestamp: Long) :String{
        val sdf = SimpleDateFormat("HH:mm",Locale.getDefault())
        return sdf.format((Date(timestamp*1000)))
    }
    fun dayName (timestamp: Long) :String {
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format((Date()))
    }
}