package com.woojiahao.buswhere.data.widget

import androidx.datastore.preferences.core.stringPreferencesKey

fun dataKeyForStopService(busStopCode: Int, serviceNo: String) =
  stringPreferencesKey("widget_data_source_${busStopCode}_${serviceNo}")
