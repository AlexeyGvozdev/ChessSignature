# Git Workflow - ChessSignature

## 🌿 Стратегия веток

Каждая фаза разработки выполняется в отдельной ветке, затем мержится в `develop`, а из `develop` в `main`.

---

## 📊 Структура веток

```
main (production-ready код)
  ↑
develop (integration branch)
  ↑
feature/phase-XX (фазы разработки)
```

---

## 🔄 Workflow для каждой фазы

### Шаг 1: Создание ветки для фазы

```bash
# Переключиться на develop
git checkout develop

# Создать и переключиться на ветку фазы
git checkout -b feature/phase-01-project-setup
```

### Шаг 2: Работа в ветке

```bash
# Делать коммиты по мере выполнения задач
git add .
git commit -m "feat(phase-01): настройка Compose Multiplatform"

git add .
git commit -m "feat(phase-01): создание структуры модулей"
```

### Шаг 3: Завершение фазы

```bash
# Убедиться что все изменения закоммичены
git status

# Переключиться на develop
git checkout develop

# Смержить ветку фазы
git merge feature/phase-01-project-setup

# Удалить ветку фазы (опционально)
git branch -d feature/phase-01-project-setup
```

### Шаг 4: Переход к следующей фазе

```bash
# Создать ветку для следующей фазы
git checkout -b feature/phase-02-data-models
```

---

## 📋 Список веток для всех фаз

### Фаза 1: Настройка проекта
```bash
git checkout -b feature/phase-01-project-setup
```
**Задачи:**
- Настройка Compose Multiplatform
- Создание структуры модулей
- Настройка зависимостей

**Коммиты:**
```bash
git commit -m "feat(phase-01): настройка Compose Multiplatform"
git commit -m "feat(phase-01): создание структуры пакетов"
git commit -m "feat(phase-01): настройка зависимостей"
```

---

### Фаза 2: Модели данных
```bash
git checkout develop
git checkout -b feature/phase-02-data-models
```
**Задачи:**
- Создание enum классов
- Создание data классов
- Unit тесты

**Коммиты:**
```bash
git commit -m "feat(phase-02): добавить PieceType с русской нотацией"
git commit -m "feat(phase-02): добавить PieceColor и ValidationResult"
git commit -m "feat(phase-02): добавить Move и GameState"
git commit -m "test(phase-02): unit тесты для моделей"
```

---

### Фаза 3: Бизнес-логика
```bash
git checkout develop
git checkout -b feature/phase-03-business-logic
```
**Задачи:**
- MoveValidator
- GameViewModel
- Unit тесты

**Коммиты:**
```bash
git commit -m "feat(phase-03): добавить MoveValidator с русской нотацией"
git commit -m "feat(phase-03): добавить GameViewModel"
git commit -m "feat(phase-03): реализовать методы ViewModel"
git commit -m "test(phase-03): unit тесты для ViewModel"
```

---

### Фаза 4: Platform-specific код
```bash
git checkout develop
git checkout -b feature/phase-04-platform-code
```
**Задачи:**
- ClipboardManager (expect/actual)

**Коммиты:**
```bash
git commit -m "feat(phase-04): добавить ClipboardManager expect"
git commit -m "feat(phase-04): реализовать ClipboardManager для Android"
git commit -m "feat(phase-04): реализовать ClipboardManager для iOS"
```

---

### Фаза 5: UI Тема
```bash
git checkout develop
git checkout -b feature/phase-05-ui-theme
```
**Задачи:**
- Цветовая схема
- MaterialTheme

**Коммиты:**
```bash
git commit -m "feat(phase-05): добавить цветовую схему"
git commit -m "feat(phase-05): настроить MaterialTheme"
```

---

### Фаза 6: UI Базовые компоненты
```bash
git checkout develop
git checkout -b feature/phase-06-ui-basic-components
```
**Задачи:**
- ColorSelector
- PieceSelector
- CurrentMoveDisplay

**Коммиты:**
```bash
git commit -m "feat(phase-06): добавить ColorSelector"
git commit -m "feat(phase-06): добавить PieceSelector"
git commit -m "feat(phase-06): добавить CurrentMoveDisplay"
```

---

### Фаза 7: UI Сетка и клавиатура
```bash
git checkout develop
git checkout -b feature/phase-07-ui-grid-keyboard
```
**Задачи:**
- SquaresGrid
- ChessKeyboard

**Коммиты:**
```bash
git commit -m "feat(phase-07): добавить SquaresGrid 8x8"
git commit -m "feat(phase-07): добавить ChessKeyboard"
```

---

### Фаза 8: UI Список ходов
```bash
git checkout develop
git checkout -b feature/phase-08-ui-moves-list
```
**Задачи:**
- MovesList
- MovesListWithCopy

**Коммиты:**
```bash
git commit -m "feat(phase-08): добавить MovesList"
git commit -m "feat(phase-08): добавить MovesListWithCopy с кнопкой"
git commit -m "feat(phase-08): добавить Snackbar для копирования"
```

---

### Фаза 9: Главный экран
```bash
git checkout develop
git checkout -b feature/phase-09-main-screen
```
**Задачи:**
- GameScreen
- App.kt

**Коммиты:**
```bash
git commit -m "feat(phase-09): добавить GameScreen"
git commit -m "feat(phase-09): добавить App.kt"
git commit -m "feat(phase-09): интегрировать все компоненты"
```

