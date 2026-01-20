# Фаза 7: UI Сетка и клавиатура - Завершено ✅

## Обзор

Создана полная система клавиатуры для ввода шахматных ходов. Клавиатура состоит из трех секций: координаты доски, фигуры и специальные символы. Все компоненты используют ранее созданные базовые кнопки и интегрированы в единый интерфейс.

## Созданные файлы

### Keyboard.kt
**Путь:** `app/src/main/java/com/sin28x/chesssignature/ui/components/Keyboard.kt`

**Компоненты:**

## 1. CoordinateKeyboard

Клавиатура для ввода координат шахматной доски.

```kotlin
@Composable
fun CoordinateKeyboard(
    onSquareClick: (String) -> Unit,
    modifier: Modifier = Modifier
)
```

**Структура:**
- **Первый ряд**: Буквы a-h (горизонтали)
- **Второй ряд**: Цифры 1-8 (вертикали)

**Особенности:**
- ✅ 8 кнопок в каждом ряду
- ✅ Равномерное распределение (weight = 1f)
- ✅ Отступы между кнопками 4dp
- ✅ Вертикальный отступ между рядами 8dp

**Использование:**
```kotlin
CoordinateKeyboard(
    onSquareClick = { square ->
        viewModel.addSquare(square)
    }
)
```

**Пример отображения:**
```
[a] [b] [c] [d] [e] [f] [g] [h]
[1] [2] [3] [4] [5] [6] [7] [8]
```

## 2. PieceKeyboard

Клавиатура для выбора типа фигуры в русской нотации.

```kotlin
@Composable
fun PieceKeyboard(
    onPieceClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    selectedPiece: String? = null
)
```

**Фигуры:**
- **К** - Конь
- **Ф** - Ферзь
- **Л** - Ладья
- **С** - Слон
- **Кр** - Король

**Особенности:**
- ✅ 5 кнопок в одном ряду
- ✅ Визуальная индикация выбранной фигуры
- ✅ Равномерное распределение
- ✅ Отступы между кнопками 4dp

**Использование:**
```kotlin
val selectedPiece by remember { mutableStateOf<String?>(null) }

PieceKeyboard(
    onPieceClick = { piece ->
        viewModel.addSymbol(piece)
    },
    selectedPiece = selectedPiece
)
```

**Пример отображения:**
```
[К] [Ф] [Л] [С] [Кр]
```

## 3. SymbolKeyboard

Клавиатура для специальных символов и действий.

```kotlin
@Composable
fun SymbolKeyboard(
    onSymbolClick: (String) -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
    deleteEnabled: Boolean = true
)
```

**Символы:**
- **:** - Взятие
- **+** - Шах
- **#** - Мат
- **0-0** - Короткая рокировка
- **0-0-0** - Длинная рокировка
- **⌫** - Удаление последнего символа

**Особенности:**
- ✅ 6 кнопок в одном ряду
- ✅ Рокировки занимают больше места (weight = 1.5f)
- ✅ Кнопка удаления с error цветом
- ✅ Управление состоянием deleteEnabled

**Использование:**
```kotlin
SymbolKeyboard(
    onSymbolClick = { symbol ->
        viewModel.addSymbol(symbol)
    },
    onDeleteClick = {
        viewModel.deleteLastSymbol()
    },
    deleteEnabled = currentInput.isNotEmpty()
)
```

**Пример отображения:**
```
[:] [+] [#] [0-0  ] [0-0-0] [⌫]
```

## 4. ChessKeyboard

Полная шахматная клавиатура, объединяющая все секции.

```kotlin
@Composable
fun ChessKeyboard(
    onSquareClick: (String) -> Unit,
    onPieceClick: (String) -> Unit,
    onSymbolClick: (String) -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
    selectedPiece: String? = null,
    deleteEnabled: Boolean = true
)
```

**Структура:**
```
┌─────────────────────────────────┐
│  CoordinateKeyboard             │
│  [a][b][c][d][e][f][g][h]      │
│  [1][2][3][4][5][6][7][8]      │
├─────────────────────────────────┤
│  PieceKeyboard                  │
│  [К][Ф][Л][С][Кр]              │
├─────────────────────────────────┤
│  SymbolKeyboard                 │
│  [:][+][#][0-0][0-0-0][⌫]      │
└─────────────────────────────────┘
```

**Особенности:**
- ✅ Обернута в Card с surfaceVariant фоном
- ✅ Padding 12dp вокруг содержимого
- ✅ Вертикальные отступы между секциями 12dp
- ✅ Единый интерфейс для всех действий

**Использование:**
```kotlin
@Composable
fun InputScreen(viewModel: GameViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    
    Column {
        // Поле ввода
        MoveInputWithValidation(
            input = uiState.currentInput,
            validationResult = uiState.validationResult
        )
        
        // Клавиатура
        ChessKeyboard(
            onSquareClick = { square ->
                viewModel.addSquare(square)
            },
            onPieceClick = { piece ->
                viewModel.addSymbol(piece)
            },
            onSymbolClick = { symbol ->
                viewModel.addSymbol(symbol)
            },
            onDeleteClick = {
                viewModel.deleteLastSymbol()
            },
            deleteEnabled = uiState.currentInput.isNotEmpty()
        )
    }
}
```

## Архитектура

### Композиция компонентов

