package com.woojiahao.buswhere.ui

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.woojiahao.buswhere.ui.screens.BusWhereScreen
import com.woojiahao.buswhere.ui.theme.BusWhereTheme
import com.woojiahao.buswhere.viewmodel.BusWhereViewModel
import com.woojiahao.buswhere.viewmodel.BusWhereViewModelFactory

@Composable
fun BusWhereApp(context: Context) {
  var showSearchBar by remember { mutableStateOf(false) }
  val vm: BusWhereViewModel = viewModel(factory = BusWhereViewModelFactory(context))

  BusWhereTheme {
    Scaffold(
      modifier = Modifier.fillMaxSize(),
      floatingActionButton = {
        FloatingActionButton(onClick = { showSearchBar = !showSearchBar }) {
          Icon(
            Icons.Default.Search,
            "Search for bus by bus stop code, road name, or stop name"
          )
        }
      },
      floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
      BusWhereScreen(
        modifier = Modifier.padding(innerPadding),
        vm = vm,
        showSearchBar = showSearchBar
      )
    }
  }
}
