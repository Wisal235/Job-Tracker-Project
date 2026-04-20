package ca.wali235.jobtracker.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// the main theme wrapper for the whole app
// it picks the right color palette based on system settings
@Composable
fun JobTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // dynamic color only on android 12+ so we keep it off by default
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    // build the right color scheme for current mode
    val colorScheme = when {
        // if user wants dynamic colors and phone is android 12 plus
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }

        // dark mode palette
        darkTheme -> darkColorScheme(
            primary = PrimaryGreen,
            secondary = SecondaryGreen,
            tertiary = AccentBrown,
            background = Color(0xFF121212),
            surface = Color(0xFF1E1E1E),
            onBackground = Color(0xFFE6E1E5),
            onSurface = Color(0xFFE6E1E5),
            onPrimary = Color.White
        )

        // default light mode palette
        else -> lightColorScheme(
            primary = PrimaryGreen,
            secondary = SecondaryGreen,
            tertiary = AccentBrown,
            background = Color(0xFFF6F4EF),
            surface = Color(0xFFFFFFFF),
            onBackground = Color(0xFF1C1B1F),
            onSurface = Color(0xFF1C1B1F),
            onPrimary = Color.White
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}