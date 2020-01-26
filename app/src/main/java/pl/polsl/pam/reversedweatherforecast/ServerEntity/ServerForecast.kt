package pl.polsl.pam.reversedweatherforecast.ServerEntity

import android.view.Window
import com.beust.klaxon.Json
import org.jetbrains.annotations.Nullable
import java.io.Serializable

class ServerForecast{

    data class TestForecastInfo(
            val city: City,
            val cod: String,
            val list: List<X>
    ) : Serializable

    data class City(
            val id: Int,
            val name: String
    ) : Serializable

    data class X(
            val clouds: Clouds,
            val dt: Int,
            val dt_txt: String,
            val main: Main,
            val weather: List<Weather>
    ) : Serializable

    data class Clouds(
            val all: Int
    ) : Serializable

    data class Main(
            val temp: Double
    ) : Serializable

    data class Weather(
            val id: Int
    ) : Serializable
}