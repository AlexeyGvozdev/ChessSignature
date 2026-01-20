# Русская шахматная нотация

## 📝 Обозначения фигур

### Русская нотация (используется в приложении)

| Фигура | Русское обозначение | Английское | Полное название |
|--------|---------------------|------------|-----------------|
| Король | **Кр** | K | Король |
| Ферзь | **Ф** | Q | Ферзь |
| Ладья | **Л** | R | Ладья |
| Слон | **С** | B | Слон |
| Конь | **К** | N | Конь |
| Пешка | *(нет)* | *(нет)* | Пешка |

---

## 🔄 Обновленный PieceType

```kotlin
package com.sin28x.chesssignature.model

enum class PieceType {
    PAWN, KNIGHT, BISHOP, ROOK, QUEEN, KING;
    
    // Русская нотация
    fun toRussianNotation(): String = when (this) {
        PAWN -> ""      // Пешка не обозначается
        KNIGHT -> "К"   // Конь
        BISHOP -> "С"   // Слон
        ROOK -> "Л"     // Ладья
        QUEEN -> "Ф"    // Ферзь
        KING -> "Кр"    // Король
    }
    
    // Английская нотация (для экспорта в PGN)
    fun toEnglishNotation(): String = when (this) {
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
    
    companion object {
        fun fromRussianNotation(notation: String): PieceType? = when (notation) {
            "К" -> KNIGHT
            "С" -> BISHOP
            "Л" -> ROOK
            "Ф" -> QUEEN
            "Кр" -> KING
            "" -> PAWN
            else -> null
        }
    }
}
```

---

## 🎮 Обновленный GameViewModel

```kotlin
class GameViewModel {
    private val _state = MutableStateFlow(GameState())
    val state: StateFlow<GameState> = _state.asStateFlow()
    private val validator = MoveValidator()
    
    fun selectPiece(piece: PieceType) {
        _state.update { it.copy(selectedPiece = piece) }
    }
    
    fun addSquare(square: String) {
        val currentState = _state.value
        // Используем русскую нотацию
        val pieceNotation = currentState.selectedPiece.toRussianNotation()
        val newInput = currentState.currentInput + pieceNotation + square
        
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
    
    // ... остальные методы
}
```

---

## ✅ Обновленный MoveValidator

```kotlin
class MoveValidator {
    fun validateNotation(notation: String): ValidationResult {
        if (notation.isBlank()) {
            return ValidationResult.Error("Ход не может быть пустым")
        }
        
        // Проверка рокировки
        if (notation == "O-O" || notation == "O-O-O" || 
            notation == "0-0" || notation == "0-0-0") {
            return ValidationResult.Valid
        }
        
        // Проверка формата хода с РУССКОЙ нотацией
        // Кр, Ф, Л, С, К - русские обозначения фигур
        val movePattern = Regex("^(Кр|Ф|Л|С|К)?[a-h]?[1-8]?x?[a-h][1-8][+#]?(=(Ф|Л|С|К))?$")
        
        return if (movePattern.matches(notation)) {
            ValidationResult.Valid
        } else {
            ValidationResult.Error("Неверный формат хода: $notation")
        }
    }
}
```

---

## 📋 Примеры ходов с русской нотацией

### ✅ Валидные ходы:

| Ход | Описание |
|-----|----------|
| `e4` | Пешка на e4 |
| `Кf3` | Конь на f3 |
| `Сb5` | Слон на b5 |
| `Лa1` | Ладья на a1 |
| `Фd8` | Ферзь на d8 |
| `Крe1` | Король на e1 |
| `Сxf7+` | Слон берет на f7 с шахом |
| `Фh5#` | Ферзь на h5, мат |
| `e8=Ф` | Превращение пешки в ферзя |
| `O-O` | Короткая рокировка |
| `O-O-O` | Длинная рокировка |

### Сравнение с английской нотацией:

| Русская | Английская | Описание |
|---------|------------|----------|
| `Кf3` | `Nf3` | Конь на f3 |
| `Сb5` | `Bb5` | Слон на b5 |
| `Лa1` | `Ra1` | Ладья на a1 |
| `Фd8` | `Qd8` | Ферзь на d8 |
| `Крe1` | `Ke1` | Король на e1 |
| `e8=Ф` | `e8=Q` | Превращение в ферзя |

