package pl.polsl.pam.reversedweatherforecast

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import pl.polsl.pam.reversedweatherforecast.ServerEntity.ServerForecast
import java.util.*

class ChooseWeatherActivity : AppCompatActivity() {

    var goodCities = mutableListOf<ServerForecast.TestForecastInfo>()
    var spinWeather = mutableListOf<String>()
    var selectedWeather = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_weather)

        val cities = intent.getSerializableExtra("keyIdentifier") as? Array<ServerForecast.TestForecastInfo>

        val editText1 = findViewById<EditText>(R.id.editText)
        val editText2 = findViewById<EditText>(R.id.editText2)
        val spinner = findViewById<Spinner>(R.id.spinner)
        val button = findViewById<Button>(R.id.button2)
        val listView = findViewById<ListView>(R.id.listView)

        spinWeather.add("sunny")
        spinWeather.add("rainy")
        spinWeather.add("snowy")
        spinWeather.add("stormy")
        spinWeather.add("cloudy")

        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinWeather)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner!!.setAdapter(aa)

        selectedWeather = spinWeather[0]

        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedWeather = spinWeather[position]
            }
        }

        button.setOnClickListener {
            goodCities.clear()
            var sunnyCounter = 0
            var rainyCounter = 0
            var snowyCounter = 0
            var stormyCounter = 0
            var cloudyCounter = 0

            if (editText1.text.isBlank() || editText2.text.isBlank()) {
                Toast.makeText(applicationContext, "Min and Max temperature can't be empty", Toast.LENGTH_LONG).show()
            } else {
                if (editText2.text.toString().toInt() - editText1.text.toString().toInt() < 0) {
                    Toast.makeText(applicationContext, "Select correct min and max temperature", Toast.LENGTH_LONG).show()
                } else {

//                    var statusMin = 0
//                    var statusMax = 0
//
//                    if (selectedWeather.equals("stormy")) {
//                        statusMin = 200
//                        statusMax = 232
//                    }
//                    if (selectedWeather.equals("rainy")) {
//                        statusMin = 300
//                        statusMax = 531
//                    }
//                    if (selectedWeather.equals("snowy")) {
//                        statusMin = 600
//                        statusMax = 622
//                    }
//                    if (selectedWeather.equals("sunny")) {
//                        statusMin = 800
//                        statusMax = 802
//                    }
//                    if (selectedWeather.equals("cloudy")) {
//                        statusMin = 803
//                        statusMax = 804
//                    }

                    cities?.forEach {
                        var sum = 0.0
                        var avg = 0.0
                        var counter = 0
                        it.list.forEach {

                            var l = it.dt.toLong() * 1000
                            var d = Date(l)
                            var h = d.hours
                            if (h >= 8 && h <= 19) {
                                sum += it.main.temp - 273
                                counter++

                                var stat = it.weather?.get(0).id


                                //sunny
                                if(stat >= 800 && stat <= 802){
                                    sunnyCounter++
                                }
                                //cloudy
                                else if(stat >= 803 && stat <= 804){
                                    cloudyCounter++
                                }
                                //rainy
                                else if(stat >= 300 && stat <= 531){
                                    rainyCounter++
                                }
                                //snowy
                                else if(stat >= 600 && stat <= 622){
                                    snowyCounter++
                                }
                                //stormy
                                else if(stat >= 200 && stat <= 232){
                                    stormyCounter++
                                }
                            }
                        }
                        avg = sum / counter
                        Log.i("msg", "AVG: " + avg.toString())

                        var min = editText1.text.toString().toDouble()
                        var max = editText2.text.toString().toDouble()

                        var countersArray: IntArray = intArrayOf(sunnyCounter, cloudyCounter, rainyCounter, snowyCounter, stormyCounter)
                        //znalezc max po indeksach
                        var idx = findMaxValue(countersArray)

                        var weatherMatch = false

                        if (selectedWeather.equals("sunny") && idx == 0) {
                            weatherMatch = true
                        }
                        else if (selectedWeather.equals("cloudy") && idx == 1) {
                            weatherMatch = true
                        }
                        else if (selectedWeather.equals("rainy") && idx == 2) {
                            weatherMatch = true
                        }
                        else if (selectedWeather.equals("snowy") && idx == 3) {
                            weatherMatch = true
                        }
                        else if (selectedWeather.equals("stormy") && idx == 4) {
                            weatherMatch = true
                        }

                        if (avg.compareTo(min) >= 0 && avg.compareTo(max) <= 0 && weatherMatch) {
                            goodCities.add(it)
                        }
                    }
                    Toast.makeText(applicationContext, "We find: ${goodCities?.size} cities", Toast.LENGTH_LONG).show()
                    var goodCitiesNames = mutableListOf<String>()
                    goodCities.forEach {
                        goodCitiesNames.add(it.city.name)
                    }

                    var goodCitiesArray = arrayOfNulls<String>(goodCitiesNames.size)
                    goodCitiesArray = goodCitiesNames.toTypedArray()
                    val adapter = ArrayAdapter(this, R.layout.listview_item2, goodCitiesArray)

                    listView.adapter = adapter
                }
            }
        }
    }

    fun findMaxValue(arr : IntArray) : Int{
        var max = 0

        for (x in 1 until arr.size){
            if(arr[max] < arr[x]){
                max = x
            }
        }
        return max
    }
}