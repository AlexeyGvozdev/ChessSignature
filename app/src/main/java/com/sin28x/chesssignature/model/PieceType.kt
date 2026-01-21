package com.sin28x.chesssignature.model

/**
 * Типы шахматных фигур
 */
enum class PieceType {
    PAWN,    // Пешка
    KNIGHT,  // Конь
    BISHOP,  // Слон
    ROOK,    // Ладья
    QUEEN,   // Ферзь
    KING;    // Король

    /**
     * Конвертирует тип фигуры в русскую нотацию
     */
    fun toRussianNotation(): String = when (this) {
        PAWN -> ""      // Пешка не обозначается
        KNIGHT -> "К"   // Конь
        BISHOP -> "С"   // Слон
        ROOK -> "Л"     // Ладья
        QUEEN -> "Ф"    // Ферзь
        KING -> "Кр"    // Король
    }

    /**
     * Конвертирует тип фигуры в английскую нотацию (для PGN)
     */
    fun toEnglishNotation(): String = when (this) {
        PAWN -> ""
        KNIGHT -> "N"
        BISHOP -> "B"
        ROOK -> "R"
        QUEEN -> "Q"
        KING -> "K"
    }

    /**
     * Возвращает отображаемое название фигуры для UI
     */
    fun toDisplayString(): String = when (this) {
        PAWN -> "Пешка"
        KNIGHT -> "Конь"
        BISHOP -> "Слон"
        ROOK -> "Ладья"
        QUEEN -> "Ферзь"
        KING -> "Король"
    }

    companion object {
        /**
         * Создает PieceType из русской нотации
         */
        fun fromRussianNotation(notation: String): PieceType? = when (notation) {
            "" -> PAWN
            "К" -> KNIGHT
            "С" -> BISHOP
            "Л" -> ROOK
            "Ф" -> QUEEN
            "Кр" -> KING
            else -> null
        }
    }
}