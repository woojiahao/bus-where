package com.woojiahao.buswhere.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

private val intercepter = HttpLoggingInterceptor().apply {
  this.level = HttpLoggingInterceptor.Level.BODY
}

private val okHttpClient =
  OkHttpClient.Builder()
    .addInterceptor(intercepter)
    .readTimeout(60, TimeUnit.SECONDS)
    .connectTimeout(60, TimeUnit.SECONDS)
    .writeTimeout(60, TimeUnit.SECONDS)
    .build()

private val json = Json {
  ignoreUnknownKeys = true
  coerceInputValues = true
}

fun createRetrofit(url: String): Retrofit {
  return Retrofit.Builder()
    .client(okHttpClient)
    .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(url)
    .build()
}
