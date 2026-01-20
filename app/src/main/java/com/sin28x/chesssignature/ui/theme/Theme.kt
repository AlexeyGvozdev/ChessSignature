package com.sin28x.chesssignature.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * Светлая цветовая схема Material 3.
 */
private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = OnPrimaryContainer,
    
    secondary = Secondary,
    onSecondary = OnSecondary,
    secondaryContainer = SecondaryContainer,
    onSecondaryContainer = OnSecondaryContainer,
    
    tertiary = Tertiary,
    onTertiary = OnTertiary,
    tertiaryContainer = TertiaryContainer,
    onTertiaryContainer = OnTertiaryContainer,
    
    error = Error,
    onError = OnError,
    errorContainer = ErrorContainer,
    onErrorContainer = OnErrorContainer,
    
    background = Background,
    onBackground = OnBackground,
    
    surface = Surface,
    onSurface = OnSurface,
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = OnSurfaceVariant,
    
    outline = Outline,
    outlineVariant = OutlineVariant
)

/**
 * Темная цветовая схема Material 3.
 */
private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkOnPrimary,
    primaryContainer = DarkPrimaryContainer,
    onPrimaryContainer = DarkOnPrimaryContainer,
    
    secondary = DarkSecondary,
    onSecondary = DarkOnSecondary,
    secondaryContainer = DarkSecondaryContainer,
    onSecondaryContainer = DarkOnSecondaryContainer,
    
    tertiary = DarkTertiary,
    onTertiary = DarkOnTertiary,
    tertiaryContainer = DarkTertiaryContainer,
    onTertiaryContainer = DarkOnTertiaryContainer,
    
    error = DarkError,
    onError = DarkOnError,
    errorContainer = DarkErrorContainer,
    onErrorContainer = DarkOnErrorContainer,
    
    background = DarkBackground,
    onBackground = DarkOnBackground,
    
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,
    
    outline = DarkOutline,
    outlineVariant = DarkOutlineVariant
)

/**
 * Класс для хранения специфичных цветов шахматной доски.
 * Предоставляется через CompositionLocal для доступа из любого Composable.
 */
data class ChessColorScheme(
    val lightSquare: androidx.compose.ui.graphics.Color,
    val lightSquareSelected: androidx.compose.ui.graphics.Color,
    val darkSquare: androidx.compose.ui.graphics.Color,
    val darkSquareSelected: androidx.compose.ui.graphics.Color,
    val lastMoveHighlight: androidx.compose.ui.graphics.Color,
    val validMoveHint: androidx.compose.ui.graphics.Color,
    val whitePiece: androidx.compose.ui.graphics.Color,
    val blackPiece: androidx.compose.ui.graphics.Color,
    val validInput: androidx.compose.ui.graphics.Color,
    val invalidInput: androidx.compose.ui.graphics.Color,
    val incompleteInput: androidx.compose.ui.graphics.Color
)

/**
 * Светлая схема шахматных цветов.
 */
private val LightChessColorScheme = ChessColorScheme(
    lightSquare = ChessColors.LightSquare,
    lightSquareSelected = ChessColors.LightSquareSelected,
    darkSquare = ChessColors.DarkSquare,
    darkSquareSelected = ChessColors.DarkSquareSelected,
    lastMoveHighlight = ChessColors.LastMoveHighlight,
    validMoveHint = ChessColors.ValidMoveHint,
    whitePiece = ChessColors.WhitePiece,
    blackPiece = ChessColors.BlackPiece,
    validInput = ChessColors.ValidInput,
    invalidInput = ChessColors.InvalidInput,
    incompleteInput = ChessColors.IncompleteInput
)

/**
 * Темная схема шахматных цветов.
 */
private val DarkChessColorScheme = ChessColorScheme(
    lightSquare = DarkChessColors.LightSquare,
    lightSquareSelected = DarkChessColors.LightSquareSelected,
    darkSquare = DarkChessColors.DarkSquare,
    darkSquareSelected = DarkChessColors.DarkSquareSelected,
    lastMoveHighlight = DarkChessColors.LastMoveHighlight,
    validMoveHint = DarkChessColors.ValidMoveHint,
    whitePiece = DarkChessColors.WhitePiece,
    blackPiece = DarkChessColors.BlackPiece,
    validInput = DarkChessColors.ValidInput,
    invalidInput = DarkChessColors.InvalidInput,
    incompleteInput = DarkChessColors.IncompleteInput
)

/**
 * CompositionLocal для доступа к шахматным цветам из любого Composable.
 */
val LocalChessColors = staticCompositionLocalOf { LightChessColorScheme }

/**
 * Extension property для удобного доступа к шахматным цветам.
 * Использование: MaterialTheme.chessColors.lightSquare
 */
val MaterialTheme.chessColors: ChessColorScheme
    @Composable
    get() = LocalChessColors.current

/**
 * Главная тема приложения ChessSignature.
 * 
 * @param darkTheme Использовать темную тему. По умолчанию определяется системными настройками.
 * @param dynamicColor Использовать динамические цвета (Material You) на Android 12+.
 * @param content Содержимое приложения.
 */
@Composable
fun ChessSignatureTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    // Определяем цветовую схему
    val colorScheme = when {
        // Динамические цвета доступны на Android 12+
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        // Используем предопределенные темы
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    
    // Определяем шахматную цветовую схему
    val chessColorScheme = if (darkTheme) DarkChessColorScheme else LightChessColorScheme
    
    // Настройка системных баров
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }
    
    // Применяем тему
    CompositionLocalProvider(LocalChessColors provides chessColorScheme) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}