---

## 🎨 Обновленная клавиатура

Специальные символы остаются без изменений:

```kotlin
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
            style = MaterialTheme.typography.labelMedium
        )
        
        // Специальные символы (без изменений)
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
        
        // Рокировки (без изменений)
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
        
        // Управление
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
```

---

## 📤 Экспорт в PGN (английская нотация)

При экспорте в PGN используется стандартная английская нотация:

```kotlin
class PGNExporter {
    fun exportGame(game: Game): String {
        val header = buildString {
            appendLine("[Event \"?\"]")
            appendLine("[Site \"?\"]")
            appendLine("[Date \"${formatDate(game.createdAt)}\"]")
            appendLine("[Round \"?\"]")
            appendLine("[White \"?\"]")
            appendLine("[Black \"?\"]")
            appendLine("[Result \"*\"]")
            appendLine()
        }
        
        // Конвертируем русскую нотацию в английскую для PGN
        val moves = game.moves.joinToString(" ") { move ->
            val whiteMoveEng = convertToEnglishNotation(move.whiteMove)
            val blackMoveEng = move.blackMove?.let { convertToEnglishNotation(it) }
            
            if (blackMoveEng != null) {
                "${move.moveNumber}. $whiteMoveEng $blackMoveEng"
            } else {
                "${move.moveNumber}. $whiteMoveEng"
            }
        }
        
        return header + moves + " *"
    }
    
    private fun convertToEnglishNotation(russianMove: String): String {
        return russianMove
            .replace("Кр", "K")
            .replace("Ф", "Q")
            .replace("Л", "R")
            .replace("С", "B")
            .replace("К", "N")
    }
    
    private fun formatDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("yyyy.MM.dd", Locale.US)
        return sdf.format(Date(timestamp))
    }
}
```

---

## 💡 Примеры использования

### Пример 1: Испанская партия (русская нотация)

```
Ввод:
Пешка: [e4] → ✓ → [e5] → ✓
Конь: [f3] → ✓ → [c6] → ✓
Слон: [b5] → ✓

Отображение:
1. e4 e5
2. Кf3 Кc6
3. Сb5 _

Копирование (русская):
"1. e4 e5 2. Кf3 Кc6 3. Сb5"

Экспорт в PGN (английская):
"1. e4 e5 2. Nf3 Nc6 3. Bb5"
```

### Пример 2: Детский мат

```
Ввод:
e4 → e5
Слон: c4 → Слон: c5
Ферзь: h5 → Конь: c6
Ферзь: x → f7 → # → ✓

Отображение:
1. e4 e5
2. Сc4 Сc5
3. Фh5 Кc6
4. Фxf7# _

Копирование:
"1. e4 e5 2. Сc4 Сc5 3. Фh5 Кc6 4. Фxf7#"
```

---

## 🔄 Настройки нотации (опционально для v2.0)

Можно добавить переключатель между русской и английской нотацией:

```kotlin
enum class NotationLanguage {
    RUSSIAN,  // Кр, Ф, Л, С, К
    ENGLISH;  // K, Q, R, B, N
    
    fun toDisplayString(): String = when (this) {
        RUSSIAN -> "Русская (Кр, Ф, Л, С, К)"
        ENGLISH -> "Английская (K, Q, R, B, N)"
    }
}

data class GameState(
    // ... существующие поля
    val notationLanguage: NotationLanguage = NotationLanguage.RUSSIAN
)
```

---

## 📊 Итоговая таблица обозначений

| Фигура | Русская | Английская | Unicode |
|--------|---------|------------|---------|
| Король | Кр | K | ♔/♚ |
| Ферзь | Ф | Q | ♕/♛ |
| Ладья | Л | R | ♖/♜ |
| Слон | С | B | ♗/♝ |
| Конь | К | N | ♘/♞ |
| Пешка | - | - | ♙/♟ |

---

## ✨ Преимущества русской нотации

1. ✅ **Привычнее для русскоязычных пользователей**
2. ✅ **Соответствует русским шахматным книгам**
3. ✅ **Легче читать и понимать**
4. ✅ **Можно экспортировать в английскую для PGN**

---

## 🎯 Готово!

Приложение теперь использует русскую шахматную нотацию по умолчанию, что делает его более удобным для русскоязычных пользователей.