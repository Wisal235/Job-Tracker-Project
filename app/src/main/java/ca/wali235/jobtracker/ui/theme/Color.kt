package ca.wali235.jobtracker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// --- brand colors that stay the same in light and dark modes ---
val PrimaryGreen = Color(0xFF1F6B52)
val SecondaryGreen = Color(0xFF2E8B6E)
val AccentBrown = Color(0xFFB08968)

// --- status colors for job badges (also same in both modes) ---
val LeadColor = Color(0xFFF4A261)
val QuoteColor = Color(0xFF3A86FF)
val ActiveColor = Color(0xFF2A9D8F)
val DoneColor = Color(0xFF6C757D)

// --- theme aware colors ---
// these switch automatically when user turns dark mode on or off
// we keep the same names so the rest of the app does not change

// main screen background
// light mode: cream paper color
// dark mode: almost black
val LightBackground: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xFF121212) else Color(0xFFF6F4EF)

// card background
// light mode: pure white
// dark mode: slightly lifted dark grey so cards stand out
val CardBackground: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xFF1E1E1E) else Color(0xFFFFFFFF)

// main text color
// light mode: near black
// dark mode: near white
val TextDark: Color
    @Composable
    get() = if (isSystemInDarkTheme()) Color(0xFFE6E1E5) else Color(0xFF1C1B1F)