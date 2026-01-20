package com.sin28x.chesssignature.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Цветовая палитра приложения ChessSignature.
 * Использует Material Design 3 цветовую систему.
 */

// Основные цвета темы (Primary)
val Primary = Color(0xFF6750A4)
val OnPrimary = Color(0xFFFFFFFF)
val PrimaryContainer = Color(0xFFEADDFF)
val OnPrimaryContainer = Color(0xFF21005D)

// Вторичные цвета (Secondary)
val Secondary = Color(0xFF625B71)
val OnSecondary = Color(0xFFFFFFFF)
val SecondaryContainer = Color(0xFFE8DEF8)
val OnSecondaryContainer = Color(0xFF1D192B)

// Третичные цвета (Tertiary)
val Tertiary = Color(0xFF7D5260)
val OnTertiary = Color(0xFFFFFFFF)
val TertiaryContainer = Color(0xFFFFD8E4)
val OnTertiaryContainer = Color(0xFF31111D)

// Цвета ошибок (Error)
val Error = Color(0xFFB3261E)
val OnError = Color(0xFFFFFFFF)
val ErrorContainer = Color(0xFFF9DEDC)
val OnErrorContainer = Color(0xFF410E0B)

// Фоновые цвета (Background)
val Background = Color(0xFFFFFBFE)
val OnBackground = Color(0xFF1C1B1F)

// Поверхностные цвета (Surface)
val Surface = Color(0xFFFFFBFE)
val OnSurface = Color(0xFF1C1B1F)
val SurfaceVariant = Color(0xFFE7E0EC)
val OnSurfaceVariant = Color(0xFF49454F)

// Контурные цвета (Outline)
val Outline = Color(0xFF79747E)
val OutlineVariant = Color(0xFFCAC4D0)

// Специфичные цвета для шахматной доски
object ChessColors {
    // Светлые клетки доски
    val LightSquare = Color(0xFFF0D9B5)
    val LightSquareSelected = Color(0xFFBBCA7A)
    
    // Темные клетки доски
    val DarkSquare = Color(0xFFB58863)
    val DarkSquareSelected = Color(0xFF829769)
    
    // Цвета для подсветки ходов
    val LastMoveHighlight = Color(0xFFCDD26A)
    val ValidMoveHint = Color(0x4400FF00)
    
    // Цвета фигур
    val WhitePiece = Color(0xFFFFFFFF)
    val BlackPiece = Color(0xFF000000)
    
    // Цвета для валидации
    val ValidInput = Color(0xFF4CAF50)
    val InvalidInput = Color(0xFFB3261E)
    val IncompleteInput = Color(0xFFFFA726)
}

// Темная тема - Primary
val DarkPrimary = Color(0xFFD0BCFF)
val DarkOnPrimary = Color(0xFF381E72)
val DarkPrimaryContainer = Color(0xFF4F378B)
val DarkOnPrimaryContainer = Color(0xFFEADDFF)

// Темная тема - Secondary
val DarkSecondary = Color(0xFFCCC2DC)
val DarkOnSecondary = Color(0xFF332D41)
val DarkSecondaryContainer = Color(0xFF4A4458)
val DarkOnSecondaryContainer = Color(0xFFE8DEF8)

// Темная тема - Tertiary
val DarkTertiary = Color(0xFFEFB8C8)
val DarkOnTertiary = Color(0xFF492532)
val DarkTertiaryContainer = Color(0xFF633B48)
val DarkOnTertiaryContainer = Color(0xFFFFD8E4)

// Темная тема - Error
val DarkError = Color(0xFFF2B8B5)
val DarkOnError = Color(0xFF601410)
val DarkErrorContainer = Color(0xFF8C1D18)
val DarkOnErrorContainer = Color(0xFFF9DEDC)

// Темная тема - Background
val DarkBackground = Color(0xFF1C1B1F)
val DarkOnBackground = Color(0xFFE6E1E5)

// Темная тема - Surface
val DarkSurface = Color(0xFF1C1B1F)
val DarkOnSurface = Color(0xFFE6E1E5)
val DarkSurfaceVariant = Color(0xFF49454F)
val DarkOnSurfaceVariant = Color(0xFFCAC4D0)

// Темная тема - Outline
val DarkOutline = Color(0xFF938F99)
val DarkOutlineVariant = Color(0xFF49454F)

// Специфичные цвета для шахматной доски в темной теме
object DarkChessColors {
    // Светлые клетки доски
    val LightSquare = Color(0xFF769656)
    val LightSquareSelected = Color(0xFF9CAF6E)
    
    // Темные клетки доски
    val DarkSquare = Color(0xFF4A5F3A)
    val DarkSquareSelected = Color(0xFF6B7F5B)
    
    // Цвета для подсветки ходов
    val LastMoveHighlight = Color(0xFF8B9F5A)
    val ValidMoveHint = Color(0x4400FF00)
    
    // Цвета фигур
    val WhitePiece = Color(0xFFE0E0E0)
    val BlackPiece = Color(0xFF1A1A1A)
    
    // Цвета для валидации
    val ValidInput = Color(0xFF66BB6A)
    val InvalidInput = Color(0xFFEF5350)
    val IncompleteInput = Color(0xFFFFB74D)
}