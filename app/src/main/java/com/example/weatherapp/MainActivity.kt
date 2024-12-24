package com.example.weatherapp

import retrofit2.Call
import android.os.Bundle
import retrofit2.Response
import retrofit2.Callback
import android.widget.Button
import android.widget.EditText
import androidx.core.view.ViewCompat
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowInsetsCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: Adapter
    private lateinit var mService: RetrofitServices

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val rView: RecyclerView = findViewById(R.id.rView)
        adapter = Adapter(DiffCallback())
        rView.adapter = adapter
        rView.layoutManager = LinearLayoutManager(this)

        mService = Common.retrofitService

        val etSearch: EditText = findViewById(R.id.et_search)
        val btnSearch: Button = findViewById(R.id.btn_search)

        btnSearch.setOnClickListener {
            val cityName = etSearch.text.toString().trim()

            if (cityName.isNotEmpty()) {
                fetchWeather(cityName)
            }
        }
    }

    private fun fetchWeather(cityName: String) {
        val appid = BuildConfig.OPEN_WEATHER_API_KEY

        mService.getWeatherList(cityName, appid).enqueue(object : Callback<WeatherForecast> {
            override fun onResponse(call: Call<WeatherForecast>, response: Response<WeatherForecast>) {
                if (response.isSuccessful) {
                    val forecast = response.body()
                    adapter.submitList(forecast!!.list)
                }
            }

            override fun onFailure(call: Call<WeatherForecast>, t: Throwable) { }
        })
    }
}