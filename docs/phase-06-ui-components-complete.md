# Фаза 6: UI Базовые компоненты - Завершено ✅

## Обзор

Создан полный набор переиспользуемых UI компонентов для приложения ChessSignature. Все компоненты используют созданную ранее систему темизации и готовы к интеграции в основной интерфейс.

## Созданные файлы

### 1. Buttons.kt - Кнопки
**Путь:** `app/src/main/java/com/sin28x/chesssignature/ui/components/Buttons.kt`

**Компоненты:**

#### ChessSquareButton
Кнопка для клетки шахматной доски (a-h, 1-8).
```kotlin
@Composable
fun ChessSquareButton(
    square: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
)
```

**Особенности:**
- ✅ Размер 56x56 dp
- ✅ Закругленные углы (8dp)
- ✅ Outlined стиль
- ✅ Моноширинный шрифт для координат
- ✅ Поддержка disabled состояния

**Использование:**
```kotlin
ChessSquareButton(
    square = "e4",
    onClick = { viewModel.addSquare("e4") }
)
```

#### PieceButton
Кнопка для выбора типа фигуры.
```kotlin
@Composable
fun PieceButton(
    piece: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    selected: Boolean = false
)
```

**Особенности:**
- ✅ Размер 56x56 dp
- ✅ Визуальная индикация выбора (selected state)
- ✅ Жирный шрифт для выбранной фигуры
- ✅ Primary цвет для выбранной кнопки
- ✅ Увеличенная толщина border при выборе

**Использование:**
```kotlin
PieceButton(
    piece = "К",
    onClick = { viewModel.addSymbol("К") },
    selected = selectedPiece == "К"
)
```

#### SymbolButton
Кнопка для специальных символов (взятие, шах, мат, рокировка).
```kotlin
@Composable
fun SymbolButton(
    symbol: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
)
```

**Поддерживаемые символы:**
- `:` - взятие
- `+` - шах
- `#` - мат
- `0-0` - короткая рокировка
- `0-0-0` - длинная рокировка

#### ActionButton
Универсальная кнопка действия.
```kotlin
@Composable
fun ActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    primary: Boolean = false
)
```

**Варианты:**
- `primary = false` - Outlined стиль
- `primary = true` - Filled стиль с primary цветом

**Использование:**
```kotlin
ActionButton(
    text = "Добавить ход",
    onClick = { viewModel.addMove() },
    primary = true
)
```

#### DeleteButton
Специализированная кнопка удаления.
```kotlin
@Composable
fun DeleteButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
)
```

**Особенности:**
- ✅ Символ backspace (⌫)
- ✅ Error цвет для border и текста
- ✅ Размер 56x56 dp

### 2. InputComponents.kt - Компоненты ввода
**Путь:** `app/src/main/java/com/sin28x/chesssignature/ui/components/InputComponents.kt`

**Компоненты:**

#### MoveInputField
Поле отображения текущего ввода хода.
```kotlin
@Composable
fun MoveInputField(
    input: String,
    validationResult: ValidationResult,
    modifier: Modifier = Modifier
)
```

**Особенности:**
- ✅ Цветовая индикация валидности
- ✅ Зеленый border для валидного ввода
- ✅ Красный border для невалидного
- ✅ Оранжевый border для неполного
- ✅ Placeholder "Введите ход"
- ✅ Моноширинный шрифт 20sp

**Цветовая схема:**
- **Valid**: Зеленый фон (10% opacity) + зеленый border
- **Invalid**: Красный фон (10% opacity) + красный border
- **Incomplete**: Обычный фон + оранжевый border

#### ValidationIndicator
Индикатор валидации с иконкой и текстом.
```kotlin
@Composable
fun ValidationIndicator(
    validationResult: ValidationResult,
    modifier: Modifier = Modifier
)
```

**Сообщения:**
- **Valid**: "Ход корректен" (зеленый)
- **Invalid**: "Неверный формат" (красный)
- **Incomplete**: "Продолжайте ввод" (оранжевый)

