package com.sin28x.chesssignature.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sin28x.chesssignature.ui.theme.ChessTypography
import com.sin28x.chesssignature.ui.theme.chessColors

/**
 * Кнопка для клетки шахматной доски (a-h, 1-8).
 * Используется в клавиатуре для ввода координат.
 *
 * @param square Текст клетки (например, "e4", "a1")
 * @param onClick Обработчик нажатия
 * @param modifier Модификатор для кастомизации
 * @param enabled Активна ли кнопка
 */
@Composable
fun ChessSquareButton(
    square: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.size(56.dp),
        enabled = enabled,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(
            width = 1.dp,
            color = if (enabled) 
                MaterialTheme.colorScheme.outline 
            else 
                MaterialTheme.colorScheme.outline.copy(alpha = 0.38f)
        ),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onSurface,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
        )
    ) {
        Text(
            text = square,
            style = ChessTypography.keyboardSquare,
            fontWeight = FontWeight.Medium
        )
    }
}

/**
 * Кнопка для шахматной фигуры.
 * Используется в клавиатуре для выбора типа фигуры.
 *
 * @param piece Текст фигуры в русской нотации (например, "К", "Ф", "Л")
 * @param onClick Обработчик нажатия
 * @param modifier Модификатор для кастомизации
 * @param enabled Активна ли кнопка
 * @param selected Выбрана ли кнопка
 */
@Composable
fun PieceButton(
    piece: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    selected: Boolean = false
) {
    val containerColor = when {
        selected -> MaterialTheme.colorScheme.primaryContainer
        else -> Color.Transparent
    }
    
    val contentColor = when {
        !enabled -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
        selected -> MaterialTheme.colorScheme.onPrimaryContainer
        else -> MaterialTheme.colorScheme.onSurface
    }
    
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.size(56.dp),
        enabled = enabled,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(
            width = if (selected) 2.dp else 1.dp,
            color = if (selected) 
                MaterialTheme.colorScheme.primary 
            else if (enabled)
                MaterialTheme.colorScheme.outline
            else
                MaterialTheme.colorScheme.outline.copy(alpha = 0.38f)
        ),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
        )
    ) {
        Text(
            text = piece,
            style = ChessTypography.keyboardPiece,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
        )
    }
}

/**
 * Кнопка для специальных символов (взятие, шах, мат, рокировка).
 * 
 * @param symbol Символ (например, ":", "+", "#", "0-0")
 * @param onClick Обработчик нажатия
 * @param modifier Модификатор для кастомизации
 * @param enabled Активна ли кнопка
 */
@Composable
fun SymbolButton(
    symbol: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.size(width = 120.dp, height = 48.dp),
        enabled = enabled,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(
            width = 1.dp,
            color = if (enabled)
                MaterialTheme.colorScheme.outline
            else
                MaterialTheme.colorScheme.outline.copy(alpha = 0.38f)
        ),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onSurface,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
        )
    ) {
        Text(
            text = symbol,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

/**
 * Кнопка действия (добавить ход, удалить, очистить и т.д.).
 * 
 * @param text Текст кнопки
 * @param onClick Обработчик нажатия
 * @param modifier Модификатор для кастомизации
 * @param enabled Активна ли кнопка
 * @param primary Использовать ли primary стиль (заполненная кнопка)
 */
@Composable
fun ActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    primary: Boolean = false
) {
    if (primary) {
        Button(
            onClick = onClick,
            modifier = modifier.size(width = 120.dp, height = 48.dp),
            enabled = enabled,
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.38f),
                disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.38f)
            )
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Medium
            )
        }
    } else {
        OutlinedButton(
            onClick = onClick,
            modifier = modifier.size(width = 120.dp, height = 48.dp),
            enabled = enabled,
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
            border = BorderStroke(
                width = 1.dp,
                color = if (enabled)
                    MaterialTheme.colorScheme.outline
                else
                    MaterialTheme.colorScheme.outline.copy(alpha = 0.38f)
            ),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onSurface,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
            )
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

/**
 * Кнопка удаления (backspace).
 * 
 * @param onClick Обработчик нажатия
 * @param modifier Модификатор для кастомизации
 * @param enabled Активна ли кнопка
 */
@Composable
fun DeleteButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.size(width = 120.dp, height = 48.dp),
        enabled = enabled,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(
            width = 1.dp,
            color = if (enabled)
                MaterialTheme.colorScheme.error
            else
                MaterialTheme.colorScheme.error.copy(alpha = 0.38f)
        ),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.error,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = MaterialTheme.colorScheme.error.copy(alpha = 0.38f)
        )
    ) {
        Text(
            text = "⌫",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
        )
    }
}