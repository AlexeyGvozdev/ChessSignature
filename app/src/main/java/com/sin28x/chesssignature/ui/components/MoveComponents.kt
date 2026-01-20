package com.sin28x.chesssignature.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sin28x.chesssignature.model.Move
import com.sin28x.chesssignature.model.PieceColor
import com.sin28x.chesssignature.ui.theme.ChessTypography

/**
 * Компонент для отображения одного хода.
 * Показывает номер хода и нотацию.
 *
 * @param moveNumber Номер хода (1, 2, 3...)
 * @param move Объект хода
 * @param onClick Обработчик нажатия (опционально)
 * @param modifier Модификатор для кастомизации
 * @param isSelected Выбран ли ход
 */
@Composable
fun MoveItem(
    moveNumber: Int,
    move: Move,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false
) {
    val backgroundColor = when {
        isSelected -> MaterialTheme.colorScheme.primaryContainer
        else -> MaterialTheme.colorScheme.surface
    }
    
    val textColor = when {
        isSelected -> MaterialTheme.colorScheme.onPrimaryContainer
        else -> MaterialTheme.colorScheme.onSurface
    }
    
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .then(
                if (onClick != null) {
                    Modifier.clickable(onClick = onClick)
                } else {
                    Modifier
                }
            )
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Номер хода
        Text(
            text = "$moveNumber.",
            style = ChessTypography.moveNumber,
            color = textColor.copy(alpha = 0.6f)
        )
        
        // Нотация хода
        Text(
            text = move.toRussianNotation(),
            style = ChessTypography.moveNotation,
            color = textColor,
            modifier = Modifier.weight(1f)
        )
    }
}

/**
 * Компонент для отображения пары ходов (белые и черные).
 * Используется для отображения полного хода в шахматной нотации.
 *
 * @param moveNumber Номер хода
 * @param whiteMove Ход белых
 * @param blackMove Ход черных (может быть null)
 * @param onWhiteClick Обработчик нажатия на ход белых
 * @param onBlackClick Обработчик нажатия на ход черных
 * @param modifier Модификатор для кастомизации
 * @param selectedColor Цвет выбранного хода (null если ничего не выбрано)
 */
@Composable
fun MovePairItem(
    moveNumber: Int,
    whiteMove: Move,
    blackMove: Move? = null,
    onWhiteClick: (() -> Unit)? = null,
    onBlackClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    selectedColor: PieceColor? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Номер хода
        Text(
            text = "$moveNumber.",
            style = ChessTypography.moveNumber,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            modifier = Modifier.padding(end = 4.dp)
        )
        
        // Ход белых
        MoveChip(
            move = whiteMove,
            onClick = onWhiteClick,
            isSelected = selectedColor == PieceColor.WHITE,
            modifier = Modifier.weight(1f)
        )
        
        // Ход черных
        if (blackMove != null) {
            MoveChip(
                move = blackMove,
                onClick = onBlackClick,
                isSelected = selectedColor == PieceColor.BLACK,
                modifier = Modifier.weight(1f)
            )
        } else {
            // Пустое место для симметрии
            Text(
                text = "",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

/**
 * Компактный чип для отображения хода.
 * Используется в MovePairItem.
 *
 * @param move Объект хода
 * @param onClick Обработчик нажатия
 * @param isSelected Выбран ли ход
 * @param modifier Модификатор для кастомизации
 */
@Composable
fun MoveChip(
    move: Move,
    onClick: (() -> Unit)? = null,
    isSelected: Boolean = false,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when {
        isSelected -> MaterialTheme.colorScheme.primaryContainer
        else -> MaterialTheme.colorScheme.surfaceVariant
    }
    
    val textColor = when {
        isSelected -> MaterialTheme.colorScheme.onPrimaryContainer
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }
    
    Text(
        text = move.toRussianNotation(),
        style = ChessTypography.moveNotation,
        color = textColor,
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(backgroundColor)
            .then(
                if (onClick != null) {
                    Modifier.clickable(onClick = onClick)
                } else {
                    Modifier
                }
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)
    )
}

/**
 * Карточка с информацией о текущей позиции.
 * Показывает количество ходов и другую статистику.
 *
 * @param totalMoves Общее количество ходов
 * @param modifier Модификатор для кастомизации
 */
@Composable
fun PositionInfoCard(
    totalMoves: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Информация о позиции",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Bold
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Всего ходов:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = totalMoves.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Ход:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = if (totalMoves % 2 == 0) "Белые" else "Черные",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}