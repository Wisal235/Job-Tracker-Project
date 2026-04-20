package ca.wali235.jobtracker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// this is the main theme for the app
// it chooses dark or light colors based on phone setting

@Composable
fun JobTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // pick color scheme based on dark or light mode
    val colorScheme = if (darkTheme) {
        // dark mode colors
        darkColorScheme(
            // primary = main brand color (buttons, top bar)
            primary = Color(0xFF6EBF8B),
            onPrimary = Color(0xFF003920),
            primaryContainer = Color(0xFF00522F),
            onPrimaryContainer = Color(0xFF8BF5A7),

            // secondary = second brand color (chips, small buttons)
            secondary = Color(0xFFB6CCB8),
            onSecondary = Color(0xFF223526),
            secondaryContainer = Color(0xFF384B3C),
            onSecondaryContainer = Color(0xFFD2E8D4),

            // tertiary = extra accent (less used)
            tertiary = Color(0xFFA3CDD8),
            onTertiary = Color(0xFF01363F),
            tertiaryContainer = Color(0xFF204E57),
            onTertiaryContainer = Color(0xFFBEEAF5),

            // background = main screen background
            background = Color(0xFF121212),
            onBackground = Color(0xFFE6E1E5),

            // surface = cards and sheets
            surface = Color(0xFF1E1E1E),
            onSurface = Color(0xFFE6E1E5),
            surfaceVariant = Color(0xFF404942),
            onSurfaceVariant = Color(0xFFC0C9C0),

            // error = red color for error messages
            error = Color(0xFFFFB4AB),
            onError = Color(0xFF690005),
            errorContainer = Color(0xFF93000A),
            onErrorContainer = Color(0xFFFFDAD6),

            // outline = borders and dividers
            outline = Color(0xFF8A938B),
            outlineVariant = Color(0xFF404942)
        )
    } else {
        // light mode colors
        lightColorScheme(
            primary = Color(0xFF2F6B46),
            onPrimary = Color(0xFFFFFFFF),
            primaryContainer = Color(0xFF8BF5A7),
            onPrimaryContainer = Color(0xFF002110),

            secondary = Color(0xFF506352),
            onSecondary = Color(0xFFFFFFFF),
            secondaryContainer = Color(0xFFD2E8D4),
            onSecondaryContainer = Color(0xFF0E1F12),

            tertiary = Color(0xFF38656F),
            onTertiary = Color(0xFFFFFFFF),
            tertiaryContainer = Color(0xFFBEEAF5),
            onTertiaryContainer = Color(0xFF001F25),

            background = Color(0xFFF6F4EF),
            onBackground = Color(0xFF1C1B1F),

            surface = Color(0xFFFFFFFF),
            onSurface = Color(0xFF1C1B1F),
            surfaceVariant = Color(0xFFDCE5DB),
            onSurfaceVariant = Color(0xFF404942),

            error = Color(0xFFBA1A1A),
            onError = Color(0xFFFFFFFF),
            errorContainer = Color(0xFFFFDAD6),
            onErrorContainer = Color(0xFF410002),

            outline = Color(0xFF707973),
            outlineVariant = Color(0xFFC0C9C0)
        )
    }

    // apply all three: colors, typography, shapes
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = AppShapes,
        content = content
    )
}