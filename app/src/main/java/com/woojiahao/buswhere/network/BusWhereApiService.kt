package com.woojiahao.buswhere.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.woojiahao.buswhere.network.models.BusWhereRoute
import com.woojiahao.buswhere.network.models.BusWhereService
import com.woojiahao.buswhere.network.models.BusWhereStop
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://buswhere.woojiahao.com"

private val okHttpClient =
  OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS).connectTimeout(60, TimeUnit.SECONDS)
    .writeTimeout(60, TimeUnit.SECONDS).build()

private val retrofit = Retrofit.Builder().client(okHttpClient)
  .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
  .baseUrl(BASE_URL).build()

interface BusWhereApiService {
  @GET("api/bus/routes")
  suspend fun getRoutes(): List<BusWhereRoute>

  @GET("api/bus/services")
  suspend fun getServices(): List<BusWhereService>

  @GET("api/bus/stops")
  suspend fun getStops(): List<BusWhereStop>

  @GET("api/bus/arrival")
  suspend fun getArrivals(@Query("bus_stop_code") busStopCode: Int): String

  @GET("api/bus/arrival")
  suspend fun getArrival(
    @Query("bus_stop_code") busStopCode: Int, @Query("service_code") serviceCode: String
  ): String
}

object BusWhereApi {
  val retrofitService: BusWhereApiService by lazy {
    retrofit.create(BusWhereApiService::class.java)
  }
}