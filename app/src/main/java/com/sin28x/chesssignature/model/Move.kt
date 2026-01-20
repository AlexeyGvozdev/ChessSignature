package com.sin28x.chesssignature.model

/**
 * Представляет шахматный ход
 */
data class Move(
    val piece: PieceType,
    val from: ChessSquare? = null,
    val to: ChessSquare,
    val isCapture: Boolean = false,
    val isCheck: Boolean = false,
    val isCheckmate: Boolean = false,
    val promotion: PieceType? = null,
    val isCastlingKingside: Boolean = false,
    val isCastlingQueenside: Boolean = false,
    val moveNumber: Int,
    val color: PieceColor
) {
    /**
     * Конвертирует ход в русскую нотацию
     */
    fun toRussianNotation(): String = buildString {
        when {
            isCastlingKingside -> append("0-0")
            isCastlingQueenside -> append("0-0-0")
            else -> {
                append(piece.toRussianNotation())
                from?.let { append(it.toString()) }
                if (isCapture) append(":")
                append(to.toString())
                promotion?.let { append(it.toRussianNotation()) }
            }
        }
        
        when {
            isCheckmate -> append("#")
            isCheck -> append("+")
        }
    }

    /**
     * Конвертирует ход в английскую нотацию (PGN)
     */
    fun toEnglishNotation(): String = buildString {
        when {
            isCastlingKingside -> append("O-O")
            isCastlingQueenside -> append("O-O-O")
            else -> {
                append(piece.toEnglishNotation())
                from?.let { append(it.toString()) }
                if (isCapture) append("x")
                append(to.toString())
                promotion?.let { append("=${it.toEnglishNotation()}") }
            }
        }
        
        when {
            isCheckmate -> append("#")
            isCheck -> append("+")
        }
    }

    /**
     * Возвращает полную запись хода с номером
     */
    fun toFullNotation(): String {
        val notation = toRussianNotation()
        return if (color == PieceColor.WHITE) {
            "$moveNumber. $notation"
        } else {
            "$moveNumber... $notation"
        }
    }
}