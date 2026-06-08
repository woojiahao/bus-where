package com.woojiahao.buswhere.ui.widgets.busarrivalcard

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

class BusArrivalCardReceiver : GlanceAppWidgetReceiver() {
  override val glanceAppWidget: GlanceAppWidget = BusArrivalCard()
}