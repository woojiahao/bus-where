package com.woojiahao.buswhere.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

// TODO(woojiahao): Support dark theme
private val DarkColorScheme = darkColorScheme(
  primary = Purple80,
  secondary = PurpleGrey80,
  tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
  primary = Primary,
  secondary = Secondary,
  tertiary = Tertiary,
  background = Background,
  surface = Surface,
  onPrimary = White,
  onSecondary = White,
  onTertiary = White,
  onBackground = White,
  onSurface = OnSurface,
  outline = Outline,
  onSurfaceVariant = OnSurfaceVariant,
  surfaceContainer = SurfaceContainer,
  error = Error,
  errorContainer = ErrorContainer,
  onErrorContainer = OnErrorContainer,
  onError = OnError,
  primaryContainer = PrimaryContainer,
  onPrimaryContainer = OnPrimaryContainer,
  secondaryContainer = SecondaryContainer,
  onSecondaryContainer = OnSecondaryContainer,
  tertiaryContainer = TertiaryContainer,
  onTertiaryContainer = OnTertiaryContainer
)

@Composable
fun BusWhereTheme(
  darkTheme: Boolean = false,
  // Dynamic color is available on Android 12+
  dynamicColor: Boolean = true,
  content: @Composable () -> Unit
) {
  val colorScheme = when {
    dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
      val context = LocalContext.current
      if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    }

    darkTheme -> DarkColorScheme
    else -> LightColorScheme
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}