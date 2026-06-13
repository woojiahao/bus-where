package com.woojiahao.buswhere.ui.widgets.busarrivalcard

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.updateAll
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import kotlin.time.Duration.Companion.minutes
import kotlin.time.toJavaDuration

class BusArrivalCardUpdateWorker(private val context: Context, workerParams: WorkerParameters) :
  CoroutineWorker(context, workerParams) {
  override suspend fun doWork(): Result {
    return try {
      Log.i("woojiahao:logs", "bus arrival card update worker triggered")
      val ids = GlanceAppWidgetManager(applicationContext).getGlanceIds(BusArrivalCard::class.java)
      Log.i("woojiahao:logs", "widget ids = $ids")
      // This worker just needs to trigger an update to the widgets since the widget loads the data
      // from the repository directly
      BusArrivalCard().updateAll(context)
      Result.success()
    } catch (e: Exception) {
      Result.retry()
    }
  }

  companion object {
    private const val UNIQUE_WORKER_NAME = "busArrivalCardUpdateWorker"

    @RequiresApi(Build.VERSION_CODES.O)
    fun schedule(context: Context) {
      WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        UNIQUE_WORKER_NAME,
        ExistingPeriodicWorkPolicy.KEEP,
        PeriodicWorkRequest.Builder(
          BusArrivalCardUpdateWorker::class.java,
          15.minutes.toJavaDuration()
        ).setInitialDelay(15.minutes.toJavaDuration()).build(),
      )
    }

    fun cancel(context: Context) {
      WorkManager.getInstance(context).cancelUniqueWork(UNIQUE_WORKER_NAME)
    }
  }
}