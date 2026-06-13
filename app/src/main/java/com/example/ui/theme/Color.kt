package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// Elegant Dark Palette (from HTML specification)
val ElegantDarkBackground = Color(0xFF1A1C1E)     // #1A1C1E
val ElegantDarkSurface = Color(0xFF1C2024)        // #1C2024 (Primary Card/Surface Elements)
val ElegantDarkSurfaceVariant = Color(0xFF2F3033) // #2F3033 (Secondary containers, nav, stats)
val ElegantDarkOutline = Color(0xFF44474E)        // #44474E (Borders/Dividers)

val ElegantDarkPrimary = Color(0xFF90CAF9)        // #90CAF9 (Dynamic Blue for Command Center accent)
val ElegantDarkSecondary = Color(0xFFC4C7C5)      // #C4C7C5 (Subtexts & secondary keys)
val ElegantDarkTertiary = Color(0xFFD1E4FF)       // #D1E4FF (Selected pills highlight)

// Light Colors (Fallback/Standard)
val StudentPrimaryLight = Color(0xFF2563EB)
val StudentSecondaryLight = Color(0xFF4B5563)
val StudentTertiaryLight = Color(0xFF0D9488)
val StudentBackgroundLight = Color(0xFFF8FAFC)
val StudentSurfaceLight = Color(0xFFFFFFFF)

// Dark Colors mapped directly to Elegant Dark specification
val StudentPrimaryDark = ElegantDarkPrimary
val StudentSecondaryDark = ElegantDarkSecondary
val StudentTertiaryDark = ElegantDarkTertiary
val StudentBackgroundDark = ElegantDarkBackground
val StudentSurfaceDark = ElegantDarkSurface

// Accents & Signals based on HTML specification
val AccentColor = Color(0xFFFFB866)   // #FFB866 (Warm Amber / Assignments)
val SuccessColor = Color(0xFFB4E4A1)  // #B4E4A1 (Pale Green / Internships & Career)
val ErrorColor = Color(0xFFFFB4AB)    // #FFB4AB (GPA - Beautiful soft coral/pinkish red)
val InfoColor = Color(0xFF90CAF9)     // #90CAF9 (Attendance / Placements light blue)
val VaultPurpleColor = Color(0xFFD0BCFF) // #D0BCFF (Vault Purple)
val AiBlueBg = Color(0xFF004A77)      // #004A77 (AI Intelligence banner background)
val AiTextOnBlue = Color(0xFFD1E4FF)  // #D1E4FF (On-AI blue container text)

