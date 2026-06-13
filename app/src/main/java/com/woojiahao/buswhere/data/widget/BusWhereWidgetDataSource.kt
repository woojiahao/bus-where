package com.woojiahao.buswhere.data.widget

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.woojiahao.buswhere.models.Arrival
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json

private val Context.widgetDataSource: DataStore<Preferences> by preferencesDataStore(name = "widget_data_source")

private fun dataKeyForStopService(busStopCode: Int, serviceNo: String) =
  stringPreferencesKey("widget_data_source_${busStopCode}_${serviceNo}")

suspend fun Context.saveStopService(
  busStopCode: Int,
  serviceNo: String,
  arrivals: List<Arrival>
) {
  this.widgetDataSource.edit { preferences ->
    val uniqueKey = dataKeyForStopService(busStopCode, serviceNo)
    Log.i("local", "saveStopService: unique key is $uniqueKey")
    preferences[uniqueKey] = Json.encodeToString(arrivals)
  }
}

suspend fun Context.getStopService(
  busStopCode: Int,
  serviceNo: String
): List<Arrival>? {
  val dataStore = this.widgetDataSource.data.first()
  Log.i("local", "getStopService: data store has ${dataStore.asMap().map { it.key }.joinToString()}")
  val uniqueKey = dataKeyForStopService(busStopCode, serviceNo)
  Log.i("local", "getStopService: unique key is $uniqueKey")
  val data = dataStore[uniqueKey] ?: return null
  return Json.decodeFromString(data)
}