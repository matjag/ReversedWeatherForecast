package pl.polsl.pam.reversedweatherforecast.ServerEntity

import android.view.Window

class ServerForecast {

    data class ServerForecastInfo(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<X>,
    val message: Int
)

data class City(
    val coord: Coord,
    val country: String,
    val id: Int,
    val name: String,
    val sunrise: Int,
    val sunset: Int,
    val timezone: Int
)

data class Coord(
    val lat: Double,
    val lon: Double
)

data class X(
    val clouds: Clouds,
    val dt: Int,
    val dt_txt: String,
    val main: Main,
    val rain: Rain,
    val snow: Snow,
    val sys: Sys,
    val weather: List<Weather>,
    val wind: Wind
)

data class Clouds(
    val all: Int
)

data class Main(
    val feels_like: Double,
    val grnd_level: Int,
    val humidity: Int,
    val pressure: Int,
    val sea_level: Int,
    val temp: Double,
    val temp_kf: Double,
    val temp_max: Double,
    val temp_min: Double
)

data class Rain(
    val `3h`: Double
)

data class Snow(
    val `3h`: Double
)

data class Sys(
    val pod: String
)

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)

data class Wind(
    val deg: Int,
    val speed: Double
)
}