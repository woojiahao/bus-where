package com.woojiahao.buswhere.ui.widgets.busarrivalcard

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.getAppWidgetState
import androidx.glance.background
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.Text
import com.woojiahao.buswhere.data.api.BusWhereApi
import com.woojiahao.buswhere.repository.BusWhereArrivalState
import com.woojiahao.buswhere.repository.BusWhereRepository
import com.woojiahao.buswhere.repository.BusWhereRepositoryImpl
import kotlin.time.Clock
import kotlin.time.Duration.Companion.hours
import kotlin.time.ExperimentalTime

class BusArrivalCard : GlanceAppWidget() {
  override val stateDefinition: GlanceStateDefinition<*>
    get() = PreferencesGlanceStateDefinition

  @OptIn(ExperimentalTime::class)
  override suspend fun provideGlance(context: Context, id: GlanceId) {
    val repository: BusWhereRepository =
      BusWhereRepositoryImpl(BusWhereApi.retrofitService, context)

    val preferences = getAppWidgetState(context, PreferencesGlanceStateDefinition, id)
    val selectedBusStopCode =
      preferences[BusArrivalCardConfigActivity.SELECTED_BUS_STOP_CODE_KEY] ?: 0
    val selectedServiceNo = preferences[BusArrivalCardConfigActivity.SELECTED_SERVICE_NO] ?: ""
    Log.i(
      "woojiahao:logs",
      "selectedBusStopCode: $selectedBusStopCode; selectedServiceNo: $selectedServiceNo"
    )

    provideContent {
      Log.i("woojiahao:logs", "loading glance $id")
      val arrivals by produceState<BusWhereArrivalState>(
        BusWhereArrivalState.Loading,
        selectedBusStopCode,
        selectedServiceNo
      ) {
        repository.getArrivals(selectedBusStopCode, selectedServiceNo)
          .collect { value -> this.value = value }
      }
      val timeNow = Clock.System.now().plus(8.hours)

      Box(
        modifier = GlanceModifier.fillMaxSize().padding(vertical = 8.dp, horizontal = 8.dp)
          .cornerRadius(2.dp)
          .background(color = Color(66, 49, 115))
      ) {
        when (arrivals) {
          BusWhereArrivalState.Error -> Text("Something happened")
          BusWhereArrivalState.Idle -> Text("Idle")
          BusWhereArrivalState.Loading -> Text("Loading")
          is BusWhereArrivalState.Success ->
            Row(modifier = GlanceModifier.fillMaxWidth()) {
              // should be 1 at this point, but just in case
              (arrivals as BusWhereArrivalState.Success).arrivals.forEach { arrival ->
                arrival.nextBuses.forEach { bus ->
                  val timeToArrive = bus.estimatedArrival!!.minus(timeNow).inWholeMinutes
                  Column() {
                    Text(if (timeToArrive <= 0L) "A" else timeToArrive.toString())
                  }
                }
              }
            }
        }
      }
    }
  }
}