package com.sin28x.chesssignature.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sin28x.chesssignature.model.*
import com.sin28x.chesssignature.ui.components.*
import com.sin28x.chesssignature.ui.theme.ChessSignatureTheme
import com.sin28x.chesssignature.viewmodel.GameViewModel
import kotlinx.coroutines.launch

/**
 * Главный экран приложения
 * Интегрирует все компоненты для ввода и отображения шахматных ходов
 */
@Composable
fun MainScreen(
    viewModel: GameViewModel = viewModel()
) {
    // Собираем состояние из ViewModel
    val gameState by viewModel.gameState.collectAsState()
    
    // Состояние для Snackbar
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    
    ChessSignatureTheme {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // 1. Переключатель первого хода (Белые/Черные)
                ColorSelector(
                    selectedColor = gameState.startingColor,
                    onColorSelected = { color ->
                        viewModel.setStartingColor(color)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                
                // 2. Селектор фигур (Пешка, Конь, Слон, Ладья, Ферзь, Король)
                PieceSelector(
                    selectedPiece = gameState.selectedPiece,
                    onPieceSelected = { piece ->
                        viewModel.selectPiece(piece)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                
                // 3. Сетка клеток 8x8 (a8-h1)
                SquaresGrid(
                    onSquareClick = { square ->
                        val chessSquare = ChessSquare.fromString(square)
                        if (chessSquare != null) {
                            viewModel.addSquare(chessSquare)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                )
                
                // 4. Клавиатура (рокировки и управление)
                // Ходы добавляются автоматически при достижении валидного состояния
                ChessKeyboard(
                    onKeyPressed = { viewModel.addCharacter(it) },
                    onBackspace = { viewModel.deleteLastCharacter() },
                    onClear = { viewModel.clearCurrentInput() },
                    modifier = Modifier.fillMaxWidth()
                )
                
                // 5. Список ходов с кнопками управления
                val context = LocalContext.current
                MovesListWithCopy(
                    moves = gameState.moves,
                    onCopyClick = {
                        val text = gameState.getMovesAsRussianNotation()
                        if (text.isNotEmpty()) {
                            copyNotationToClipboard(
                                context = context,
                                notation = text
                            )
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "Нотация скопирована в буфер обмена"
                                )
                            }
                        }
                    },
                    onDeleteLastClick = {
                        viewModel.deleteLastMove()
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Последний ход удален"
                            )
                        }
                    },
                    onClearAllClick = {
                        viewModel.clearAllMoves()
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Все ходы очищены"
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }
        }
    }
}

/**
 * Компонент для выбора цвета первого хода
 */
@Composable
fun ColorSelector(
    selectedColor: PieceColor,
    onColorSelected: (PieceColor) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Первый ход:",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Кнопка "Белые"
            FilterChip(
                selected = selectedColor == PieceColor.WHITE,
                onClick = { onColorSelected(PieceColor.WHITE) },
                label = { Text("⚪ Белые") },
                modifier = Modifier.weight(1f)
            )
            
            // Кнопка "Черные"
            FilterChip(
                selected = selectedColor == PieceColor.BLACK,
                onClick = { onColorSelected(PieceColor.BLACK) },
                label = { Text("⚫ Черные") },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

/**
 * Компонент для выбора типа фигуры
 */
@Composable
fun PieceSelector(
    selectedPiece: PieceType,
    onPieceSelected: (PieceType) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Фигура:",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        // Первый ряд: Пешка, Конь, Слон
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PieceType.values().take(3).forEach { piece ->
                FilterChip(
                    selected = selectedPiece == piece,
                    onClick = { onPieceSelected(piece) },
                    label = { Text(piece.toDisplayString()) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
        
        // Второй ряд: Ладья, Ферзь, Король
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PieceType.values().drop(3).forEach { piece ->
                FilterChip(
                    selected = selectedPiece == piece,
                    onClick = { onPieceSelected(piece) },
                    label = { Text(piece.toDisplayString()) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

/**
 * Компонент для отображения сетки клеток 8x8
 */
@Composable
fun SquaresGrid(
    onSquareClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        // Доска от 8 ранга до 1 (сверху вниз)
        for (rank in 8 downTo 1) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                // Вертикали от a до h (слева направо)
                for (file in 'a'..'h') {
                    val square = "$file$rank"
                    val isLightSquare = (file - 'a' + rank) % 2 == 0
                    
                    SquareButton(
                        square = square,
                        isLight = isLightSquare,
                        onClick = { onSquareClick(square) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

/**
 * Компонент для отображения кнопки клетки
 */
@Composable
private fun SquareButton(
    square: String,
    isLight: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxSize()
            .aspectRatio(1f),
        shape = MaterialTheme.shapes.extraSmall, // Квадратная форма с минимальным скруглением
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isLight) {
                Color(0xFFF0D9B5) // Светлая клетка (бежевый)
            } else {
                Color(0xFFB58863) // Темная клетка (коричневый)
            },
            contentColor = if (isLight) {
                Color(0xFF000000) // Черный текст на светлом
            } else {
                Color(0xFFFFFFFF) // Белый текст на темном
            }
        ),
        contentPadding = PaddingValues(2.dp)
    ) {
        Text(
            text = square,
            style = MaterialTheme.typography.labelSmall,
            maxLines = 1
        )
    }
}

/**
 * Компонент для отображения текущего хода
 */
@Composable
fun CurrentMoveDisplay(
    currentInput: String,
    validation: ValidationResult,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = when (validation) {
            is ValidationResult.Valid -> MaterialTheme.colorScheme.surfaceVariant
            is ValidationResult.Incomplete -> Color(0xFFFFF9C4) // Светло-желтый
            is ValidationResult.Invalid -> Color(0xFFFFCDD2) // Светло-красный
        },
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Текущий ход:",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                // Индикатор валидации
                Text(
                    text = when (validation) {
                        is ValidationResult.Valid -> "✅"
                        is ValidationResult.Incomplete -> "⚠️"
                        is ValidationResult.Invalid -> "❌"
                    },
                    style = MaterialTheme.typography.titleMedium
                )
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = currentInput.ifEmpty { "Введите ход..." },
                style = MaterialTheme.typography.titleLarge,
                color = if (currentInput.isEmpty()) {
                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
            
            // Сообщение об ошибке
            if (validation is ValidationResult.Invalid) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = validation.message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

/**
 * Компонент для отображения списка ходов с кнопками управления
 */
@Composable
fun MovesListWithCopy(
    moves: List<Move>,
    onCopyClick: () -> Unit,
    onDeleteLastClick: () -> Unit,
    onClearAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Заголовок
        Text(
            text = "Список ходов:",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        // Кнопки управления
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Кнопка "Копировать"
            Button(
                onClick = onCopyClick,
                enabled = moves.isNotEmpty(),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "📋",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Копировать")
            }
            
            // Кнопка "Удалить последний"
            OutlinedButton(
                onClick = onDeleteLastClick,
                enabled = moves.isNotEmpty(),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "🗑️",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Удалить")
            }
            
            // Кнопка "Очистить всё"
            OutlinedButton(
                onClick = onClearAllClick,
                enabled = moves.isNotEmpty(),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "🧹",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Очистить")
            }
        }
        
        // Список ходов
        if (moves.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Ходы появятся здесь",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            }
        } else {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.medium
            ) {
                LazyColumn(
                    modifier = Modifier.padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Группируем ходы по номерам
                    val groupedMoves = moves.groupBy { it.moveNumber }
                    items(groupedMoves.keys.sorted()) { moveNumber ->
                        val movesForNumber = groupedMoves[moveNumber] ?: emptyList()
                        val whiteMove = movesForNumber.find { it.color == PieceColor.WHITE }
                        val blackMove = movesForNumber.find { it.color == PieceColor.BLACK }
                        
                        MovePairItemDisplay(
                            moveNumber = moveNumber,
                            whiteMove = whiteMove,
                            blackMove = blackMove
                        )
                    }
                }
            }
        }
    }
}

/**
 * Компонент для отображения пары ходов (белые и черные) одного номера
 */
@Composable
private fun MovePairItemDisplay(
    moveNumber: Int,
    whiteMove: Move?,
    blackMove: Move?
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Номер хода
        Text(
            text = "$moveNumber.",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(40.dp)
        )
        
        // Ход белых
        Text(
            text = whiteMove?.toRussianNotation() ?: "...",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        
        // Ход черных
        Text(
            text = blackMove?.toRussianNotation() ?: "",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
    }
}

/**
 * Вспомогательная функция для копирования нотации в буфер обмена
 */
private fun copyNotationToClipboard(
    context: android.content.Context,
    notation: String
) {
    val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
    val clip = android.content.ClipData.newPlainText("Chess Notation", notation)
    clipboard.setPrimaryClip(clip)
}