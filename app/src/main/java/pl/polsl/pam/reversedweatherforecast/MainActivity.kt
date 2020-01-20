package pl.polsl.pam.reversedweatherforecast

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import com.beust.klaxon.Klaxon
import pl.polsl.pam.reversedweatherforecast.ServerEntity.ServerForecast
import pl.polsl.pam.reversedweatherforecast.ServerEntity.TestForecast
import java.net.URL

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        val textView = findViewById<TextView>(R.id.text_view)
        val button = findViewById<Button>(R.id.button)

        button.setOnClickListener {
            Thread {
                //api.openweathermap.org/data/2.5/forecast?id={city ID} //wyszukiwanie po id
                val response =
                    URL("https://api.openweathermap.org/data/2.5/forecast?q=Gliwice,PL&APPID=4749cf6173631a815735a7b0b88aeef7").readText()


                try{
                    var result = Klaxon().parse<TestForecast.TestForecastInfo>(response)

                    var s = 10 + 34
                }
                catch (ex: Exception){

                }



                //var gson = Gson()
                //var serverForecast = gson?.fromJson(response, ServerForecast.ForecastInfo::class.java)


                runOnUiThread {

//                    textView.text = response
                }
            }.start()
        }
    }
}