```
ChessKeyboard
├── Card (surfaceVariant)
│   └── Column (padding 12dp, spacing 12dp)
│       ├── CoordinateKeyboard
│       │   └── Column (spacing 8dp)
│       │       ├── Row (letters a-h, spacing 4dp)
│       │       └── Row (numbers 1-8, spacing 4dp)
│       ├── PieceKeyboard
│       │   └── Row (pieces К,Ф,Л,С,Кр, spacing 4dp)
│       └── SymbolKeyboard
│           └── Row (symbols :,+,#,0-0,0-0-0,⌫, spacing 4dp)
```

### Используемые базовые компоненты

Из [`Buttons.kt`](app/src/main/java/com/sin28x/chesssignature/ui/components/Buttons.kt):
- **ChessSquareButton** - для координат
- **PieceButton** - для фигур
- **SymbolButton** - для символов
- **DeleteButton** - для удаления

### Размеры и отступы

**Кнопки:**
- Размер: 56x56 dp (из базовых компонентов)
- Border radius: 8dp

**Отступы:**
- Между кнопками в ряду: 4dp
- Между рядами координат: 8dp
- Между секциями клавиатуры: 12dp
- Padding карточки: 12dp

**Веса (weight):**
- Обычные кнопки: 1f
- Короткая рокировка: 1.5f
- Длинная рокировка: 1.5f

## Примеры использования

### Базовое использование

```kotlin
@Composable
fun ChessInputScreen() {
    val viewModel: GameViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Поле ввода
        MoveInputWithValidation(
            input = uiState.currentInput,
            validationResult = uiState.validationResult,
            modifier = Modifier.fillMaxWidth()
        )
        
        // Клавиатура
        ChessKeyboard(
            onSquareClick = viewModel::addSquare,
            onPieceClick = viewModel::addSymbol,
            onSymbolClick = viewModel::addSymbol,
            onDeleteClick = viewModel::deleteLastSymbol,
            deleteEnabled = uiState.currentInput.isNotEmpty()
        )
        
        // Кнопки действий
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ActionButton(
                text = "Добавить ход",
                onClick = viewModel::addMove,
                primary = true,
                enabled = uiState.validationResult is ValidationResult.Valid,
                modifier = Modifier.weight(1f)
            )
            
            ActionButton(
                text = "Очистить",
                onClick = viewModel::clearInput,
                enabled = uiState.currentInput.isNotEmpty(),
                modifier = Modifier.weight(1f)
            )
        }
    }
}
```

### Использование отдельных клавиатур

```kotlin
@Composable
fun CustomInputScreen() {
    Column {
        // Только координаты
        CoordinateKeyboard(
            onSquareClick = { square ->
                println("Clicked: $square")
            }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Только фигуры
        var selected by remember { mutableStateOf<String?>(null) }
        PieceKeyboard(
            onPieceClick = { piece ->
                selected = piece
            },
            selectedPiece = selected
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Только символы
        SymbolKeyboard(
            onSymbolClick = { symbol ->
                println("Symbol: $symbol")
            },
            onDeleteClick = {
                println("Delete")
            }
        )
    }
}
```

### Интеграция с ViewModel

```kotlin
@Composable
fun GameScreen(viewModel: GameViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Информация о позиции
            PositionInfoCard(
                totalMoves = uiState.gameState.moves.size
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Поле ввода
            MoveInputWithValidation(
                input = uiState.currentInput,
                validationResult = uiState.validationResult
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Клавиатура
            ChessKeyboard(
                onSquareClick = viewModel::addSquare,
                onPieceClick = viewModel::addSymbol,
                onSymbolClick = viewModel::addSymbol,
                onDeleteClick = viewModel::deleteLastSymbol,
                deleteEnabled = uiState.currentInput.isNotEmpty()
            )
        }
    }
}
```

## Технические детали

### Адаптивность

Клавиатура автоматически адаптируется к ширине экрана благодаря использованию `weight`:

```kotlin
Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(4.dp)
) {
    items.forEach { item ->
        Button(
            modifier = Modifier.weight(1f) // Равномерное распределение
        )
    }
}
```

### Состояния

**Кнопки поддерживают:**
- Enabled/Disabled
- Selected (только PieceButton)
- Hover (автоматически в Material 3)

**Управление состоянием:**
```kotlin
ChessKeyboard(
    deleteEnabled = currentInput.isNotEmpty(), // Динамическое состояние
    selectedPiece = selectedPiece // Визуальная индикация
)
```

### Производительность

- Все клавиатуры stateless
- Нет внутреннего состояния
- Минимальная рекомпозиция
- Эффективное использование remember

## Следующие шаги

После завершения Фазы 7, следующие задачи:

1. **Фаза 8: UI Список ходов**
   - Создание прокручиваемого списка ходов
   - Интеграция MoveItem и MovePairItem
   - Функция копирования нотации в буфер обмена

2. **Фаза 9: Главный экран**
   - Интеграция всех компонентов
   - Подключение к GameViewModel
   - Финальная компоновка UI
   - Навигация и управление состоянием

3. **Фаза 10: Android Entry Point**
   - Настройка MainActivity
   - Инициализация приложения
   - Тестирование на устройстве

## Заключение

Фаза 7 успешно завершена. Создана полная система клавиатуры:

✅ **CoordinateKeyboard** - ввод координат доски (a-h, 1-8)
✅ **PieceKeyboard** - выбор фигур (К, Ф, Л, С, Кр)
✅ **SymbolKeyboard** - специальные символы и действия
✅ **ChessKeyboard** - объединенная клавиатура

**Особенности:**
- Модульная архитектура
- Переиспользуемые компоненты
- Адаптивный дизайн
- Интеграция с темой
- Готовность к использованию

Клавиатура готова к интеграции в главный экран приложения.