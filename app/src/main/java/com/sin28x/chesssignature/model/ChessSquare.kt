package com.sin28x.chesssignature.model

/**
 * Представляет клетку на шахматной доске
 * @param file Вертикаль (a-h)
 * @param rank Горизонталь (1-8)
 */
data class ChessSquare(
    val file: Char,
    val rank: Int
) {
    init {
        require(file in 'a'..'h') { "File must be between 'a' and 'h'" }
        require(rank in 1..8) { "Rank must be between 1 and 8" }
    }

    /**
     * Возвращает строковое представление клетки (например, "e4")
     */
    override fun toString(): String = "$file$rank"

    companion object {
        /**
         * Создает ChessSquare из строки (например, "e4")
         */
        fun fromString(square: String): ChessSquare? {
            if (square.length != 2) return null
            val file = square[0].lowercaseChar()
            val rank = square[1].digitToIntOrNull() ?: return null
            
            return try {
                ChessSquare(file, rank)
            } catch (e: IllegalArgumentException) {
                null
            }
        }

        /**
         * Возвращает все клетки доски
         */
        fun allSquares(): List<ChessSquare> = buildList {
            for (rank in 8 downTo 1) {
                for (file in 'a'..'h') {
                    add(ChessSquare(file, rank))
                }
            }
        }
    }
}