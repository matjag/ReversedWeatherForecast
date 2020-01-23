package pl.polsl.pam.reversedweatherforecast

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.*
import com.beust.klaxon.Klaxon
import kotlinx.android.synthetic.main.activity_main.*
//import jdk.nashorn.internal.runtime.ScriptingFunctions.readLine
import pl.polsl.pam.reversedweatherforecast.ServerEntity.TestForecast
//import sun.misc.ClassLoaderUtil
import java.io.BufferedReader
import java.io.InputStream
import java.net.URL


class MainActivity : AppCompatActivity() {

    var startCities = mutableListOf<City>()
    var cities = mutableListOf<City>()
    var isByCity = true

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        val textView = findViewById<TextView>(R.id.text_view)
        val button = findViewById<Button>(R.id.button)
        //val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
        val spinner = findViewById<Spinner>(R.id.spinner3)

        val fileText: List<String> = applicationContext.assets.open("start_cities.txt").bufferedReader().use{
            it.readLines()
        }
        //println(fileText)
        for(s in fileText){
            val city = s.split(";")
            startCities.add(City(city[0].toInt(), city[1], city[2].toDouble(), city[3].toDouble(), city[4].toDouble()))
        }

        val fileText2: List<String> = applicationContext.assets.open("start_cities.txt").bufferedReader().use{
            it.readLines()
        }
        //println(fileText)
        for(s in fileText2){
            val city = s.split(";")
            cities.add(City(city[0].toInt(), city[1], city[2].toDouble(), city[3].toDouble(), city[4].toDouble()))
        }

//        radioGroup.setOnCheckedChangeListener { radioGroup, i ->
//            radioGroup.
//        }

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            spinner3.isEnabled = radio.id == R.id.bycity
            if(radio.id == R.id.bycity){
                isByCity = true
            }
            else{
                isByCity = false
            }
        }

        var spinList = mutableListOf<String>()
        startCities.forEach{
            spinList.add(it.cityName)
        }
        // Create an ArrayAdapter using a simple spinner layout and languages array
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinList)
        // Set layout to use when the list of choices appear
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Set Adapter to Spinner
        spinner3!!.setAdapter(aa)

//        radioGroup.setOnCheckedChangeListener(
//            RadioGroup.OnCheckedChangeListener { group, checkedId ->
//                val radio: RadioButton = findViewById(checkedId)
//                Toast.makeText(applicationContext," On checked change :"+
//                        " ${radio.text}",
//                    Toast.LENGTH_SHORT).show()
//            })
//        button.setOnClickListener{
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
            Thread {
                //api.openweathermap.org/data/2.5/forecast?id={city ID} //wyszukiwanie po id
                val response =
                    URL("https://api.openweathermap.org/data/2.5/forecast?q=Gliwice,PL&APPID=4749cf6173631a815735a7b0b88aeef7").readText()
                
                try{
                    var result = Klaxon().parse<TestForecast.TestForecastInfo>(response)
                }
                catch (ex: Exception){
                }
                
                runOnUiThread {

//                    textView.text = response
                }
            }.start()
        }
    }

    // Get the selected radio button text using radio button on click listener
//    fun radio_button_click(view: View){
//        // Get the clicked radio button instance
//        val radio: RadioButton = findViewById(radioGroup.checkedRadioButtonId)
//        Toast.makeText(applicationContext,"On click : ${radio.text}",
//            Toast.LENGTH_SHORT).show()
//    }
}
