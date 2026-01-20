package com.sin28x.chesssignature.model

/**
 * Цвет шахматных фигур
 */
enum class PieceColor {
    WHITE,  // Белые
    BLACK;  // Черные

    /**
     * Возвращает противоположный цвет
     */
    fun opposite(): PieceColor = when (this) {
        WHITE -> BLACK
        BLACK -> WHITE
    }

    /**
     * Возвращает название цвета на русском
     */
    fun toRussianName(): String = when (this) {
        WHITE -> "Белые"
        BLACK -> "Черные"
    }
}