package com.sin28x.chesssignature.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sin28x.chesssignature.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel для управления состоянием шахматной игры.
 *
 * Отвечает за:
 * - Управление вводом ходов
 * - Валидацию введенных ходов
 * - Парсинг ходов в объекты Move
 * - Управление списком ходов
 * - Управление выбранной фигурой и цветом
 */
class GameViewModel : ViewModel() {
    
    /**
     * Внутреннее изменяемое состояние игры
     */
    private val _gameState = MutableStateFlow(GameState())
    
    /**
     * Публичное неизменяемое состояние игры для подписки из UI
     */
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()
    
    /**
     * Обновляет текущий ввод и валидирует его.
     * Если ход становится валидным, автоматически добавляет его в список.
     *
     * @param input Новая строка ввода хода (например, "e4", "Кf3", "0-0")
     */
    fun updateInput(input: String) {
        _gameState.update { currentState ->
            val validation = validateInput(input, currentState)
            val newState = currentState.copy(
                currentInput = input,
                inputValidation = validation
            )
            
            // Автоматически добавляем ход, если он валиден
            if (validation is ValidationResult.Valid) {
                val move = parseMove(input, newState)
                if (move != null) {
                    return@update newState.copy(
                        moves = newState.moves + move,
                        currentInput = "",
                        isWhiteTurn = !newState.isWhiteTurn,
                        inputValidation = ValidationResult.Incomplete
                    )
                }
            }
            
            newState
        }
    }
    
    /**
     * Добавляет клетку к текущему вводу с учетом выбранной фигуры.
     * Используется при клике на клетку доски.
     *
     * @param square Клетка доски для добавления к вводу
     */
    fun addSquare(square: ChessSquare) {
        val currentState = _gameState.value
        val currentInput = currentState.currentInput
        
        // Добавляем обозначение фигуры (если не пешка) + координаты клетки
        val pieceNotation = currentState.selectedPiece.toRussianNotation()
        val newInput = currentInput + pieceNotation + square.toString()
        
        updateInput(newInput)
    }
    
    /**
     * Добавляет специальный символ к вводу.
     *
     * @param symbol Символ для добавления (":", "+", "#", "0-0", "0-0-0")
     */
    fun addSymbol(symbol: String) {
        val currentInput = _gameState.value.currentInput
        val newInput = currentInput + symbol
        updateInput(newInput)
    }
    
    /**
     * Удаляет последний символ из ввода
     */
    fun deleteLastSymbol() {
        val currentInput = _gameState.value.currentInput
        if (currentInput.isNotEmpty()) {
            updateInput(currentInput.dropLast(1))
        }
    }
    
    /**
     * Полностью очищает текущий ввод.
     */
    fun clearInput() {
        updateInput("")
    }
    
    /**
     * Выбирает фигуру для ввода хода.
     *
     * @param piece Тип фигуры (PAWN, KNIGHT, BISHOP, ROOK, QUEEN, KING)
     */
    fun selectPiece(piece: PieceType) {
        _gameState.update { it.copy(selectedPiece = piece) }
    }
    
    /**
     * Переключает начальный цвет между белыми и черными.
     * Используется для выбора, кто ходит первым.
     */
    fun toggleStartingColor() {
        _gameState.update { currentState ->
            currentState.copy(
                startingColor = currentState.startingColor.opposite()
            )
        }
    }
    
    /**
     * Устанавливает начальный цвет для игры.
     *
     * @param color Цвет для первого хода
     */
    fun setStartingColor(color: PieceColor) {
        _gameState.update { currentState ->
            currentState.copy(
                startingColor = color,
                isWhiteTurn = (color == PieceColor.WHITE)
            )
        }
    }
    
    /**
     * Добавляет символ к текущему вводу.
     *
     * @param char Символ для добавления
     */
    fun addCharacter(char: String) {
        val currentInput = _gameState.value.currentInput
        val newInput = currentInput + char
        updateInput(newInput)
    }
    
    /**
     * Удаляет последний символ из ввода
     */
    fun deleteLastCharacter() {
        deleteLastSymbol()
    }
    
    /**
     * Очищает текущий ввод.
     */
    fun clearCurrentInput() {
        clearInput()
    }
    
    /**
     * Отправляет текущий ход (добавляет его в список)
     */
    fun submitMove() {
        addMove()
    }
    
    /**
     * Добавляет текущий ввод как новый ход в список ходов.
     * Ход добавляется только если валидация успешна.
     * После добавления ввод очищается и ход переходит к другому цвету.
     */
    fun addMove() {
        val currentState = _gameState.value
        
        if (!currentState.canAddMove()) return
        
        val move = parseMove(currentState.currentInput, currentState)
        
        if (move != null) {
            _gameState.update { state ->
                state.copy(
                    moves = state.moves + move,
                    currentInput = "",
                    isWhiteTurn = !state.isWhiteTurn,
                    inputValidation = ValidationResult.Incomplete
                )
            }
        }
    }
    
    /**
     * Удаляет последний ход из списка.
     * Также переключает ход обратно к предыдущему цвету.
     */
    fun deleteLastMove() {
        _gameState.update { currentState ->
            if (currentState.moves.isNotEmpty()) {
                currentState.copy(
                    moves = currentState.moves.dropLast(1),
                    isWhiteTurn = !currentState.isWhiteTurn
                )
            } else {
                currentState
            }
        }
    }
    
