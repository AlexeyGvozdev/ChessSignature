package com.sin28x.chesssignature.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Клавиатура для ввода координат шахматной доски.
 * Содержит буквы (a-h) и цифры (1-8).
 *
 * @param onSquareClick Обработчик нажатия на клетку
 * @param modifier Модификатор для кастомизации
 */
@Composable
fun CoordinateKeyboard(
    onSquareClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Ряд с буквами a-h
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            ('a'..'h').forEach { letter ->
                ChessSquareButton(
                    square = letter.toString(),
                    onClick = { onSquareClick(letter.toString()) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
        
        // Ряд с цифрами 1-8
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            (1..8).forEach { number ->
                ChessSquareButton(
                    square = number.toString(),
                    onClick = { onSquareClick(number.toString()) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

/**
 * Клавиатура для выбора типа фигуры.
 * Содержит фигуры в русской нотации: К, Ф, Л, С, Кр.
 *
 * @param onPieceClick Обработчик нажатия на фигуру
 * @param modifier Модификатор для кастомизации
 * @param selectedPiece Выбранная фигура (для визуальной индикации)
 */
@Composable
fun PieceKeyboard(
    onPieceClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    selectedPiece: String? = null
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        listOf("К", "Ф", "Л", "С", "Кр").forEach { piece ->
            PieceButton(
                piece = piece,
                onClick = { onPieceClick(piece) },
                selected = selectedPiece == piece,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

/**
 * Клавиатура для специальных символов.
 * Содержит: взятие (:), шах (+), мат (#), рокировки (0-0, 0-0-0).
 *
 * @param onSymbolClick Обработчик нажатия на символ
 * @param onDeleteClick Обработчик нажатия на удаление
 * @param modifier Модификатор для кастомизации
 * @param deleteEnabled Активна ли кнопка удаления
 */
@Composable
fun SymbolKeyboard(
    onSymbolClick: (String) -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
    deleteEnabled: Boolean = true
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Взятие
        SymbolButton(
            symbol = ":",
            onClick = { onSymbolClick(":") },
            modifier = Modifier.weight(1f)
        )
        
        // Шах
        SymbolButton(
            symbol = "+",
            onClick = { onSymbolClick("+") },
            modifier = Modifier.weight(1f)
        )
        
        // Мат
        SymbolButton(
            symbol = "#",
            onClick = { onSymbolClick("#") },
            modifier = Modifier.weight(1f)
        )
        
        // Короткая рокировка
        SymbolButton(
            symbol = "0-0",
            onClick = { onSymbolClick("0-0") },
            modifier = Modifier.weight(1.5f)
        )
        
        // Длинная рокировка
        SymbolButton(
            symbol = "0-0-0",
            onClick = { onSymbolClick("0-0-0") },
            modifier = Modifier.weight(1.5f)
        )
        
        // Удаление
        DeleteButton(
            onClick = onDeleteClick,
            enabled = deleteEnabled,
            modifier = Modifier.weight(1f)
        )
    }
}

/**
 * Полная шахматная клавиатура.
 * Объединяет все клавиатуры: координаты, фигуры и символы.
 *
 * @param onSquareClick Обработчик нажатия на клетку
 * @param onPieceClick Обработчик нажатия на фигуру
 * @param onSymbolClick Обработчик нажатия на символ
 * @param onDeleteClick Обработчик нажатия на удаление
 * @param modifier Модификатор для кастомизации
 * @param selectedPiece Выбранная фигура
 * @param deleteEnabled Активна ли кнопка удаления
 */
@Composable
fun ChessKeyboard(
    onSquareClick: (String) -> Unit,
    onPieceClick: (String) -> Unit,
    onSymbolClick: (String) -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
    selectedPiece: String? = null,
    deleteEnabled: Boolean = true
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Клавиатура координат
            CoordinateKeyboard(
                onSquareClick = onSquareClick
            )
            
            // Клавиатура фигур
            PieceKeyboard(
                onPieceClick = onPieceClick,
                selectedPiece = selectedPiece
            )
            
            // Клавиатура символов
            SymbolKeyboard(
                onSymbolClick = onSymbolClick,
                onDeleteClick = onDeleteClick,
                deleteEnabled = deleteEnabled
            )
        }
    }
}