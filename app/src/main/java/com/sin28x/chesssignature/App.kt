package com.sin28x.chesssignature

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sin28x.chesssignature.ui.screen.MainScreen
import com.sin28x.chesssignature.ui.theme.ChessSignatureTheme

/**
 * Главный Composable приложения.
 * Оборачивает MainScreen в тему ChessSignature.
 */
@Composable
fun App() {
    ChessSignatureTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding(),
            color = MaterialTheme.colorScheme.background
        ) {
            MainScreen()
        }
    }
}