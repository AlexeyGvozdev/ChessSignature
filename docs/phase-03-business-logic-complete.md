# Фаза 3: Бизнес-логика - Завершено ✅

## Созданный компонент

### GameViewModel
**Файл:** [`app/src/main/java/com/sin28x/chesssignature/viewmodel/GameViewModel.kt`](../app/src/main/java/com/sin28x/chesssignature/viewmodel/GameViewModel.kt)

Центральный компонент бизнес-логики приложения, управляющий состоянием игры.

## Архитектура

### State Management
Использует **StateFlow** для реактивного управления состоянием:
```kotlin
private val _gameState = MutableStateFlow(GameState())
val gameState: StateFlow<GameState> = _gameState.asStateFlow()
```

### Основные функции

#### 1. Управление вводом
- `updateInput(input: String)` - обновляет текущий ввод с валидацией
- `addSquare(square: ChessSquare)` - добавляет клетку к вводу
- `addSymbol(symbol: String)` - добавляет специальный символ (`:`, `+`, `#`)
- `deleteLastSymbol()` - удаляет последний символ
- `clearInput()` - очищает весь ввод

#### 2. Управление фигурами
- `selectPiece(piece: PieceType)` - выбирает фигуру для ввода
- `toggleStartingColor()` - переключает начальный цвет

#### 3. Управление ходами
- `addMove()` - добавляет ход в список
- `deleteLastMove()` - удаляет последний ход
- `clearAllMoves()` - очищает все ходы

#### 4. Валидация
- `validateInput(input, state): ValidationResult` - валидирует текущий ввод
  - Проверяет формат хода
  - Поддерживает рокировки (`0-0`, `0-0-0`)
  - Различает полные и неполные ходы
  - Возвращает сообщения об ошибках

#### 5. Парсинг
- `parseMove(input, state): Move?` - парсит строку в объект Move
  - Обрабатывает рокировки
  - Парсит обычные ходы
  - Извлекает фигуру, клетки, взятия, шахи/маты

## Поддерживаемые форматы ходов

### Обычные ходы
- `e4` - ход пешки
- `Кf3` - ход коня
- `Фd4` - ход ферзя
- `Крe1` - ход короля

### Ходы с исходной клеткой
- `Кg1f3` - конь с g1 на f3
- `Лa1d1` - ладья с a1 на d1

### Взятия
- `e:d5` - пешка берет на d5
- `Ф:h5` - ферзь берет на h5
- `Кg1:f3` - конь с g1 берет на f3

### Шахи и маты
- `Фh5+` - ферзь на h5 с шахом
- `Л:d8#` - ладья берет на d8 с матом

### Рокировки
- `0-0` - короткая рокировка
- `0-0-0` - длинная рокировка

## Валидация

### Regex паттерны

**Полный ход:**
```regex
^(Кр|Ф|Л|С|К)?([a-h][1-8])?:?([a-h][1-8])([+#])?$
```

**Частичный ход (для Incomplete):**
```regex
^(Кр|Ф|Л|С|К)?([a-h]?[1-8]?)?:?([a-h]?[1-8]?)?([+#])?$
```

### Состояния валидации
1. **Valid** - ход полностью введен и корректен
2. **Incomplete** - ввод неполный, но может стать валидным
3. **Invalid** - ввод некорректен с сообщением об ошибке

## Примеры использования

### Базовый ввод хода
```kotlin
val viewModel = GameViewModel()

// Пользователь вводит "e4"
viewModel.updateInput("e")  // Incomplete
viewModel.updateInput("e4") // Valid
viewModel.addMove()         // Ход добавлен
```

### Использование сетки клеток
```kotlin
// Пользователь кликает на клетку e4
viewModel.addSquare(ChessSquare('e', 4))
// currentInput = "e4", validation = Valid
```

### Ввод взятия с шахом
```kotlin
viewModel.selectPiece(PieceType.QUEEN)
viewModel.addSquare(ChessSquare('d', 1))
viewModel.addSymbol(":")
viewModel.addSquare(ChessSquare('h', 5))
viewModel.addSymbol("+")
// currentInput = "Фd1:h5+", validation = Valid
viewModel.addMove()
```

### Рокировка
```kotlin
viewModel.updateInput("0-0")  // Valid
viewModel.addMove()           // Короткая рокировка добавлена
```

## Реактивность

UI подписывается на `gameState` и автоматически обновляется:
```kotlin
val state by viewModel.gameState.collectAsState()

// UI реагирует на изменения:
// - state.currentInput
// - state.inputValidation
// - state.moves
// - state.selectedPiece
// и т.д.
```

## Качество кода

### Документация
Все публичные функции имеют подробные KDoc комментарии с описанием:
- Назначения функции
- Параметров с типами и описанием
- Возвращаемых значений

### Immutability
Все обновления состояния создают новые объекты:
```kotlin
_gameState.update { currentState ->
    currentState.copy(
        currentInput = newInput,
        inputValidation = newValidation
    )
}
```

### Coroutines
Использует `viewModelScope` для корутин (готово для будущих асинхронных операций)

### Type Safety
Строгая типизация через sealed classes и enums предотвращает ошибки

## Статус
✅ GameViewModel создан
✅ Валидация ходов реализована
✅ Парсинг ходов реализован
✅ Управление состоянием через StateFlow
✅ Подробная документация всех функций
✅ Проект собирается без ошибок

## Следующий шаг
Готово к **Фазе 5: UI Тема** - создание цветовой схемы и типографики.
(Фаза 4 пропускается, так как platform-specific код уже создан в Фазе 1)