package com.woojiahao.buswhere.ui.widgets.busarrivalcard

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.datastore.preferences.core.stringPreferencesKey

class BusArrivalCardConfigActivity : ComponentActivity() {
  private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID

  companion object {
    val SELECTED_BUS_STOP_CODE_KEY = stringPreferencesKey("widget_bus_stop_code")
    val SELECTED_SERVICE_NO = stringPreferencesKey("widget_service_no")
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    appWidgetId = intent?.extras?.getInt(
      AppWidgetManager.EXTRA_APPWIDGET_ID,
      AppWidgetManager.INVALID_APPWIDGET_ID
    ) ?: AppWidgetManager.INVALID_APPWIDGET_ID

    if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
      finish()
      return
    }

    setResult(Activity.RESULT_CANCELED)

    setContent {
    }
  }

  private fun runSetupPipeline(selection: String, )
}