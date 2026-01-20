package com.sin28x.chesssignature.platform

/**
 * Платформенно-специфичные функции
 */
expect class Platform {
    /**
     * Копирует текст в буфер обмена
     */
    fun copyToClipboard(text: String)
    
    /**
     * Показывает короткое уведомление (Toast на Android, Alert на iOS)
     */
    fun showToast(message: String)
}