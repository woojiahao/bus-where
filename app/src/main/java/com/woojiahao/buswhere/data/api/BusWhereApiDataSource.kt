package com.woojiahao.buswhere.data.api

import com.woojiahao.buswhere.data.api.dtos.BusWhereApiRouteDto
import com.woojiahao.buswhere.data.api.dtos.BusWhereApiServiceDto
import com.woojiahao.buswhere.data.api.dtos.BusWhereApiStopDto
import com.woojiahao.buswhere.network.createRetrofit
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://buswhere.woojiahao.com"

private val retrofit = createRetrofit(BASE_URL)

interface BusWhereApiDataSource {
  @GET("api/bus/routes")
  suspend fun getRoutes(): List<BusWhereApiRouteDto>

  @GET("api/bus/services")
  suspend fun getServices(): List<BusWhereApiServiceDto>

  @GET("api/bus/stops")
  suspend fun getStops(): List<BusWhereApiStopDto>

  @GET("api/bus/arrival")
  suspend fun getArrival(
    @Query("bus_stop_code") busStopCode: Int, @Query("service_code") serviceCode: String
  ): String

  @GET("api/bus/arrival")
  suspend fun getArrival(@Query("bus_stop_code") busStopCode: Int): String
}

object BusWhereApi {
  val retrofitService: BusWhereApiDataSource by lazy {
    retrofit.create(BusWhereApiDataSource::class.java)
  }
}