package com.woojiahao.buswhere.ui.widgets.busarrivalcard

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.woojiahao.buswhere.data.api.BusWhereApi
import com.woojiahao.buswhere.repository.BusWhereRepositoryImpl

class BusArrivalCardUpdateWorker(private val context: Context, workerParams: WorkerParameters) :
  CoroutineWorker(context, workerParams) {
  override suspend fun doWork(): Result {
    val repo = BusWhereRepositoryImpl(
      apiDataSource = BusWhereApi.retrofitService,
      context = context.applicationContext
    )
//    return try {
//      val arrivalData
//    }
    TODO("hi")
  }
}