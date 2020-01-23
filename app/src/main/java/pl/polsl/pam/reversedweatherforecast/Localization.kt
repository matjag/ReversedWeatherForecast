package pl.polsl.pam.reversedweatherforecast

class Localization(lat : Double, lon : Double) {
    var lat = lat
    var lon = lon

    fun setLocalization(lat: Double, lon: Double){
        this.lat = lat
        this.lon = lon
    }
}