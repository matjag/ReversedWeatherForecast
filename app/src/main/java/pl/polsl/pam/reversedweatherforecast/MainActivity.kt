package pl.polsl.pam.reversedweatherforecast

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.*
import com.beust.klaxon.Klaxon
import kotlinx.android.synthetic.main.activity_main.*
import pl.polsl.pam.reversedweatherforecast.ServerEntity.ServerForecast
import java.net.URL
import kotlin.math.*


class MainActivity : AppCompatActivity() {

    var startCities = mutableListOf<City>()
    var cities = mutableListOf<City>()
    var isByCity = true
    var distance = 50
    var localizationCoord = Localization(0.0, 0.0)
    var citiesFromResponse = mutableListOf<ServerForecast.TestForecastInfo>()
    var isInThread = false


    lateinit var locationManager: LocationManager
    private var hasGps = false
    private var hasNetwork = false
    private var locationGps: Location? = null
    private var locationNetwork: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        val textView = findViewById<TextView>(R.id.text_view)
        val button = findViewById<Button>(R.id.button)
        //val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
        val spinner3 = findViewById<Spinner>(R.id.spinner3)
        val seekBar = findViewById<SeekBar>(R.id.seekBar)
        val seekBarLabel = findViewById<TextView>(R.id.textView4)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.setVisibility(View.INVISIBLE)

        seekBarLabel.text = distance.toString() + "km"


        val fileText: List<String> =
            applicationContext.assets.open("start_cities.txt").bufferedReader().use {
                it.readLines()
            }
        //println(fileText)
        for (s in fileText) {
            val city = s.split(";")
            startCities.add(
                City(
                    city[0].toInt(),
                    city[1],
                    city[2].toDouble(),
                    city[3].toDouble(),
                    city[4].toDouble()
                )
            )
        }

        val fileText2: List<String> =
            applicationContext.assets.open("cities.txt").bufferedReader().use {
                it.readLines()
            }
        //println(fileText)
        for (s in fileText2) {
            val city = s.split(";")
            cities.add(
                City(
                    city[0].toInt(),
                    city[1],
                    city[2].toDouble(),
                    city[3].toDouble(),
                    city[4].toDouble()
                )
            )
        }

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            spinner3.isEnabled = radio.id == R.id.bycity
            if(radio.id == R.id.bylocalization){
                localizationCoord.lat = getLocation().lat
                localizationCoord.lon = getLocation().lon
            }
        }

        var selectedCity: City?
        var spinList = mutableListOf<String>()
        startCities.forEach {
            spinList.add(it.cityName)
        }
        // Create an ArrayAdapter using a simple spinner layout and languages array
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinList)
        // Set layout to use when the list of choices appear
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Set Adapter to Spinner
        spinner3!!.setAdapter(aa)

        selectedCity = startCities.findLast { it.cityName == spinner3.selectedItem }
        localizationCoord.setLocalization(selectedCity!!.lat, selectedCity!!.lon)


        seekBar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seek: SeekBar,
                progress: Int, fromUser: Boolean
            ) {
                // write custom code for progress is changed
                distance = progress
                seekBarLabel.text = distance.toString() + "km";
            }

            override fun onStartTrackingTouch(seek: SeekBar) {
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
            }
        })

        spinner3?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                localizationCoord.setLocalization(
                    startCities[position].lat,
                    startCities[position].lon
                )
            }

        }