#### CompactValidationIndicator
Компактная версия индикатора (только цветная точка).
```kotlin
@Composable
fun CompactValidationIndicator(
    validationResult: ValidationResult,
    modifier: Modifier = Modifier
)
```

**Использование:**
- Когда нужно сэкономить место
- В углу поля ввода
- Размер 16x16 dp

#### MoveInputWithValidation
Комбинированный компонент: поле ввода + индикатор.
```kotlin
@Composable
fun MoveInputWithValidation(
    input: String,
    validationResult: ValidationResult,
    modifier: Modifier = Modifier,
    showValidationText: Boolean = true
)
```

**Режимы:**
- `showValidationText = true` - полный индикатор внизу
- `showValidationText = false` - компактный индикатор в углу

### 3. MoveComponents.kt - Компоненты ходов
**Путь:** `app/src/main/java/com/sin28x/chesssignature/ui/components/MoveComponents.kt`

**Компоненты:**

#### MoveItem
Компонент для отображения одного хода.
```kotlin
@Composable
fun MoveItem(
    moveNumber: Int,
    move: Move,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false
)
```

**Особенности:**
- ✅ Номер хода + нотация
- ✅ Поддержка выбора (selected state)
- ✅ Опциональный onClick
- ✅ Primary container цвет при выборе

#### MovePairItem
Отображение пары ходов (белые и черные).
```kotlin
@Composable
fun MovePairItem(
    moveNumber: Int,
    whiteMove: Move,
    blackMove: Move? = null,
    onWhiteClick: (() -> Unit)? = null,
    onBlackClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    selectedColor: PieceColor? = null
)
```

**Особенности:**
- ✅ Классический формат шахматной нотации
- ✅ Номер хода + два хода в ряд
- ✅ Поддержка неполной пары (только белые)
- ✅ Независимый выбор белых/черных

**Пример отображения:**
```
1. e4    e5
2. Кf3   Кc6
3. Сb5
```

#### MoveChip
Компактный чип для отображения хода.
```kotlin
@Composable
fun MoveChip(
    move: Move,
    onClick: (() -> Unit)? = null,
    isSelected: Boolean = false,
    modifier: Modifier = Modifier
)
```

**Использование:**
- Внутри MovePairItem
- Отдельные чипы для быстрого доступа
- Surface variant фон
- Primary container при выборе

#### PositionInfoCard
Карточка с информацией о позиции.
```kotlin
@Composable
fun PositionInfoCard(
    totalMoves: Int,
    modifier: Modifier = Modifier
)
```

**Отображаемая информация:**
- ✅ Общее количество ходов
- ✅ Чей ход (белые/черные)
- ✅ Card с surface variant фоном

## Архитектура компонентов

### Принципы дизайна

1. **Переиспользуемость**
   - Все компоненты независимы
   - Минимум обязательных параметров
   - Максимум кастомизации через Modifier

2. **Консистентность**
   - Единый стиль закругления (8dp для кнопок, 12dp для карточек)
   - Единые размеры кнопок (56x56 dp)
   - Единые отступы и spacing

3. **Доступность**
   - Поддержка disabled состояний
   - Достаточный контраст цветов
   - Читаемые размеры шрифтов

4. **Темизация**
   - Использование MaterialTheme.colorScheme
   - Использование MaterialTheme.chessColors
   - Использование ChessTypography

### Цветовая система

**Кнопки:**
- Outline: MaterialTheme.colorScheme.outline
- Content: MaterialTheme.colorScheme.onSurface
- Selected: MaterialTheme.colorScheme.primary
- Error: MaterialTheme.colorScheme.error

**Валидация:**
- Valid: MaterialTheme.chessColors.validInput
- Invalid: MaterialTheme.chessColors.invalidInput
- Incomplete: MaterialTheme.chessColors.incompleteInput

**Ходы:**
- Background: MaterialTheme.colorScheme.surface
- Selected: MaterialTheme.colorScheme.primaryContainer
- Chip: MaterialTheme.colorScheme.surfaceVariant

## Примеры использования

