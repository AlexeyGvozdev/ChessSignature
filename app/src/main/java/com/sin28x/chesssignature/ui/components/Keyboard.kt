package com.sin28x.chesssignature.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
 * @param onKeyPressed Обработчик нажатия на любую клавишу
 * @param onBackspace Обработчик нажатия на backspace
 * @param onClear Обработчик нажатия на очистку
 * @param onEnter Обработчик нажатия на ввод
 * @param modifier Модификатор для кастомизации
 * @param inputValidation Результат валидации текущего ввода
 */
@Composable
fun ChessKeyboard(
    onKeyPressed: (String) -> Unit,
    onBackspace: () -> Unit,
    onClear: () -> Unit,
    onEnter: () -> Unit,
    modifier: Modifier = Modifier,
    inputValidation: com.sin28x.chesssignature.model.ValidationResult = com.sin28x.chesssignature.model.ValidationResult.Incomplete
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
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "Клавиатура:",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 2.dp)
            )
            
            // Первый ряд: Рокировки и взятие
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                SymbolButton(
                    symbol = "0-0",
                    onClick = { onKeyPressed("0-0") },
                    modifier = Modifier.weight(1f)
                )
                SymbolButton(
                    symbol = "0-0-0",
                    onClick = { onKeyPressed("0-0-0") },
                    modifier = Modifier.weight(1f)
                )
                SymbolButton(
                    symbol = "x",
                    onClick = { onKeyPressed("x") },
                    modifier = Modifier.weight(1f)
                )
            }
            
            // Второй ряд: Управление
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Backspace
                DeleteButton(
                    onClick = onBackspace,
                    enabled = true,
                    modifier = Modifier.weight(1f)
                )
                
                // Очистить
                ActionButton(
                    text = "Очистить",
                    onClick = onClear,
                    enabled = true,
                    modifier = Modifier.weight(1f)
                )
                
                // Применить (добавить ход)
                ActionButton(
                    text = "Применить",
                    onClick = onEnter,
                    enabled = inputValidation is com.sin28x.chesssignature.model.ValidationResult.Valid,
                    primary = true,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}