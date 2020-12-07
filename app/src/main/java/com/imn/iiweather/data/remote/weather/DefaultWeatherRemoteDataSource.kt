package com.imn.iiweather.data.remote.weather

import com.imn.iiweather.BuildConfig
import com.imn.iiweather.data.repository.weather.WeatherRemoteDataSource
import com.imn.iiweather.domain.model.location.LocationModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.coroutines.resumeWithException

class DefaultWeatherRemoteDataSource(
    private val baseUrl: String,
    private val okHttpClient: OkHttpClient,
) : WeatherRemoteDataSource {

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getCurrentWeather(locationModel: LocationModel): WeatherResponse =
        withContext(Dispatchers.IO) {
            suspendCancellableCoroutine { continuation ->
                val request = Request.Builder()
                    .url(baseUrl + BuildConfig.API_KEY + "/${locationModel.latitude},${locationModel.longitude}")
                    .build()

                val call = okHttpClient.newCall(request)
                try {
                    val response = WeatherResponse(call.execute().body!!.string())

                    continuation.resume(response) { call.cancel() }
                } catch (e: Throwable) {
                    continuation.resumeWithException(e)
                }

                continuation.invokeOnCancellation { call.cancel() }
            }
        }


    companion object {
        const val BASE_URL = "https://api.darksky.net/forecast/"
    }
}