package com.woojiahao.buswhere

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.woojiahao.buswhere.ui.BusWhereApp
import com.woojiahao.buswhere.ui.theme.BusWhereTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      BusWhereTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
          BusWhereApp(
            context = applicationContext,
            modifier = Modifier.padding(innerPadding)
          )
        }
      }
    }
  }
}

