package ca.wali235.jobtracker.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// this file holds all shapes for the app
// material 3 uses small / medium / large
// we use same shapes everywhere so app look consistent

val AppShapes = Shapes(
    // small shape -> for chips, small buttons, text fields
    small = RoundedCornerShape(12.dp),

    // medium shape -> for cards, dialogs
    medium = RoundedCornerShape(16.dp),

    // large shape -> for big containers, bottom sheets
    large = RoundedCornerShape(24.dp)
)