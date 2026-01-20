# ChessSignature - Финальная архитектура

## 📋 Обзор проекта

**ChessSignature** - кроссплатформенное приложение (Android + iOS) для быстрого ввода шахматных ходов с визуальной сеткой клеток для выбора.

### ✨ Ключевые возможности

- ✅ **Визуальная сетка 8x8** - кнопки с названиями клеток (a1, b2, c4...) как на шахматной доске
- ✅ **Расширенная клавиатура** - фигуры + специальные символы
- ✅ **Быстрый ввод** - клик по клетке добавляет её название к ходу
- ✅ **Отображение списка ходов** в стандартном формате
- ✅ **Копирование в буфер** одной кнопкой
- ✅ **Compose Multiplatform** - ~95% общего кода
- ✅ **Простота** - без валидации, фокус на скорости ввода

---

## 🎨 UI Дизайн

### Главный экран

```
┌─────────────────────────────────────┐
│  ChessSignature      [🗑️] [📋]     │
├─────────────────────────────────────┤
│  Первый ход: [⚪ Белые] [⚫ Черные] │
├─────────────────────────────────────┤
│  Сетка клеток (8x8):                │
│  ┌───────────────────────────────┐ │
│  │ ┌──┬──┬──┬──┬──┬──┬──┬──┐    │ │
│  │ │a8│b8│c8│d8│e8│f8│g8│h8│    │ │
│  │ ├──┼──┼──┼──┼──┼──┼──┼──┤    │ │
│  │ │a7│b7│c7│d7│e7│f7│g7│h7│    │ │
│  │ ├──┼──┼──┼──┼──┼──┼──┼──┤    │ │
│  │ │a6│b6│c6│d6│e6│f6│g6│h6│    │ │
│  │ ├──┼──┼──┼──┼──┼──┼──┼──┤    │ │
│  │ │a5│b5│c5│d5│e5│f5│g5│h5│    │ │
│  │ ├──┼──┼──┼──┼──┼──┼──┼──┤    │ │
│  │ │a4│b4│c4│d4│e4│f4│g4│h4│    │ │
│  │ ├──┼──┼──┼──┼──┼──┼──┼──┤    │ │
│  │ │a3│b3│c3│d3│e3│f3│g3│h3│    │ │
│  │ ├──┼──┼──┼──┼──┼──┼──┼──┤    │ │
│  │ │a2│b2│c2│d2│e2│f2│g2│h2│    │ │
│  │ ├──┼──┼──┼──┼──┼──┼──┼──┤    │ │
│  │ │a1│b1│c1│d1│e1│f1│g1│h1│    │ │
│  │ └──┴──┴──┴──┴──┴──┴──┴──┘    │ │
│  └───────────────────────────────┘ │
│  (клетки чередуются светлые/темные)│
├─────────────────────────────────────┤
│  Список ходов:                      │
│  1. e4      e5                      │
│  2. Nf3     Nc6                     │
│  3. Bb5     _                       │
├─────────────────────────────────────┤
│  Текущий ввод: [Bb5_]              │
├─────────────────────────────────────┤
│  Фигура: (•) Пешка  ( ) Конь       │
│          ( ) Слон   ( ) Ладья      │
│          ( ) Ферзь  ( ) Король     │
├─────────────────────────────────────┤
│  Клавиатура:                        │
│  Специальные: [x][+][#][=]         │
│  Рокировки: [O-O][O-O-O]           │
│  Управление: [←][✓]                 │
└─────────────────────────────────────┘
```

---

## 🏗️ Архитектура

### Структура компонентов

```
┌─────────────────────────────────────┐
│         UI Layer (Compose)          │
│  • GameScreen                       │
│  • SquaresGrid (8x8 сетка)         │
│  • MovesList                        │
│  • ChessKeyboard                    │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│      Business Logic                 │
│  • GameViewModel                    │
│  • Models (Move, GameState)         │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│      Platform Layer                 │
│  • ClipboardManager (expect/actual) │
└─────────────────────────────────────┘
```

