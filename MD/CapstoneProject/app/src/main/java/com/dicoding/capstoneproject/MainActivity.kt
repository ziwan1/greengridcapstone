package com.dicoding.capstoneproject

import android.Manifest
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.location.Address
import android.location.Geocoder
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.PhotoMetadata
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.*
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import javax.net.ssl.HttpsURLConnection


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var currentMarker: Marker? = null
    private lateinit var placesClient: PlacesClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val kabupatenSpinner: Spinner = findViewById(R.id.kabupatenSpinner)
        val kabupaten = arrayOf("Pilih Kab/Kota","Malang", "Surabaya", "Magetan", "Nganjuk")
        val adapter = ArrayAdapter(this, R.layout.spinner_item_layout, kabupaten)
        kabupatenSpinner.adapter = adapter

        val searchButton = findViewById<Button>(R.id.search_button)
        val searchText = findViewById<EditText>(R.id.search_text)


        searchButton.setOnClickListener {
            val selectedKabupaten = kabupatenSpinner.selectedItem.toString()
            val location = searchText.text.toString()
            if (location != "" && selectedKabupaten != "Pilih Kab/Kota") {
                searchLocation(location, selectedKabupaten)
            }
        }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
        mMap.setOnMapClickListener {
            // Hide the fragment container
            hideCardView()
        }
        val kabupatenSpinner: Spinner = findViewById(R.id.kabupatenSpinner)
        mMap.setOnMarkerClickListener { marker ->
            // Show the info fragment
            val latitude = marker.position.latitude
            val longitude = marker.position.longitude
            val selectedKabupaten = kabupatenSpinner.selectedItem.toString()
            val infoFragment = InfoFragment.newInstance(latitude,longitude,marker.title, selectedKabupaten)
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, infoFragment)
                .addToBackStack(null)
                .commit()
            showCardView()
            true

        }
        val surabaya = LatLng(-7.2575, 112.7521)
        val cameraPosition = CameraPosition.Builder().target(surabaya).zoom(12.0f).build()
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

        private fun hideCardView() {
        val cardView = findViewById<FrameLayout>(R.id.fragment_container)
        cardView.visibility = View.GONE

    }
    private fun showCardView() {
        val cardView = findViewById<FrameLayout>(R.id.fragment_container)
        val slideUpAnimation = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f
        )
        slideUpAnimation.duration = 500
        cardView.startAnimation(slideUpAnimation)
        cardView.visibility = View.VISIBLE
    }
    private fun searchLocation(location: String, selectedKabupaten: String) {
        val fullQuery = "$location, $selectedKabupaten"
        val geocoder = Geocoder(this)
        val addressList: List<Address>?

        try {
            addressList = geocoder.getFromLocationName(fullQuery, 1)

            if (addressList != null && addressList.isNotEmpty()) {
                val address = addressList[0]
                val latLng = LatLng(address.latitude, address.longitude)
                if (currentMarker == null) {
                    currentMarker = mMap.addMarker(MarkerOptions().position(latLng).title("$location"))
                } else {
                    currentMarker?.position = latLng
                    currentMarker?.title = "$location"
                }
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))

            } else {
                Toast.makeText(applicationContext, "Location not found", Toast.LENGTH_LONG).show()
            }
            hideCardView()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

}


