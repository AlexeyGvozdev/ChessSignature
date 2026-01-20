package com.sin28x.chesssignature.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sin28x.chesssignature.model.GameState
import com.sin28x.chesssignature.model.Move

/**
 * Placeholder для пустого списка ходов.
 * Отображается когда ходов еще нет.
 *
 * @param modifier Модификатор для кастомизации
 */
@Composable
fun EmptyMovesPlaceholder(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "♟️",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
            )
            Text(
                text = "Ходов пока нет",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                textAlign = TextAlign.Center
            )
            Text(
                text = "Начните вводить ходы с помощью клавиатуры",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Список ходов с возможностью копирования.
 * Отображает ходы в формате пар (белые-черные).
 *
 * @param moves Список ходов
 * @param modifier Модификатор для кастомизации
 * @param onMoveClick Обработчик нажатия на ход (опционально)
 */
@Composable
fun MovesList(
    moves: List<Move>,
    modifier: Modifier = Modifier,
    onMoveClick: ((Move) -> Unit)? = null
) {
    if (moves.isEmpty()) {
        EmptyMovesPlaceholder(modifier = modifier)
    } else {
        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Группируем ходы по парам (белые-черные)
            val movePairs = moves.chunked(2)
            
            itemsIndexed(movePairs) { index, pair ->
                val moveNumber = index + 1
                val whiteMove = pair[0]
                val blackMove = pair.getOrNull(1)
                
                MovePairItem(
                    moveNumber = moveNumber,
                    whiteMove = whiteMove,
                    blackMove = blackMove,
                    onWhiteClick = onMoveClick?.let { { it(whiteMove) } },
                    onBlackClick = blackMove?.let { move ->
                        onMoveClick?.let { { it(move) } }
                    }
                )
            }
        }
    }
}

/**
 * Карточка со списком ходов и кнопкой копирования.
 * Включает заголовок, список ходов и кнопку копирования нотации.
 *
 * @param gameState Состояние игры с ходами
 * @param modifier Модификатор для кастомизации
 * @param onMoveClick Обработчик нажатия на ход
 * @param onCopyClick Обработчик нажатия на кнопку копирования
 */
@Composable
fun MovesListCard(
    gameState: GameState,
    modifier: Modifier = Modifier,
    onMoveClick: ((Move) -> Unit)? = null,
    onCopyClick: (() -> Unit)? = null
) {
    val context = LocalContext.current
    
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Заголовок с кнопкой копирования
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Список ходов",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                if (gameState.moves.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            copyNotationToClipboard(
                                context = context,
                                notation = gameState.getMovesAsRussianNotation()
                            )
                            onCopyClick?.invoke()
                        }
                    ) {
                        Text(
                            text = "📋",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
            
            // Список ходов
            MovesList(
                moves = gameState.moves,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false),
                onMoveClick = onMoveClick
            )
        }
    }
}

/**
 * Компактный список ходов без карточки.
 * Используется когда нужен только список без дополнительных элементов.
 *
 * @param moves Список ходов
 * @param modifier Модификатор для кастомизации
 * @param showMoveNumbers Показывать ли номера ходов
 */
@Composable
fun CompactMovesList(
    moves: List<Move>,
    modifier: Modifier = Modifier,
    showMoveNumbers: Boolean = true
) {
    if (moves.isEmpty()) {
        EmptyMovesPlaceholder(modifier = modifier)
    } else {
        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            itemsIndexed(moves) { index, move ->
                if (showMoveNumbers) {
                    MoveItem(
                        moveNumber = index + 1,
                        move = move
                    )
                } else {
                    MoveChip(
                        move = move,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

/**
 * Копирует нотацию в буфер обмена.
 *
 * @param context Контекст Android
 * @param notation Нотация для копирования
 */
private fun copyNotationToClipboard(context: Context, notation: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Chess Notation", notation)
    clipboard.setPrimaryClip(clip)
}