### Клавиатура координат
```kotlin
@Composable
fun CoordinateKeyboard(viewModel: GameViewModel) {
    Column {
        // Ряд с буквами
        Row {
            ('a'..'h').forEach { letter ->
                ChessSquareButton(
                    square = letter.toString(),
                    onClick = { viewModel.addSquare(letter.toString()) }
                )
            }
        }
        
        // Ряд с цифрами
        Row {
            (1..8).forEach { number ->
                ChessSquareButton(
                    square = number.toString(),
                    onClick = { viewModel.addSquare(number.toString()) }
                )
            }
        }
    }
}
```

### Клавиатура фигур
```kotlin
@Composable
fun PieceKeyboard(
    selectedPiece: String?,
    onPieceSelected: (String) -> Unit
) {
    Row {
        listOf("К", "Ф", "Л", "С", "Кр").forEach { piece ->
            PieceButton(
                piece = piece,
                onClick = { onPieceSelected(piece) },
                selected = selectedPiece == piece
            )
        }
    }
}
```

### Поле ввода с валидацией
```kotlin
@Composable
fun InputSection(viewModel: GameViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    
    Column {
        MoveInputWithValidation(
            input = uiState.currentInput,
            validationResult = uiState.validationResult,
            showValidationText = true
        )
        
        Row {
            ActionButton(
                text = "Добавить",
                onClick = { viewModel.addMove() },
                primary = true,
                enabled = uiState.validationResult == ValidationResult.Valid
            )
            
            DeleteButton(
                onClick = { viewModel.deleteLastSymbol() },
                enabled = uiState.currentInput.isNotEmpty()
            )
        }
    }
}
```

### Список ходов
```kotlin
@Composable
fun MovesList(moves: List<Move>) {
    LazyColumn {
        items(moves.chunked(2)) { pair ->
            val moveNumber = moves.indexOf(pair[0]) / 2 + 1
            MovePairItem(
                moveNumber = moveNumber,
                whiteMove = pair[0],
                blackMove = pair.getOrNull(1),
                onWhiteClick = { /* handle click */ },
                onBlackClick = { /* handle click */ }
            )
        }
    }
}
```

## Технические детали

### Размеры и отступы
- **Кнопки**: 56x56 dp
- **Border radius кнопок**: 8dp
- **Border radius карточек**: 12dp
- **Border width**: 1dp (2dp для selected)
- **Padding кнопок**: 0dp (content padding)
- **Padding карточек**: 16dp

### Типографика
- **Координаты**: ChessTypography.keyboardSquare (18sp)
- **Фигуры**: ChessTypography.keyboardPiece (20sp)
- **Ввод**: ChessTypography.inputField (20sp)
- **Нотация**: ChessTypography.moveNotation (16sp)
- **Номер хода**: ChessTypography.moveNumber (14sp)

### Состояния
- **Enabled**: Полная непрозрачность
- **Disabled**: 38% opacity
- **Selected**: Primary container background
- **Hover**: Не реализовано (мобильное приложение)

## Следующие шаги

После завершения Фазы 6, следующие задачи:

1. **Фаза 7: UI Сетка и клавиатура**
   - Сборка клавиатуры из созданных кнопок
   - Организация layout для координат и фигур
   - Добавление специальных символов

2. **Фаза 8: UI Список ходов**
   - Создание прокручиваемого списка
   - Интеграция MoveItem и MovePairItem
   - Функция копирования нотации

3. **Фаза 9: Главный экран**
   - Интеграция всех компонентов
   - Подключение к GameViewModel
   - Финальная компоновка UI

## Заключение

Фаза 6 успешно завершена. Создан полный набор переиспользуемых UI компонентов:

✅ **Кнопки** - 5 типов кнопок для разных целей
✅ **Ввод** - 4 компонента для ввода и валидации
✅ **Ходы** - 4 компонента для отображения ходов
✅ **Темизация** - все компоненты используют систему темизации
✅ **Документация** - подробное описание каждого компонента

Компоненты готовы к использованию в следующих фазах разработки.