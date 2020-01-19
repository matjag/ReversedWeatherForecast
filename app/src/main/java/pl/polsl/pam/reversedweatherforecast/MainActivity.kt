package pl.polsl.pam.reversedweatherforecast

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import java.net.URL

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val textView = findViewById<TextView>(R.id.text_view)
        val button = findViewById<Button>(R.id.button)

        button.setOnClickListener {
            Thread {
                val response =
                    URL("https://api.openweathermap.org/data/2.5/forecast?q=Gliwice,PL&APPID=4749cf6173631a815735a7b0b88aeef7").readText()

                runOnUiThread {
                    textView.text = response
                }
            }.start()
        }
    }
}
