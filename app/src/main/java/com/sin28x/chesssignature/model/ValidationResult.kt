package com.sin28x.chesssignature.model

/**
 * Результат валидации ввода хода
 */
sealed class ValidationResult {
    /**
     * Ввод валиден
     */
    data object Valid : ValidationResult()
    
    /**
     * Ввод в процессе (неполный, но пока корректный)
     */
    data object Incomplete : ValidationResult()
    
    /**
     * Ввод невалиден
     * @param message Сообщение об ошибке
     */
    data class Invalid(val message: String) : ValidationResult()
    
    /**
     * Проверяет, является ли результат валидным
     */
    fun isValid(): Boolean = this is Valid
    
    /**
     * Проверяет, является ли результат невалидным
     */
    fun isInvalid(): Boolean = this is Invalid
    
    /**
     * Проверяет, является ли результат неполным
     */
    fun isIncomplete(): Boolean = this is Incomplete
}