    /**
     * Очищает все ходы и сбрасывает игру в начальное состояние.
     * Ход возвращается к белым.
     */
    fun clearAllMoves() {
        _gameState.update { currentState ->
            currentState.copy(
                moves = emptyList(),
                currentInput = "",
                isWhiteTurn = (currentState.startingColor == PieceColor.WHITE),
                inputValidation = ValidationResult.Incomplete
            )
        }
    }
    
    /**
     * Валидирует введенную строку хода.
     *
     * @param input Строка ввода для валидации
     * @param state Текущее состояние игры
     * @return ValidationResult.Valid если ход корректен,
     *         ValidationResult.Incomplete если ввод неполный,
     *         ValidationResult.Invalid с сообщением об ошибке если ввод некорректен
     */
    private fun validateInput(input: String, state: GameState): ValidationResult {
        if (input.isEmpty()) {
            return ValidationResult.Incomplete
        }
        
        // Проверка на рокировку
        if (input == "0-0" || input == "0-0-0") {
            return ValidationResult.Valid
        }
        
        // Базовая валидация формата хода
        // Примеры валидных ходов:
        // - e4 (пешка на e4)
        // - e:d5 (пешка с вертикали e берет на d5)
        // - Кf3 (конь на f3)
        // - Кg1f3 (конь с g1 на f3)
        // - Л:d5 (ладья берет на d5)
        // - Фd1:h5+ (ферзь с d1 берет на h5 с шахом)
        // - e8=Ф (превращение пешки в ферзя)
        
        // Для фигур (не пешек): (Кр|Ф|Л|С|К)([a-h][1-8])?:?([a-h][1-8])(=[КФЛС])?([+#])?
        val piecePattern = Regex("""^(Кр|Ф|Л|С|К)([a-h][1-8])?:?([a-h][1-8])(=[КФЛС])?([+#])?$""")
        
        // Для пешек: ([a-h])?:?([a-h][1-8])(=[КФЛС])?([+#])?
        // Пешка может иметь только одну букву (вертикаль) перед взятием, но не полную клетку
        val pawnPattern = Regex("""^([a-h])?:?([a-h][1-8])(=[КФЛС])?([+#])?$""")
        
        return if (piecePattern.matches(input) || pawnPattern.matches(input)) {
            ValidationResult.Valid
        } else {
            // Проверяем, может ли это быть началом валидного хода
            // Разрешаем частичный ввод для удобства пользователя
            val partialPattern = Regex("""^(Кр|Ф|Л|С|К)?([a-h]?[1-8]?)?:?([a-h]?[1-8]?)?(=[КФЛС]?)?([+#])?$""")
            if (partialPattern.matches(input)) {
                ValidationResult.Incomplete
            } else {
                ValidationResult.Invalid("Неверный формат хода")
            }
        }
    }
    
    /**
     * Парсит строку хода в объект Move.
     *
     * @param input Строка хода для парсинга (например, "e4", "Кf3", "Фd1:h5+", "0-0")
     * @param state Текущее состояние игры для определения номера хода и цвета
     * @return Объект Move если парсинг успешен, null если строка не может быть распарсена
     */
    private fun parseMove(input: String, state: GameState): Move? {
        // Рокировка
        if (input == "0-0") {
            return Move(
                piece = PieceType.KING,
                to = if (state.getCurrentColor() == PieceColor.WHITE) 
                    ChessSquare('g', 1) else ChessSquare('g', 8),
                isCastlingKingside = true,
                moveNumber = state.getCurrentMoveNumber(),
                color = state.getCurrentColor()
            )
        }
        
        if (input == "0-0-0") {
            return Move(
                piece = PieceType.KING,
                to = if (state.getCurrentColor() == PieceColor.WHITE) 
                    ChessSquare('c', 1) else ChessSquare('c', 8),
                isCastlingQueenside = true,
                moveNumber = state.getCurrentMoveNumber(),
                color = state.getCurrentColor()
            )
        }
        
        // Парсинг обычного хода
        val movePattern = Regex("""^(Кр|Ф|Л|С|К)?([a-h][1-8])?(:)?([a-h][1-8])(=[КФЛС])?([+#])?$""")
        val match = movePattern.matchEntire(input) ?: return null
        
        val pieceNotation = match.groupValues[1]
        val fromSquare = match.groupValues[2]
        val isCapture = match.groupValues[3].isNotEmpty()
        val toSquare = match.groupValues[4]
        val promotionSymbol = match.groupValues[5]
        val checkSymbol = match.groupValues[6]
        
        // Парсим превращение пешки
        val promotion = if (promotionSymbol.isNotEmpty()) {
            val promotionPiece = promotionSymbol.removePrefix("=")
            PieceType.fromRussianNotation(promotionPiece)
        } else null
        
        val piece = if (pieceNotation.isEmpty()) {
            PieceType.PAWN
        } else {
            PieceType.fromRussianNotation(pieceNotation) ?: return null
        }
        
        val to = ChessSquare.fromString(toSquare) ?: return null
        val from = if (fromSquare.isNotEmpty()) {
            ChessSquare.fromString(fromSquare)
        } else null
        
        return Move(
            piece = piece,
            from = from,
            to = to,
            isCapture = isCapture,
            isCheck = checkSymbol == "+",
            isCheckmate = checkSymbol == "#",
            promotion = promotion,
            moveNumber = state.getCurrentMoveNumber(),
            color = state.getCurrentColor()
        )
    }
}