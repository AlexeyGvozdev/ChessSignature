# ChessSignature - Финальная структура UI

## 📱 Обновленная структура экрана

```
┌─────────────────────────────────────┐
│                                     │
│  1. Первый ход:                     │
│     [⚪ Белые] [⚫ Черные]          │
│                                     │
├─────────────────────────────────────┤
│                                     │
│  2. Фигура:                         │
│     (•) Пешка  ( ) Конь             │
│     ( ) Слон   ( ) Ладья            │
│     ( ) Ферзь  ( ) Король           │
│                                     │
├─────────────────────────────────────┤
│                                     │
│  3. Сетка клеток (8x8):             │
│     ┌──┬──┬──┬──┬──┬──┬──┬──┐      │
│     │a8│b8│c8│d8│e8│f8│g8│h8│      │
│     ├──┼──┼──┼──┼──┼──┼──┼──┤      │
│     │a7│b7│c7│d7│e7│f7│g7│h7│      │
│     ├──┼──┼──┼──┼──┼──┼──┼──┤      │
│     │a6│b6│c6│d6│e6│f6│g6│h6│      │
│     ├──┼──┼──┼──┼──┼──┼──┼──┤      │
│     │a5│b5│c5│d5│e5│f5│g5│h5│      │
│     ├──┼──┼──┼──┼──┼──┼──┼──┤      │
│     │a4│b4│c4│d4│e4│f4│g4│h4│      │
│     ├──┼──┼──┼──┼──┼──┼──┼──┤      │
│     │a3│b3│c3│d3│e3│f3│g3│h3│      │
│     ├──┼──┼──┼──┼──┼──┼──┼──┤      │
│     │a2│b2│c2│d2│e2│f2│g2│h2│      │
│     ├──┼──┼──┼──┼──┼──┼──┼──┤      │
│     │a1│b1│c1│d1│e1│f1│g1│h1│      │
│     └──┴──┴──┴──┴──┴──┴──┴──┘      │
│                                     │
├─────────────────────────────────────┤
│                                     │
│  4. Клавиатура:                     │
│     Специальные: [x][+][#][=]      │
│     Рокировки: [O-O][O-O-O]        │
│     Управление: [←][Очистить][✓]   │
│                                     │
├─────────────────────────────────────┤
│                                     │
│  5. Текущий ход: [Nf3_] ✅         │
│                                     │
├─────────────────────────────────────┤
│                                     │
│  6. Список ходов:  [📋 Копировать] │
│     1. e4    e5                     │
│     2. Nf3   Nc6                    │
│     3. Bb5   a6                     │
│     4. Ba4   _                      │
│                                     │
└─────────────────────────────────────┘
```

---

## 🎨 Обновленный GameScreen

```kotlin
package com.sin28x.chesssignature.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sin28x.chesssignature.platform.ClipboardManager
import com.sin28x.chesssignature.viewmodel.GameViewModel

@Composable
fun GameScreen() {
    val viewModel = remember { GameViewModel() }
    val state by viewModel.state.collectAsState()
    val clipboardManager = remember { ClipboardManager() }
    
    var showToast by remember { mutableStateOf(false) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Переключатель первого хода
        ColorSelector(
            selectedColor = state.startingColor,
            onColorSelected = { color ->
                viewModel.setStartingColor(color)
            },
            modifier = Modifier.fillMaxWidth()
        )
        
        // 2. Селектор фигур
        PieceSelector(
            selectedPiece = state.selectedPiece,
            onPieceSelected = { piece ->
                viewModel.selectPiece(piece)
            },
            modifier = Modifier.fillMaxWidth()
        )
        
        // 3. Сетка клеток 8x8
        SquaresGrid(
            onSquareClick = { square ->
                viewModel.addSquare(square)
            },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
        
        // 4. Клавиатура
        ChessKeyboard(
            onKeyPressed = { viewModel.addCharacter(it) },
            onBackspace = { viewModel.deleteLastCharacter() },
            onClear = { viewModel.clearCurrentInput() },
            onEnter = { viewModel.submitMove() },
            inputValidation = state.inputValidation,
            modifier = Modifier.fillMaxWidth()
        )
        
        // 5. Текущий ход
        CurrentMoveDisplay(
            currentInput = state.currentInput,
            validation = state.inputValidation,
            modifier = Modifier.fillMaxWidth()
        )
        
        // 6. Список ходов с кнопкой копирования
        MovesListWithCopy(
            moves = state.moves,
            onCopyClick = {
                val text = viewModel.getMovesText()
                if (text.isNotEmpty()) {
                    clipboardManager.copyToClipboard(text)
                    showToast = true
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.8f)
        )
    }
    
    // Toast для подтверждения копирования
    if (showToast) {
        LaunchedEffect(Unit) {
            kotlinx.coroutines.delay(2000)
            showToast = false
        }
        
        Snackbar(
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Ходы скопированы в буфер обмена")
        }
    }
}
```

