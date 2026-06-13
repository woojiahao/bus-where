package com.woojiahao.buswhere.ui.widgets.busarrivalcard

import android.content.Context
import android.util.Log
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.getAppWidgetState
import androidx.glance.appwidget.updateAll
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.woojiahao.buswhere.data.api.BusWhereApi
import com.woojiahao.buswhere.data.widget.saveStopService

class BusArrivalCardUpdateWorker(private val context: Context, workerParams: WorkerParameters) :
  CoroutineWorker(context, workerParams) {
  override suspend fun doWork(): Result {
    return try {
      val apiDataSource = BusWhereApi.retrofitService

      val manager = GlanceAppWidgetManager(context)
      val glanceIds = manager.getGlanceIds(BusArrivalCard::class.java)

      for (glanceId in glanceIds) {
        val prefs = getAppWidgetState(context, PreferencesGlanceStateDefinition, glanceId)
        val selectedBusStopCode =
          prefs[BusArrivalCardConfigActivity.SELECTED_BUS_STOP_CODE_KEY] ?: 0
        val selectedServiceNo = prefs[BusArrivalCardConfigActivity.SELECTED_SERVICE_NO] ?: ""
        Log.i(
          "local",
          "selected bus stop code: $selectedBusStopCode; selected service no: $selectedServiceNo"
        )
        val freshData =
          apiDataSource.getArrival(selectedBusStopCode, selectedServiceNo).map { it.toModel() }
        Log.i(
          "local",
          "fresh data length: ${freshData.size}"
        )
        context.saveStopService(selectedBusStopCode, selectedServiceNo, freshData)
      }

      BusArrivalCard().updateAll(context)
      Result.success()
    } catch (e: Exception) {
      Result.retry()
    }
  }
}