---

## 📦 Модели данных

### Move.kt

```kotlin
package com.sin28x.chesssignature.model

data class Move(
    val moveNumber: Int,
    val whiteMove: String,
    val blackMove: String? = null
)
```

### GameState.kt

```kotlin
package com.sin28x.chesssignature.model

data class GameState(
    val moves: List<Move> = emptyList(),
    val currentInput: String = "",
    val isWhiteTurn: Boolean = true,
    val startingColor: PieceColor = PieceColor.WHITE,
    val selectedPiece: PieceType = PieceType.PAWN
) {
    fun getMovesAsText(): String {
        return moves.joinToString(" ") { move ->
            if (move.blackMove != null) {
                "${move.moveNumber}. ${move.whiteMove} ${move.blackMove}"
            } else {
                "${move.moveNumber}. ${move.whiteMove}"
            }
        }
    }
}

enum class PieceColor {
    WHITE, BLACK;
    
    fun toDisplayString(): String = when (this) {
        WHITE -> "Белые"
        BLACK -> "Черные"
    }
}

enum class PieceType {
    PAWN, KNIGHT, BISHOP, ROOK, QUEEN, KING;
    
    fun toNotation(): String = when (this) {
        PAWN -> ""
        KNIGHT -> "N"
        BISHOP -> "B"
        ROOK -> "R"
        QUEEN -> "Q"
        KING -> "K"
    }
    
    fun toDisplayString(): String = when (this) {
        PAWN -> "Пешка"
        KNIGHT -> "Конь"
        BISHOP -> "Слон"
        ROOK -> "Ладья"
        QUEEN -> "Ферзь"
        KING -> "Король"
    }
}
```

---

## 🎮 GameViewModel

```kotlin
package com.sin28x.chesssignature.viewmodel

import com.sin28x.chesssignature.model.GameState
import com.sin28x.chesssignature.model.Move
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameViewModel {
    private val _state = MutableStateFlow(GameState())
    val state: StateFlow<GameState> = _state.asStateFlow()
    
    fun selectPiece(piece: PieceType) {
        _state.update { it.copy(selectedPiece = piece) }
    }
    
    fun addSquare(square: String) {
        val currentState = _state.value
        val pieceNotation = currentState.selectedPiece.toNotation()
        val addition = pieceNotation + square
        _state.update { it.copy(currentInput = it.currentInput + addition) }
    }
    
    fun addCharacter(char: String) {
        _state.update { it.copy(currentInput = it.currentInput + char) }
    }
    
    fun deleteLastCharacter() {
        _state.update { it.copy(currentInput = it.currentInput.dropLast(1)) }
    }
    
    fun clearCurrentInput() {
        _state.update { it.copy(currentInput = "") }
    }
    
    fun submitMove() {
        val currentState = _state.value
        val notation = currentState.currentInput.trim()
        
        if (notation.isEmpty()) return
        
        val newMoves = currentState.moves.toMutableList()
        
        if (currentState.isWhiteTurn) {
            val moveNumber = newMoves.size + 1
            newMoves.add(Move(moveNumber, notation, null))
        } else {
            val lastMove = newMoves.lastOrNull()
            if (lastMove != null) {
                newMoves[newMoves.lastIndex] = lastMove.copy(blackMove = notation)
            }
        }
        
        _state.update {
            GameState(
                moves = newMoves,
                currentInput = "",
                isWhiteTurn = !currentState.isWhiteTurn
            )
        }
    }
    
    fun setStartingColor(color: PieceColor) {
        _state.update { it.copy(
            startingColor = color,
            isWhiteTurn = (color == PieceColor.WHITE)
        )}
    }
    
    fun clearAllMoves() {
        val currentColor = _state.value.startingColor
        _state.value = GameState(startingColor = currentColor)
    }
    
    fun getMovesText(): String {
        return _state.value.getMovesAsText()
    }
}
```

---

## 🎨 SquaresGrid - Сетка клеток 8x8

