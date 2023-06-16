package com.dicoding.capstoneproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


class InfoFragment : Fragment() {
    private lateinit var location: String
    private lateinit var temperatureTextView: TextView
    private lateinit var humidityTextView: TextView
    private lateinit var precipitationTextView: TextView
    private lateinit var cloudCoverTextView: TextView
    private lateinit var directNormalIrradianceTextView: TextView

    private lateinit var recyclerView: RecyclerView
    private lateinit var imageAdapter: ImageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        location = arguments?.getString("location") ?: ""

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_info, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        imageAdapter = ImageAdapter()

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = imageAdapter


        temperatureTextView = view.findViewById(R.id.temperatureTextView)
        humidityTextView = view.findViewById(R.id.humidityTextView)
        precipitationTextView = view.findViewById(R.id.precipitationTextView)
        directNormalIrradianceTextView = view.findViewById(R.id.directNormalIrradianceTextView)
        cloudCoverTextView = view.findViewById(R.id.cloudCoverTextView)

        val latitude = arguments?.getDouble("latitude")
        val longitude = arguments?.getDouble("longitude")
        val location = arguments?.getString("location")
        val selectedKabupaten = arguments?.getString("selectedKabupaten")

        val textView = view.findViewById<TextView>(R.id.text_location)


        if (latitude != null && longitude != null) {
            fetchWeatherData(latitude, longitude)
            textView.text = "Kec.$location\nKab.$selectedKabupaten"
        }



        if (selectedKabupaten != null && location != null) {
            fetchImages(selectedKabupaten,location)
        }

        // Update the text view with the location

        return view
    }

    private fun fetchWeatherData(latitude: Double, longitude: Double) {
        // Your code to fetch data from the API and populate the views in the fragment
        val url = "https://api.open-meteo.com/v1/forecast?latitude=$latitude&longitude=$longitude&hourly=temperature_2m,relativehumidity_2m,precipitation_probability,cloudcover,direct_normal_irradiance&forecast_days=16"

        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle the error
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val gson = Gson()
                val weatherResponse = gson.fromJson(body, WeatherResponse::class.java)

                activity?.runOnUiThread {
                    // Update the TextViews with the weather data
                    temperatureTextView.text = weatherResponse.hourly.temperature_2m[0].toString() + "°C"
                    humidityTextView.text = weatherResponse.hourly.relativehumidity_2m[0].toString() + "%"
                    precipitationTextView.text = weatherResponse.hourly.precipitation_probability[0].toString() + "%"
                    directNormalIrradianceTextView.text = weatherResponse.hourly.direct_normal_irradiance[0].toString() + " W/m²"
                    cloudCoverTextView.text = weatherResponse.hourly.cloudcover[0].toString() + "%"
                }
            }
        })
    }
    data class WeatherResponse(val hourly: HourlyData)
    data class HourlyData(val temperature_2m: List<Double>, val relativehumidity_2m: List<Double>, val precipitation_probability: List<Double>,  val direct_normal_irradiance: List<Double>, val cloudcover: List<Double>)

    private fun fetchImages(selectedKabupaten: String, location: String) {
        val url = "https://bangkit-capstone-388003.et.r.appspot.com/predict/$selectedKabupaten/$location"

        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Penanganan ketika permintaan gagal
            }

            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()
                activity?.runOnUiThread {
                    try {
                        val jsonResponse = JSONObject(responseData)
                        val prediction = jsonResponse.getJSONObject("prediction")

                        // Mendapatkan daftar URL gambar
                        val imageUrls = mutableListOf<String>()
                        imageUrls.add(prediction.getString("temperature_2m"))
                        imageUrls.add(prediction.getString("relativehumidity_2m"))
                        imageUrls.add(prediction.getString("apparent_temperature"))
                        imageUrls.add(prediction.getString("precipitation"))
                        imageUrls.add(prediction.getString("rain"))
                        imageUrls.add(prediction.getString("cloudcover"))
                        imageUrls.add(prediction.getString("shortwave_radiation"))
                        imageUrls.add(prediction.getString("direct_radiation"))
                        imageUrls.add(prediction.getString("diffuse_radiation"))
                        imageUrls.add(prediction.getString("direct_normal_irradiance"))

                        // Mengupdate data gambar pada adapter
                        val titles = listOf(
                            "Suhu",
                            "Kelembaban",
                            "Suhu Terasa",
                            "Curah Hujan",
                            "Hujan",
                            "Tutupan Awan",
                            "Radiasi Sinar Matahari",
                            "Radiasi Sinar Langsung",
                            "Radiasi Sinar Tersebar",
                            "Irradian Normal Langsung"
                        )
                        imageAdapter.setImageUrls(imageUrls, titles)

                        // Sembunyikan pesan kesalahan dan tampilkan RecyclerView
//                        textViewError.visibility = View.GONE
//                        progressBar.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                    } catch (e: JSONException) {
                        // Penanganan kesalahan parsing JSON

                    }
                }
            }
        })
    }


    inner class ImageAdapter : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

        private var imageUrls: List<String> = emptyList()
        private var titles: List<String> = emptyList()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.image_item, parent, false)
            return ImageViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
            val imageUrl = imageUrls[position]
            val title = titles[position]
            holder.bindImage(imageUrl,title)
        }

        override fun getItemCount(): Int {
            return imageUrls.size
        }

        fun setImageUrls(urls: List<String>, titles: List<String>) {
            imageUrls = urls
            this.titles = titles
            notifyDataSetChanged()
        }

        inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val imageView: ImageView = itemView.findViewById(R.id.imageView)
            private val textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)

            fun bindImage(imageUrl: String, title: String) {
                Glide.with(itemView)
                    .load(imageUrl)
                    .into(imageView)

                textViewTitle.text = title
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(location: String) =
            InfoFragment().apply {
                arguments = Bundle().apply {
                    putString("location", location)
                }
            }
        fun newInstance(latitude: Double, longitude: Double, location: String, selectedKabupaten: String): InfoFragment {
            val fragment = InfoFragment()
            val args = Bundle()
            args.putDouble("latitude", latitude)
            args.putDouble("longitude", longitude)
            args.putString("location", location)
            args.putString("selectedKabupaten", selectedKabupaten)
            fragment.arguments = args
            return fragment
        }
    }
}