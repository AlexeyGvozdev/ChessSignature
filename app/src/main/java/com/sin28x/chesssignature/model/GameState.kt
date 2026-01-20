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
        return (moves.size / 2) + 1
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