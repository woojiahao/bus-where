package com.woojiahao.buswhere.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

val okHttpClient =
  OkHttpClient.Builder()
    .readTimeout(60, TimeUnit.SECONDS)
    .connectTimeout(60, TimeUnit.SECONDS)
    .writeTimeout(60, TimeUnit.SECONDS)
    .build()


fun createRetrofit(url: String): Retrofit {
  return Retrofit.Builder()
    .client(okHttpClient)
    .addConverterFactory(Json.Default.asConverterFactory("application/json".toMediaType()))
    .baseUrl(url)
    .build()
}
