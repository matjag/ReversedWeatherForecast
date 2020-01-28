package pl.polsl.pam.reversedweatherforecast

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
//import android.support.annotation.RequiresApi
import android.view.View
import android.widget.*
import pl.polsl.pam.reversedweatherforecast.ServerEntity.ServerForecast
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class ChooseWeatherActivity : AppCompatActivity() {

    var goodCities = mutableListOf<ServerForecast.TestForecastInfo>()
    var spinWeather = mutableListOf<String>()
    var selectedWeather = ""

//    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_weather)

        val cities = intent.getSerializableExtra("keyIdentifier") as? Array<ServerForecast.TestForecastInfo>

        val editText1 = findViewById<EditText>(R.id.editText)
        val editText2 = findViewById<EditText>(R.id.editText2)
        val spinner = findViewById<Spinner>(R.id.spinner)
        val button = findViewById<Button>(R.id.button2)

        spinWeather.add("sunny")
        spinWeather.add("rainy")
        spinWeather.add("snowy")
        spinWeather.add("stormy")
        spinWeather.add("cloudy")

        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinWeather)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner!!.setAdapter(aa)

        selectedWeather = spinWeather[0]


        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedWeather = spinWeather[position]
            }
        }

        button.setOnClickListener {

            if(editText2.text.toString().toInt() - editText1.text.toString().toInt() < 0){
                Toast.makeText(applicationContext,"Select correct min and max temperature", Toast.LENGTH_LONG).show()
            }
            else{
                cities?.forEach {
                    //var date: LocalDateTime
                    //date = LocalDateTime.parse(it.list.get(0).dt_txt,  DateTimeFormatter.ofPattern("yyyy-MM-dd hh-mm-ss"))
                    //var d = Date.parse(it.list.get(0).dt_txt, DateFormatter.ofPattern("yyyy-MM-dd hh-mm-ss"))
                    var sum = 0.0
                    var avg = 0.0
                    var counter = 0
                    it.list.forEach{

                        var l = it.dt.toLong() * 1000
                        var d = Date(l)
                        var h = d.hours
                        if(h >= 8 || h <= 19){
                            sum += it.main.temp - 273
                            counter++
                        }
                    }
                    

                    if(isGoodWeatherForCity(it)){
                        goodCities.add(it)
                    }
                }
            }
            Toast.makeText(applicationContext,"temp: ${editText1.text} - ${editText2.text} miast: ${cities?.size} pogoda: ${selectedWeather} ", Toast.LENGTH_LONG).show()
        }
    }

    fun isGoodWeatherForCity(cityWeather: ServerForecast.TestForecastInfo): Boolean{



        return false
    }
}