package com.woojiahao.buswhere.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusStopSearchBar(
  query: String,
  onQueryChange: (q: String) -> Unit,
  enabled: Boolean = true,
) {
  Box(
    Modifier
      .fillMaxWidth()
      .semantics { isTraversalGroup = true }
  ) {
    SearchBar(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 0.dp)
        .align(Alignment.TopCenter)
        .semantics { traversalIndex = 0f },
      inputField = {
        SearchBarDefaults.InputField(
          query = query,
          onQueryChange = onQueryChange,
          onSearch = { /* already filtering live, nothing to do */ },
          expanded = false,
          onExpandedChange = { /* keep collapsed — results are below */ },
          enabled = enabled,
          placeholder = { Text("Enter name, roadname or code") },
          leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = null)
          },
          trailingIcon = {
            AnimatedVisibility(visible = query.isNotEmpty()) {
              IconButton(onClick = { onQueryChange("") }) {
                Icon(Icons.Default.Close, contentDescription = "Clear search")
              }
            }
          }
        )
      },
      // Never expand — the LazyColumn below is the results surface
      expanded = false,
      onExpandedChange = { },
    ) {
      // Empty — no dropdown content
    }
  }
}