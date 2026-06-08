package com.woojiahao.buswhere.ui.widgets.busarrivalcard

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.color.ColorProvider
import androidx.glance.currentState
import androidx.glance.layout.Box
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle

class BusArrivalCard : GlanceAppWidget() {
  override val stateDefinition: GlanceStateDefinition<*>?
    get() = PreferencesGlanceStateDefinition

  override suspend fun provideGlance(context: Context, id: GlanceId) {
    provideContent {
      val preferences = currentState<Preferences>()
      val selectedBusStopCode = prefs[]

      Box(
        modifier = GlanceModifier.fillMaxSize().padding(vertical = 8.dp, horizontal = 8.dp)
          .cornerRadius(2.dp)
          .background(color = Color(66, 49, 115))
      ) {
        Text(
          style = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = ColorProvider(day = Color(224, 224, 224), night = Color(224, 224, 224))
          ),
          text = "Hello world!"
        )
      }
    }
  }
}