---

## 📋 MovesListWithCopy - Список ходов с кнопкой копирования

```kotlin
package com.sin28x.chesssignature.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sin28x.chesssignature.model.Move

@Composable
fun MovesListWithCopy(
    moves: List<Move>,
    onCopyClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Заголовок с кнопкой копирования
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Список ходов:",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Button(
                onClick = onCopyClick,
                enabled = moves.isNotEmpty(),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = "Копировать",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Копировать")
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
                    items(moves) { move ->
                        MoveItem(move)
                    }
                }
            }
        }
    }
}

@Composable
private fun MoveItem(move: Move) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "${move.moveNumber}.",
            modifier = Modifier.width(40.dp),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = move.whiteMove,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = move.blackMove ?: "",
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
```

---

## 📝 CurrentMoveDisplay - Отображение текущего хода

```kotlin
package com.sin28x.chesssignature.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sin28x.chesssignature.model.ValidationResult

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
            is ValidationResult.Warning -> Color(0xFFFFF9C4) // Светло-желтый
            is ValidationResult.Error -> Color(0xFFFFCDD2) // Светло-красный
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
                        is ValidationResult.Warning -> "⚠️"
                        is ValidationResult.Error -> "❌"
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
            if (validation is ValidationResult.Error) {
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
```

---

## 🎹 Обновленная ChessKeyboard

```kotlin
package com.sin28x.chesssignature.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sin28x.chesssignature.model.ValidationResult

@Composable
fun ChessKeyboard(
    onKeyPressed: (String) -> Unit,
    onBackspace: () -> Unit,
    onClear: () -> Unit,
    onEnter: () -> Unit,
    inputValidation: ValidationResult,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Клавиатура:",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        // Ряд 1: Специальные символы
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            listOf("x", "+", "#", "=").forEach { symbol ->
                KeyButton(
                    text = symbol,
                    onClick = { onKeyPressed(symbol) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
        
        // Ряд 2: Рокировки
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            KeyButton(
                text = "O-O",
                onClick = { onKeyPressed("O-O") },
                modifier = Modifier.weight(1f)
            )
            KeyButton(
                text = "O-O-O",
                onClick = { onKeyPressed("O-O-O") },
                modifier = Modifier.weight(1f)
            )
        }
        
        // Ряд 3: Управление
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            KeyButton(
                text = "←",
                onClick = onBackspace,
                modifier = Modifier.weight(1f)
            )
            KeyButton(
                text = "Очистить",
                onClick = onClear,
                modifier = Modifier.weight(1f)
            )
            KeyButton(
                text = "✓",
                onClick = onEnter,
                modifier = Modifier.weight(1f),
                isPrimary = true,
                enabled = inputValidation is ValidationResult.Valid
            )
        }
    }
}

@Composable
private fun KeyButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isPrimary: Boolean = false,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        enabled = enabled,
        colors = if (isPrimary) {
            ButtonDefaults.buttonColors()
        } else {
            ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        }
    ) {
        Text(text)
    }
}
```

---

## 🎨 ColorSelector (обновленный)

```kotlin
package com.sin28x.chesssignature.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sin28x.chesssignature.model.PieceColor

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
```

---

## 🎨 PieceSelector (обновленный)

```kotlin
package com.sin28x.chesssignature.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sin28x.chesssignature.model.PieceType

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
```

---

## 📊 Итоговая структура компонентов

```
GameScreen
├── 1. ColorSelector (Первый ход)
├── 2. PieceSelector (Фигура)
├── 3. SquaresGrid (Сетка 8x8)
├── 4. ChessKeyboard (Клавиатура)
├── 5. CurrentMoveDisplay (Текущий ход)
└── 6. MovesListWithCopy (Список ходов + кнопка)
```

---

## ✨ Ключевые изменения

1. ✅ **Убран TopAppBar** - нет заголовка "ChessSignature"
2. ✅ **Изменен порядок компонентов** - согласно требованиям
3. ✅ **Добавлена кнопка копирования** к списку ходов
4. ✅ **Добавлен Toast** - "Ходы скопированы в буфер обмена"
5. ✅ **Улучшена визуализация** - каждый раздел с подписью
6. ✅ **Текущий ход** - отдельный компонент с валидацией
7. ✅ **Список ходов** - с заголовком и кнопкой

---

## 🎯 Готово к разработке!

Все компоненты обновлены согласно новым требованиям UI. Структура экрана полностью переработана для максимального удобства использования.