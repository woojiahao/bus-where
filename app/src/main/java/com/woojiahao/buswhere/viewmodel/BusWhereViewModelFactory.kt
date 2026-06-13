package com.woojiahao.buswhere.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.woojiahao.buswhere.data.api.BusWhereApi
import com.woojiahao.buswhere.repository.BusWhereRepositoryImpl

class BusWhereViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    val repo = BusWhereRepositoryImpl(
      apiDataSource = BusWhereApi.retrofitService,
      context = context.applicationContext
    )

    @Suppress("UNCHECKED_CAST")
    return BusWhereViewModel(repo) as T
  }
}