---

### Фаза 10: Android Entry Point
```bash
git checkout develop
git checkout -b feature/phase-10-android-app
```
**Задачи:**
- MainActivity
- Тестирование

**Коммиты:**
```bash
git commit -m "feat(phase-10): настроить MainActivity"
git commit -m "feat(phase-10): настроить AndroidManifest"
git commit -m "test(phase-10): тестирование на Android"
```

---

### Фаза 11: iOS Entry Point
```bash
git checkout develop
git checkout -b feature/phase-11-ios-app
```
**Задачи:**
- MainViewController
- ContentView.swift
- Тестирование

**Коммиты:**
```bash
git commit -m "feat(phase-11): добавить MainViewController"
git commit -m "feat(phase-11): настроить ContentView.swift"
git commit -m "test(phase-11): тестирование на iOS"
```

---

### Фаза 12: Дополнительные функции
```bash
git checkout develop
git checkout -b feature/phase-12-additional-features
```
**Задачи:**
- PGNExporter
- Настройки валидации

**Коммиты:**
```bash
git commit -m "feat(phase-12): добавить PGNExporter"
git commit -m "feat(phase-12): добавить настройки валидации"
```

---

### Фаза 13: Полировка и тестирование
```bash
git checkout develop
git checkout -b feature/phase-13-polish-testing
```
**Задачи:**
- UI/UX улучшения
- Тестирование
- Исправление багов

**Коммиты:**
```bash
git commit -m "refactor(phase-13): улучшить UI/UX"
git commit -m "test(phase-13): добавить UI тесты"
git commit -m "fix(phase-13): исправить баги"
```

---

### Фаза 14: Документация и релиз
```bash
git checkout develop
git checkout -b feature/phase-14-release
```
**Задачи:**
- Документация
- Release builds

**Коммиты:**
```bash
git commit -m "docs(phase-14): обновить README"
git commit -m "docs(phase-14): добавить скриншоты"
git commit -m "build(phase-14): создать release builds"
```

---

## 🎯 Финальный мерж в main

После завершения всех фаз:

```bash
# Переключиться на main
git checkout main

# Смержить develop в main
git merge develop

# Создать тег релиза
git tag -a v1.0.0 -m "Release version 1.0.0"

# Запушить в remote
git push origin main
git push origin v1.0.0
```

---

## 📝 Соглашения о коммитах

### Формат коммита:
```
<type>(<scope>): <subject>

<body> (опционально)
```

### Типы коммитов:
- `feat`: Новая функциональность
- `fix`: Исправление бага
- `refactor`: Рефакторинг кода
- `test`: Добавление тестов
- `docs`: Изменения в документации
- `style`: Форматирование кода
- `build`: Изменения в сборке
- `chore`: Рутинные задачи

### Примеры:
```bash
git commit -m "feat(phase-02): добавить PieceType с русской нотацией"
git commit -m "fix(phase-10): исправить копирование в буфер на Android"
git commit -m "refactor(phase-13): улучшить производительность SquaresGrid"
git commit -m "test(phase-03): добавить unit тесты для GameViewModel"
git commit -m "docs(phase-14): обновить README с примерами"
```

---

## 🔍 Проверка перед мержем

Перед мержем каждой фазы в `develop`:

```bash
# 1. Убедиться что все тесты проходят
./gradlew test

# 2. Убедиться что проект собирается
./gradlew build

# 3. Проверить что нет незакоммиченных изменений
git status

# 4. Проверить историю коммитов
git log --oneline

# 5. Только после этого мержить
git checkout develop
git merge feature/phase-XX
```

---

## 📊 Визуализация workflow

```
main
  │
  └─── v1.0.0 (tag)
       │
develop
  │
  ├─── feature/phase-01-project-setup ──┐
  │                                      │ merge
  ├──────────────────────────────────────┘
  │
  ├─── feature/phase-02-data-models ────┐
  │                                      │ merge
  ├──────────────────────────────────────┘
  │
  ├─── feature/phase-03-business-logic ─┐
  │                                      │ merge
  ├──────────────────────────────────────┘
  │
  ... (остальные фазы)
  │
  └─── merge в main → v1.0.0
```

---

## 🚀 Быстрый старт

```bash
# 1. Инициализировать Git (если еще не сделано)
git init

# 2. Создать начальный коммит
git add .
git commit -m "chore: initial commit with architecture docs"

# 3. Создать ветку develop
git checkout -b develop

# 4. Начать с первой фазы
git checkout -b feature/phase-01-project-setup

# 5. Работать над фазой...
# 6. После завершения фазы - мержить в develop
# 7. Переходить к следующей фазе
```

---

## ✅ Чеклист для каждой фазы

- [ ] Создать ветку `feature/phase-XX`
- [ ] Выполнить все задачи фазы
- [ ] Сделать осмысленные коммиты
- [ ] Запустить тесты (`./gradlew test`)
- [ ] Проверить сборку (`./gradlew build`)
- [ ] Смержить в `develop`
- [ ] Обновить TODO.md (отметить выполненные задачи)
- [ ] Удалить ветку фазы (опционально)

---

**Готово к работе!** 🎉

Начинаем с создания ветки для Фазы 1:
```bash
git checkout -b feature/phase-01-project-setup