//        radioGroup.setOnCheckedChangeListener(
//            RadioGroup.OnCheckedChangeListener { group, checkedId ->
//                val radio: RadioButton = findViewById(checkedId)
//                Toast.makeText(applicationContext," On checked change :"+
//                        " ${radio.text}",
//                    Toast.LENGTH_SHORT).show()
//
//            // Get the checked radio button id from radio group
//            var id: Int = radioGroup.checkedRadioButtonId
//            if (id!=-1){ // If any radio button checked from radio group
//                // Get the instance of radio button using id
//                val radio:RadioButton = findViewById(id)
//                Toast.makeText(applicationContext,"On button click :" +
//                        " ${radio.text}",
//                    Toast.LENGTH_SHORT).show()
//            }else{
//                // If no radio button checked in this radio group
//                Toast.makeText(applicationContext,"On button click :" +
//                        " nothing selected",
//                    Toast.LENGTH_SHORT).show()
//            }
//        }


        button.setOnClickListener {

            // w tym miejscu bedzie obliczenie wszystkiego
            // 1. pobrac lokalizacje z localizationCoord
            // 2. pobrac dystans przeszukiwania z distance
            // 3. wyszukać miasta które dla których nalezy sprawdzic pogode

            if (isOnline(applicationContext) == true) {
                if (isInThread == false) {
                    isInThread = true
                    progressBar.setVisibility(View.VISIBLE)

                    Thread {
                        var c = findCities(localizationCoord, distance)
                        var test = mutableListOf<String>()
                        Log.i("przed requestem", "msg")
                        c.forEach {
                            val response =
                            //api.openweathermap.org/data/2.5/forecast?id={city ID}
                                //URL("https://api.openweathermap.org/data/2.5/forecast?q=Gliwice,PL&APPID=4749cf6173631a815735a7b0b88aeef7").readText()
                                URL("https://api.openweathermap.org/data/2.5/forecast?id=" + it.id.toString() + "&APPID=4749cf6173631a815735a7b0b88aeef7").readText()

                            test.add(response)
                        }
                        Log.i("po requestem", "msg")

                        val klaxon = Klaxon()
                        test.forEach {
                            try {
                                var result = Klaxon().parse<ServerForecast.TestForecastInfo>(it)

                                if (result != null) {
                                    citiesFromResponse.add(result)
                                }
                            } catch (ex: Exception) {
                            }
                        }
                        Log.i("miasta", "msg")
                        isInThread = false
                        progressBar.setVisibility(View.INVISIBLE)

                        //val array = arrayOfNulls<ServerForecast.TestForecastInfo>(citiesFromResponse.size)

                        // .toArray(array)

                        val array2 = citiesFromResponse.toTypedArray()


                        val intent = Intent(this, ChooseWeatherActivity::class.java)
                        // To pass any data to next activity
                        intent.putExtra("keyIdentifier", array2)
                        // start your next activity
                        startActivity(intent)

//            val intent = Intent(this, ChooseWeatherActivity::class.java)
//            // To pass any data to next activity
//            intent.putExtra("keyIdentifier", array2)
//            // start your next activity
//            startActivity(intent)

                    }.start()

                    //while(isInThread);
                    //var asd = 12 + 23
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    "Check your internet connection",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    fun findCities(
        loc: pl.polsl.pam.reversedweatherforecast.Localization,
        disc: Int
    ): List<City> {
        var listOfCity = mutableListOf<City>()

        var counter = 0

        cities.forEach {
            if (haversineDistance(loc.lat, loc.lon, it.lat, it.lon) <= disc && counter < 59) {
                listOfCity.add(it)
                counter++;

            }
        }

        return listOfCity
    }

    fun haversineDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        var tmpDistance = 0.0

        var R = 6371
        var lat = 0.0
        var lon = 0.0

        lat = degreeToRadian(lat2 - lat1)
        lon = degreeToRadian(lon2 - lon1)

        //var h1 = Math.Sin(lat / 2) * Math.Sin(lat / 2) + Math.Cos(DegreeToRadian(pos1_lat)) * Math.Cos(DegreeToRadian(pos2_lat)) * Math.Sin(lng / 2) * Math.Sin(lng / 2);
        var h1 =
            sin(lon / 2.0) * sin(lon / 2.0) + cos(degreeToRadian(lon1)) * cos(
                degreeToRadian(
                    lon2
                )
            ) * sin(
                lat / 2.0
            ) * sin(lat / 2.0)
        var h2 = 2 * asin(min(1.0, sqrt(h1)))

        tmpDistance = R * h2


        return tmpDistance
    }

    fun degreeToRadian(angle: Double): Double {
        val d = ((PI * angle) / 180.0)
        return d
    }


    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }


// Get the selected radio button text using radio button on click listener
//    fun radio_button_click(view: View){
//        // Get the clicked radio button instance
//        val radio: RadioButton = findViewById(radioGroup.checkedRadioButtonId)
//        Toast.makeText(applicationContext,"On click : ${radio.text}",
//            Toast.LENGTH_SHORT).show()
//    }


    @SuppressLint("MissingPermission")
    private fun getLocation(): Localization {
        var localization = Localization(0.0, 0.0)
        getLocationPermission()
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            if (hasGps) {
                Log.d("CodeAndroidLocation", "hasGps")
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    5000,
                    0F,
                    object : LocationListener {
                        override fun onLocationChanged(location: Location?) {
                            if (location != null) {
                                locationGps = location
                                localization.lat = locationGps!!.latitude
                                localization.lon = locationGps!!.longitude
                            }
                        }

                        override fun onStatusChanged(
                            provider: String?,
                            status: Int,
                            extras: Bundle?
                        ) {

                        }

                        override fun onProviderEnabled(provider: String?) {

                        }

                        override fun onProviderDisabled(provider: String?) {

                        }

                    })

                val localGpsLocation =
                    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (localGpsLocation != null)
                    locationGps = localGpsLocation
            }

            if (locationGps != null) {

                localization.lat = locationGps!!.latitude
                localization.lon = locationGps!!.longitude
            }

         else {
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }

        return localization
    }

    fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                0
            )
            return
        }
    }
}

