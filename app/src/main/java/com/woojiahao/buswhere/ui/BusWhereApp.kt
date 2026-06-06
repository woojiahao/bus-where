package com.woojiahao.buswhere.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.woojiahao.buswhere.ui.screens.BusWhereViewModel
import com.woojiahao.buswhere.ui.screens.FavoritesScreen
import com.woojiahao.buswhere.ui.screens.RoutesScreen
import com.woojiahao.buswhere.ui.screens.SearchScreen

enum class Destination(
  val route: String,
  val label: String,
  val icon: ImageVector,
  val contentDescription: String
) {
  SEARCH("search", "Search", Icons.Default.Search, "Search"),
  FAVORITES("favorites", "Favorites", Icons.Default.FavoriteBorder, "Favorites"),
  ROUTES("routes", "Routes", Icons.Default.Place, "Routes")
}


@Composable
fun AppNavHost(
  viewModel: BusWhereViewModel,
  navController: NavHostController,
  startDestination: Destination,
  modifier: Modifier = Modifier
) {
  NavHost(
    navController,
    startDestination = startDestination.route
  ) {
    Destination.entries.forEach { destination ->
      composable(destination.route) {
        when (destination) {
          Destination.SEARCH -> SearchScreen(viewModel.uiState, modifier)
          Destination.FAVORITES -> FavoritesScreen(modifier)
          Destination.ROUTES -> RoutesScreen(viewModel.uiState, modifier)
        }
      }
    }
  }
}

@Preview()
@Composable
fun BusWhereApp(modifier: Modifier = Modifier) {
  val navController = rememberNavController()
  val startDestination = Destination.FAVORITES
  var selectedDestination by rememberSaveable { mutableIntStateOf(startDestination.ordinal) }

  Scaffold(
    modifier = modifier,
    bottomBar = {
      NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
        Destination.entries.forEachIndexed { index, destination ->
          NavigationBarItem(
            selected = selectedDestination == index,
            onClick = {
              navController.navigate(route = destination.route)
              selectedDestination = index
            },
            icon = {
              Icon(
                destination.icon,
                contentDescription = destination.contentDescription
              )
            },
            label = { Text(destination.label) }
          )
        }
      }
    }
  ) { contentPadding ->
    val busWhereViewModel: BusWhereViewModel = viewModel()
    AppNavHost(
      busWhereViewModel,
      navController,
      startDestination,
      modifier = Modifier.padding(contentPadding)
    )
  }
}