```kotlin
package com.sin28x.chesssignature.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
            fontSize = 12.sp,
            maxLines = 1
        )
    }
}
```

---

## 🎨 PieceSelector - Селектор фигур

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
    Column(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Фигура:",
            style = MaterialTheme.typography.labelLarge
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

## 🎹 ChessKeyboard (обновленная)

```kotlin
package com.sin28x.chesssignature.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ChessKeyboard(
    onKeyPressed: (String) -> Unit,
    onBackspace: () -> Unit,
    onEnter: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Ряд 1: Специальные символы
        Text(
            text = "Специальные:",
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(start = 4.dp, top = 4.dp)
        )
        KeyRow(
            keys = listOf("x", "+", "#", "="),
            onKeyPressed = onKeyPressed
        )
        
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
        Text(
            text = "Управление:",
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(start = 4.dp, top = 4.dp)
        )
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
                text = "✓",
                onClick = onEnter,
                modifier = Modifier.weight(2f),
                isPrimary = true
            )
        }
    }
}

@Composable
private fun KeyRow(
    keys: List<String>,
    onKeyPressed: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        keys.forEach { key ->
            KeyButton(
                text = key,
                onClick = { onKeyPressed(key) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun KeyButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isPrimary: Boolean = false
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(48.dp),
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

## 📱 GameScreen

```kotlin
package com.sin28x.chesssignature.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sin28x.chesssignature.platform.ClipboardManager
import com.sin28x.chesssignature.viewmodel.GameViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen() {
    val viewModel = remember { GameViewModel() }
    val state by viewModel.state.collectAsState()
    val clipboardManager = remember { ClipboardManager() }
    
    var showClearDialog by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ChessSignature") },
                actions = {
                    IconButton(onClick = { showClearDialog = true }) {
                        Icon(Icons.Default.Clear, contentDescription = "Очистить")
                    }
                    IconButton(onClick = {
                        val text = viewModel.getMovesText()
                        if (text.isNotEmpty()) {
                            clipboardManager.copyToClipboard(text)
                        }
                    }) {
                        Icon(Icons.Default.ContentCopy, contentDescription = "Копировать")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Переключатель цвета
            ColorSelector(
                selectedColor = state.startingColor,
                onColorSelected = { color ->
                    viewModel.setStartingColor(color)
                },
                modifier = Modifier.fillMaxWidth()
            )
            
            // Сетка клеток 8x8
            SquaresGrid(
                onSquareClick = { square ->
                    viewModel.addSquare(square)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            
            // Список ходов
            MovesList(
                moves = state.moves,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.5f)
            )
            
            // Текущий ввод
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Text(
                    text = state.currentInput.ifEmpty { "Введите ход..." },
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleLarge,
                    color = if (state.currentInput.isEmpty()) {
                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }
            
            // Селектор фигур
            PieceSelector(
                selectedPiece = state.selectedPiece,
                onPieceSelected = { piece ->
                    viewModel.selectPiece(piece)
                },
                modifier = Modifier.fillMaxWidth()
            )
            
            // Клавиатура
            ChessKeyboard(
                onKeyPressed = { viewModel.addCharacter(it) },
                onBackspace = { viewModel.deleteLastCharacter() },
                onEnter = { viewModel.submitMove() },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
    
    // Диалог подтверждения очистки
    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text("Очистить все ходы?") },
            text = { Text("Это действие нельзя отменить") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.clearAllMoves()
                    showClearDialog = false
                }) {
                    Text("Очистить")
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) {
                    Text("Отмена")
                }
            }
        )
    }
}

@Composable
private fun ColorSelector(
    selectedColor: PieceColor,
    onColorSelected: (PieceColor) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Первый ход:",
            modifier = Modifier.align(Alignment.CenterVertically),
            style = MaterialTheme.typography.labelLarge
        )
        
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
```

---

## 💡 Примеры использования

### Пример 1: Ввод хода e4

```
Пользователь:
1. Кликает на кнопку [e4] в сетке
2. Текущий ввод: "e4"
3. Нажимает [✓]

Результат: "1. e4"
```

### Пример 2: Ввод хода Nf3

```
Пользователь:
1. Выбирает фигуру "Конь" (радио-кнопка)
2. Кликает на кнопку [f3] в сетке
3. Текущий ввод: "Nf3" (автоматически)
4. Нажимает [✓]

Результат: "1. Nf3"
```

### Пример 3: Ввод взятия Bxf7+

```
Пользователь:
1. Выбирает фигуру "Слон"
2. Нажимает [x]
3. Кликает на [f7] в сетке
4. Нажимает [+]
5. Текущий ввод: "Bxf7+"
6. Нажимает [✓]

Результат: "1. Bxf7+"
```

### Пример 4: Полная партия

```
Ввод:
Пешка: [e4] → ✓ → [e5] → ✓
Конь: [f3] → ✓ → [c6] → ✓
Слон: [b5] → ✓ → Пешка: [a6] → ✓

Результат:
1. e4 e5
2. Nf3 Nc6
3. Bb5 a6

Копирование: "1. e4 e5 2. Nf3 Nc6 3. Bb5 a6"
```

---

## 📂 Структура проекта

```
ChessSignature/
├── composeApp/
│   └── src/
│       ├── commonMain/kotlin/com/sin28x/chesssignature/
│       │   ├── App.kt
│       │   ├── model/
│       │   │   ├── Move.kt
│       │   │   └── GameState.kt
│       │   ├── viewmodel/
│       │   │   └── GameViewModel.kt
│       │   ├── ui/
│       │   │   ├── GameScreen.kt
│       │   │   ├── SquaresGrid.kt      # NEW - сетка 8x8
│       │   │   ├── ChessKeyboard.kt
│       │   │   ├── MovesList.kt
│       │   │   └── theme/
│       │   │       ├── Color.kt
│       │   │       └── Theme.kt
│       │   └── platform/
│       │       └── ClipboardManager.kt (expect)
│       ├── androidMain/kotlin/
│       │   └── com/sin28x/chesssignature/platform/
│       │       └── ClipboardManager.android.kt
│       └── iosMain/kotlin/
│           └── com/sin28x/chesssignature/platform/
│               └── ClipboardManager.ios.kt
├── androidApp/
│   └── src/main/kotlin/
│       └── com/sin28x/chesssignature/
│           └── MainActivity.kt
└── iosApp/
    └── iosApp/
        └── ContentView.swift
```

---

## 🎨 Цветовая схема сетки

### Светлые клетки
- Цвет: `#F0D9B5` (бежевый)
- Текст: черный `#000000`

### Темные клетки
- Цвет: `#B58863` (коричневый)
- Текст: белый `#FFFFFF`

### Чередование
Клетка светлая, если `(file - 'a' + rank) % 2 == 0`

---

## 🚀 План разработки

### Неделя 1: Основа (3-4 дня)
- [ ] Настройка Compose Multiplatform проекта
- [ ] Создание моделей (Move, GameState)
- [ ] Реализация GameViewModel
- [ ] Базовая тема

### Неделя 2: UI (4-5 дней)
- [ ] Создание SquaresGrid (сетка 8x8)
- [ ] Создание ChessKeyboard
- [ ] Создание MovesList
- [ ] Создание GameScreen
- [ ] Интеграция компонентов

### Неделя 3: Платформы (3-4 дня)
- [ ] Настройка Android entry point
- [ ] Настройка iOS entry point
- [ ] Реализация ClipboardManager (expect/actual)
- [ ] Тестирование на обеих платформах

### Неделя 4: Полировка (2-3 дня)
- [ ] Улучшение UI/UX
- [ ] Оптимизация размеров кнопок
- [ ] Исправление багов
- [ ] Подготовка к релизу

**Общее время: 3-4 недели**

---

## 📊 Преимущества архитектуры

### ✅ Визуальная сетка
- Наглядное представление клеток
- Быстрый выбор любой клетки
- Привычный вид шахматной доски

### ✅ Удобный ввод
- Выбор фигуры через селектор (радио-кнопки)
- Клик по сетке для выбора клетки
- Автоматическая сборка нотации (фигура + клетка)
- Специальные символы на клавиатуре

### ✅ Простота
- Минимум компонентов
- Понятная структура
- Легко поддерживать

### ✅ Кроссплатформенность
- ~95% общего кода
- Единый UI на Compose
- Нативная производительность

---

## 📝 Итоговые характеристики

### Размер приложений
- **Android**: ~5-7 МБ
- **iOS**: ~8-10 МБ

### Поддерживаемые версии
- **Android**: 7.0+ (API 24+)
- **iOS**: 14.0+

### Производительность
- Мгновенный отклик
- Плавная работа сетки 8x8
- Минимальное потребление памяти

### Общий код
- **UI**: 100% (Compose Multiplatform)
- **Логика**: 100% (Kotlin)
- **Платформенный код**: <5% (только ClipboardManager)

---

## 🎯 Готово к разработке!

Архитектура полностью спроектирована с визуальной сеткой клеток 8x8, как на примере. Все компоненты определены, зависимости указаны, план разработки составлен.

**Следующий шаг**: Переключиться в режим Code для начала имплементации! 🚀
## ✅ Валидация ввода ходов

### Уровни валидации

#### 1. Базовая валидация формата (обязательная)

Проверяет корректность синтаксиса нотации перед добавлением хода:

```kotlin
class MoveValidator {
    fun validateNotation(notation: String): ValidationResult {
        if (notation.isBlank()) {
            return ValidationResult.Error("Ход не может быть пустым")
        }
        
        // Проверка рокировки
        if (notation == "O-O" || notation == "O-O-O") {
            return ValidationResult.Valid
        }
        
        // Проверка формата хода
        val movePattern = Regex("^[KQRBN]?[a-h]?[1-8]?x?[a-h][1-8][+#]?(=[QRBN])?$")
        
        return if (movePattern.matches(notation)) {
            ValidationResult.Valid
        } else {
            ValidationResult.Error("Неверный формат хода: $notation")
        }
    }
}

sealed class ValidationResult {
    object Valid : ValidationResult()
    data class Error(val message: String) : ValidationResult()
    data class Warning(val message: String) : ValidationResult()
}
```

#### 2. Визуальная обратная связь

**Индикация состояния ввода:**

```kotlin
data class GameState(
    // ... существующие поля
    val inputValidation: ValidationResult = ValidationResult.Valid
)
```

**UI индикация:**
- ✅ **Зеленая рамка** - ход валиден
- ⚠️ **Желтая рамка** - предупреждение (необычный ход)
- ❌ **Красная рамка** - ошибка в формате

```kotlin
// В GameScreen
Surface(
    modifier = Modifier.fillMaxWidth(),
    color = when (state.inputValidation) {
        is ValidationResult.Valid -> MaterialTheme.colorScheme.surfaceVariant
        is ValidationResult.Warning -> Color(0xFFFFF9C4) // Светло-желтый
        is ValidationResult.Error -> Color(0xFFFFCDD2) // Светло-красный
    }
) {
    Column {
        Text(
            text = state.currentInput.ifEmpty { "Введите ход..." },
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.titleLarge
        )
        
        // Сообщение об ошибке
        if (state.inputValidation is ValidationResult.Error) {
            Text(
                text = state.inputValidation.message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }
    }
}
```

#### 3. Валидация в реальном времени

Проверка при каждом изменении ввода:

```kotlin
class GameViewModel {
    private val validator = MoveValidator()
    
    fun addSquare(square: String) {
        val currentState = _state.value
        val pieceNotation = currentState.selectedPiece.toNotation()
        val newInput = currentState.currentInput + pieceNotation + square
        
        // Валидация в реальном времени
        val validation = validator.validateNotation(newInput)
        
        _state.update { it.copy(
            currentInput = newInput,
            inputValidation = validation
        )}
    }
    
    fun addCharacter(char: String) {
        val newInput = _state.value.currentInput + char
        val validation = validator.validateNotation(newInput)
        
        _state.update { it.copy(
            currentInput = newInput,
            inputValidation = validation
        )}
    }
}
```

#### 4. Блокировка некорректных ходов

Кнопка подтверждения (✓) активна только при валидном ходе:

```kotlin
// В ChessKeyboard
KeyButton(
    text = "✓",
    onClick = onEnter,
    modifier = Modifier.weight(2f),
    isPrimary = true,
    enabled = inputValidation is ValidationResult.Valid // Новый параметр
)
```

### Примеры валидации

#### ✅ Валидные ходы:
- `e4` - пешка на e4
- `Nf3` - конь на f3
- `Bxf7+` - слон берет на f7 с шахом
- `O-O` - короткая рокировка
- `e8=Q` - превращение пешки в ферзя
- `Qh5#` - ферзь на h5, мат

#### ❌ Невалидные ходы:
- `e9` - несуществующая горизонталь
- `i4` - несуществующая вертикаль
- `Nf` - неполный ход
- `ee4` - двойная вертикаль
- `N3` - нет вертикали

#### ⚠️ Предупреждения (опционально):
- `Qa1` - ферзь на край доски (необычно)
- `Kh1` - король ходит в угол (редко)

### Расширенная валидация (опционально, для v2.0)

Для более строгой проверки можно добавить:

```kotlin
class AdvancedMoveValidator(private val board: ChessBoard) {
    fun validateMove(notation: String, currentTurn: PieceColor): ValidationResult {
        // 1. Базовая валидация формата
        val basicValidation = MoveValidator().validateNotation(notation)
        if (basicValidation !is ValidationResult.Valid) {
            return basicValidation
        }
        
        // 2. Проверка, что фигура может сделать такой ход
        val move = parseNotation(notation)
        if (!isPieceMoveValid(move, board, currentTurn)) {
            return ValidationResult.Error("Фигура не может сделать такой ход")
        }
        
        // 3. Проверка, что клетка назначения доступна
        if (!isSquareAccessible(move.to, board, currentTurn)) {
            return ValidationResult.Error("Клетка занята своей фигурой")
        }
        
        // 4. Проверка шаха королю после хода
        if (wouldExposeKingToCheck(move, board, currentTurn)) {
            return ValidationResult.Error("Ход подставляет короля под шах")
        }
        
        return ValidationResult.Valid
    }
}
```

### Настройки валидации

Пользователь может выбрать уровень валидации:

```kotlin
enum class ValidationLevel {
    NONE,           // Без валидации
    FORMAT_ONLY,    // Только формат нотации
    BASIC,          // Формат + базовые правила
    STRICT          // Полная валидация с проверкой позиции
}

data class GameState(
    // ... существующие поля
    val validationLevel: ValidationLevel = ValidationLevel.FORMAT_ONLY
)
```

### UI для настроек валидации

```kotlin
@Composable
fun ValidationSettings(
    currentLevel: ValidationLevel,
    onLevelChanged: (ValidationLevel) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Уровень валидации:", style = MaterialTheme.typography.titleMedium)
        
        ValidationLevel.values().forEach { level ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onLevelChanged(level) }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = currentLevel == level,
                    onClick = { onLevelChanged(level) }
                )
                Column(modifier = Modifier.padding(start = 8.dp)) {
                    Text(
                        text = when (level) {
                            ValidationLevel.NONE -> "Без валидации"
                            ValidationLevel.FORMAT_ONLY -> "Только формат"
                            ValidationLevel.BASIC -> "Базовая"
                            ValidationLevel.STRICT -> "Строгая"
                        },
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = when (level) {
                            ValidationLevel.NONE -> "Принимать любой ввод"
                            ValidationLevel.FORMAT_ONLY -> "Проверка синтаксиса нотации"
                            ValidationLevel.BASIC -> "Формат + базовые правила"
                            ValidationLevel.STRICT -> "Полная проверка легальности хода"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
```

---
