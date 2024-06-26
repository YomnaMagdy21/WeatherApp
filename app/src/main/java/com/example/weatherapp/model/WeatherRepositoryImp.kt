package com.example.weatherapp.model


import com.example.weatherapp.database.WeatherLocalDataSource
import com.example.weatherapp.network.WeatherRemoteDataSource
import kotlinx.coroutines.flow.Flow

class WeatherRepositoryImp(private var weatherRemoteDataSource: WeatherRemoteDataSource,
                           private var weatherLocalDataSource: WeatherLocalDataSource
):WeatherRepository{

    companion object{
        private var instance:WeatherRepositoryImp?=null
        fun getInstance(
            weatherRemoteDataSource: WeatherRemoteDataSource,
            weatherLocalDataSource: WeatherLocalDataSource
        ):WeatherRepositoryImp{
            return instance?: synchronized(this){
                val temp=WeatherRepositoryImp(
                    weatherRemoteDataSource,weatherLocalDataSource)
                instance=temp
                temp
            }
        }
    }

    override  fun getWeather(lat:Double, lon:Double, exclude:String, units:String, lang:String): Flow<WeatherResponse> {
        return weatherRemoteDataSource.getTempOverNetwork(lat,lon,exclude,units,lang)
    }



    override fun getHomeWeather(): Flow<WeatherResponse> {
        return weatherLocalDataSource.getStoredWeather()
    }

    override suspend fun insertHomeWeather(weatherResponse: WeatherResponse) {
        weatherLocalDataSource.insert(weatherResponse)
    }

    override suspend fun deleteHomeWeather() {
        weatherLocalDataSource.deleteAll()
    }

    override fun getFavoriteWeather(): Flow<List<Favorite>> {
        return weatherLocalDataSource.getStoredFavorite()
    }

    override suspend fun insertFavoriteWeather(favorite: Favorite) {
        weatherLocalDataSource.insertFav(favorite)
    }

    override suspend fun deleteFavoriteWeather(favorite: Favorite) {
        weatherLocalDataSource.deleteFav(favorite)

    }

    override fun getAlertWeather(): Flow<List<AlertMessage>> {
        return weatherLocalDataSource.getStoredAlert()
    }

    override suspend fun insertAlertWeather(alert: AlertMessage) {
        weatherLocalDataSource.insertAlert(alert)
    }

    override suspend fun deleteAlertWeather(alert: AlertMessage) {
        weatherLocalDataSource.deleteAlert(alert)
    }
}