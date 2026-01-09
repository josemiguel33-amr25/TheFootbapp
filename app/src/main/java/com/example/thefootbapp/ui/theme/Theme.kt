package com.example.thefootbapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Orange,
    onPrimary = White,
    background = Black,
    surface = Grey,
    onBackground = White,
    onSurface = White,
    secondary = Orange,
    outline = Border
)

private val LightColorScheme = lightColorScheme(
    primary = Orange,
    onPrimary = White,
    background = DeepBlack,
    surface = LightGrey,
    onBackground = DeepBlack,
    onSurface = DeepBlack,
    secondary = DeepBlack,
    outline = Border,

)

@Composable
fun TheFootbappTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, //Desactivado porque sino coge mis colores del fondo de pantalla y no del Tema
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