package com.woojiahao.buswhere.ui.widgets.busarrivalcard

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

class BusArrivalCardReceiver : GlanceAppWidgetReceiver() {
  override val glanceAppWidget: GlanceAppWidget = BusArrivalCard()

  @RequiresApi(Build.VERSION_CODES.O)
  override fun onEnabled(context: Context?) {
    super.onEnabled(context)

    if (context != null) {
      BusArrivalCardUpdateWorker.schedule(context)
    }
  }

  override fun onDisabled(context: Context?) {
    super.onDisabled(context)

    if (context != null) {
      BusArrivalCardUpdateWorker.cancel(context)
    }
  }
}