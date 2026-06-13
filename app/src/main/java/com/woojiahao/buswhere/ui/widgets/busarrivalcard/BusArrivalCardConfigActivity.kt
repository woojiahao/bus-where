package com.woojiahao.buswhere.ui.widgets.busarrivalcard

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.woojiahao.buswhere.ui.screens.BusWhereScreen
import com.woojiahao.buswhere.viewmodel.BusWhereViewModel
import com.woojiahao.buswhere.viewmodel.BusWhereViewModelFactory
import kotlinx.coroutines.launch

class BusArrivalCardConfigActivity : ComponentActivity() {
  private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID

  companion object {
    val SELECTED_BUS_STOP_CODE_KEY = intPreferencesKey("widget_bus_stop_code")
    val SELECTED_SERVICE_NO = stringPreferencesKey("widget_service_no")
  }

  @RequiresApi(Build.VERSION_CODES.O)
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
      Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        val vm: BusWhereViewModel =
          viewModel(factory = BusWhereViewModelFactory(applicationContext))

        BusWhereScreen(
          modifier = Modifier.padding(innerPadding),
          vm = vm,
          isWidgetConfigMode = true,
          onSelectService = { service, stop ->
            lifecycleScope.launch {
              val context = this@BusArrivalCardConfigActivity
              val glanceId = GlanceAppWidgetManager(context).getGlanceIdBy(appWidgetId)

              updateAppWidgetState(context, PreferencesGlanceStateDefinition, glanceId) {
                it.toMutablePreferences().apply {
                  this[SELECTED_BUS_STOP_CODE_KEY] = stop.busStopCode
                  this[SELECTED_SERVICE_NO] = service.key.serviceNo
                }
              }

              Log.i("woojiahao:logs", "config runner for glance $glanceId and updating")
              BusArrivalCard().update(context, glanceId)

              val resultValue = Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
              setResult(Activity.RESULT_OK, resultValue)
              finish()
            }
          }
        )
      }
    }
  }
}