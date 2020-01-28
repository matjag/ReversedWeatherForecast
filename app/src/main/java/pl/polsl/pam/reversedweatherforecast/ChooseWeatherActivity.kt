package pl.polsl.pam.reversedweatherforecast

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import pl.polsl.pam.reversedweatherforecast.ServerEntity.ServerForecast

class ChooseWeatherActivity : AppCompatActivity() {

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

            Toast.makeText(applicationContext,"temp: ${editText1.text} - ${editText2.text} miast: ${cities?.size} pogoda: ${selectedWeather} ", Toast.LENGTH_LONG).show()
        }
    }
}