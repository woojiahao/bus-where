package com.woojiahao.buswhere.ui

import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.woojiahao.buswhere.ui.screens.HomeScreen
import com.woojiahao.buswhere.viewmodel.BusWhereViewModel
import com.woojiahao.buswhere.viewmodel.BusWhereViewModelFactory

@Composable
fun BusWhereApp(context: Context, modifier: Modifier = Modifier) {
  Scaffold(
    modifier = modifier,
  ) { contentPadding ->
    val vm: BusWhereViewModel = viewModel(factory = BusWhereViewModelFactory(context))

    HomeScreen(modifier = modifier.padding(contentPadding), vm = vm)
  }
}
