package com.sin28x.chesssignature.model

/**
 * Состояние игры
 */
data class GameState(
    val moves: List<Move> = emptyList(),
    val currentInput: String = "",
    val isWhiteTurn: Boolean = true,
    val selectedPiece: PieceType = PieceType.PAWN,
    val startingColor: PieceColor = PieceColor.WHITE,
    val inputValidation: ValidationResult = ValidationResult.Incomplete
) {
    /**
     * Возвращает номер текущего хода
     */
    fun getCurrentMoveNumber(): Int {
        // Если первыми ходят белые: стандартная нумерация
        // 1-й ход белых = номер 1, 1-й ход черных = номер 1
        // 2-й ход белых = номер 2, 2-й ход черных = номер 2
        if (startingColor == PieceColor.WHITE) {
            return (moves.size / 2) + 1
        }
        
        // Если первыми ходят черные:
        // 1-й ход черных = номер 1
        // 1-й ход белых = номер 2
        // 2-й ход черных = номер 2
        // 2-й ход белых = номер 3
        return if (isWhiteTurn) {
            // Ход белых: moves.size - количество уже сделанных ходов
            // Если moves.size = 1 (черные сходили), белые ходят номер 2
            // Если moves.size = 3 (черные-белые-черные), белые ходят номер 3
            ((moves.size + 1) / 2) + 1
        } else {
            // Ход черных:
            // Если moves.size = 0, черные ходят номер 1
            // Если moves.size = 2 (черные-белые), черные ходят номер 2
            (moves.size / 2) + 1
        }
    }
    
    /**
     * Возвращает цвет текущего хода
     */
    fun getCurrentColor(): PieceColor {
        return if (isWhiteTurn) PieceColor.WHITE else PieceColor.BLACK
    }
    
    /**
     * Возвращает все ходы в виде строки (русская нотация)
     */
    fun getMovesAsRussianNotation(): String {
        return moves.joinToString(" ") { it.toFullNotation() }
    }
    
    /**
     * Возвращает все ходы в виде строки (английская нотация / PGN)
     */
    fun getMovesAsPGN(): String {
        return moves.chunked(2).joinToString(" ") { pair ->
            val moveNumber = (moves.indexOf(pair.first()) / 2) + 1
            val white = pair.first().toEnglishNotation()
            val black = pair.getOrNull(1)?.toEnglishNotation() ?: ""
            if (black.isNotEmpty()) {
                "$moveNumber. $white $black"
            } else {
                "$moveNumber. $white"
            }
        }
    }
    
    /**
     * Проверяет, можно ли добавить новый ход
     */
    fun canAddMove(): Boolean {
        return inputValidation.isValid() && currentInput.isNotEmpty()
    }
}