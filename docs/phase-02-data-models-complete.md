# Фаза 2: Модели данных - Завершено ✅

## Созданные модели

### 1. PieceType (Enum)
**Файл:** [`app/src/main/java/com/sin28x/chesssignature/model/PieceType.kt`](../app/src/main/java/com/sin28x/chesssignature/model/PieceType.kt)

Типы шахматных фигур:
- `PAWN` - Пешка
- `KNIGHT` - Конь (К)
- `BISHOP` - Слон (С)
- `ROOK` - Ладья (Л)
- `QUEEN` - Ферзь (Ф)
- `KING` - Король (Кр)

**Методы:**
- `toRussianNotation()` - конвертация в русскую нотацию
- `toEnglishNotation()` - конвертация в английскую нотацию (PGN)
- `fromRussianNotation(String)` - создание из русской нотации

### 2. PieceColor (Enum)
**Файл:** [`app/src/main/java/com/sin28x/chesssignature/model/PieceColor.kt`](../app/src/main/java/com/sin28x/chesssignature/model/PieceColor.kt)

Цвет фигур:
- `WHITE` - Белые
- `BLACK` - Черные

**Методы:**
- `opposite()` - возвращает противоположный цвет
- `toRussianName()` - название на русском

### 3. ChessSquare (Data Class)
**Файл:** [`app/src/main/java/com/sin28x/chesssignature/model/ChessSquare.kt`](../app/src/main/java/com/sin28x/chesssignature/model/ChessSquare.kt)

Представляет клетку на доске:
- `file: Char` - вертикаль (a-h)
- `rank: Int` - горизонталь (1-8)

**Методы:**
- `toString()` - строковое представление (например, "e4")
- `fromString(String)` - создание из строки
- `allSquares()` - все клетки доски (64 штуки)

**Валидация:**
- Проверка корректности вертикали (a-h)
- Проверка корректности горизонтали (1-8)

### 4. Move (Data Class)
**Файл:** [`app/src/main/java/com/sin28x/chesssignature/model/Move.kt`](../app/src/main/java/com/sin28x/chesssignature/model/Move.kt)

Представляет шахматный ход:
- `piece: PieceType` - тип фигуры
- `from: ChessSquare?` - откуда (опционально)
- `to: ChessSquare` - куда
- `isCapture: Boolean` - взятие
- `isCheck: Boolean` - шах
- `isCheckmate: Boolean` - мат
- `promotion: PieceType?` - превращение пешки
- `isCastlingKingside: Boolean` - короткая рокировка
- `isCastlingQueenside: Boolean` - длинная рокировка
- `moveNumber: Int` - номер хода
- `color: PieceColor` - цвет

**Методы:**
- `toRussianNotation()` - русская нотация (например, "Кре4", "0-0", "Л:d5+")
- `toEnglishNotation()` - английская нотация PGN (например, "Nf3", "O-O", "Rxd5+")
- `toFullNotation()` - полная запись с номером хода

### 5. ValidationResult (Sealed Class)
**Файл:** [`app/src/main/java/com/sin28x/chesssignature/model/ValidationResult.kt`](../app/src/main/java/com/sin28x/chesssignature/model/ValidationResult.kt)

Результат валидации ввода:
- `Valid` - ввод валиден
- `Incomplete` - ввод неполный, но корректный
- `Invalid(message: String)` - ввод невалиден с сообщением об ошибке

**Методы:**
- `isValid()` - проверка валидности
- `isInvalid()` - проверка невалидности
- `isIncomplete()` - проверка неполноты

### 6. GameState (Data Class)
**Файл:** [`app/src/main/java/com/sin28x/chesssignature/model/GameState.kt`](../app/src/main/java/com/sin28x/chesssignature/model/GameState.kt)

Состояние игры:
- `moves: List<Move>` - список ходов
- `currentInput: String` - текущий ввод
- `isWhiteTurn: Boolean` - ход белых
- `selectedPiece: PieceType` - выбранная фигура
- `startingColor: PieceColor` - начальный цвет
- `inputValidation: ValidationResult` - результат валидации

**Методы:**
- `getCurrentMoveNumber()` - номер текущего хода
- `getCurrentColor()` - цвет текущего хода
- `getMovesAsRussianNotation()` - все ходы в русской нотации
- `getMovesAsPGN()` - все ходы в формате PGN
- `canAddMove()` - можно ли добавить ход

## Примеры использования

### Создание хода
```kotlin
val move = Move(
    piece = PieceType.KNIGHT,
    to = ChessSquare('f', 3),
    moveNumber = 1,
    color = PieceColor.WHITE
)
// Результат: "1. Кf3"
```

### Рокировка
```kotlin
val castling = Move(
    piece = PieceType.KING,
    to = ChessSquare('g', 1),
    isCastlingKingside = true,
    moveNumber = 5,
    color = PieceColor.WHITE
)
// Результат: "5. 0-0"
```

### Взятие с шахом
```kotlin
val capture = Move(
    piece = PieceType.QUEEN,
    from = ChessSquare('d', 1),
    to = ChessSquare('h', 5),
    isCapture = true,
    isCheck = true,
    moveNumber = 10,
    color = PieceColor.WHITE
)
// Результат: "10. Фd1:h5+"
```

## Статус
✅ Все модели данных созданы
✅ Проект собирается без ошибок
✅ Поддержка русской и английской нотации
✅ Валидация данных

## Следующий шаг
Готово к переходу к **Фазе 3: Бизнес-логика** - создание GameViewModel и логики валидации ходов.