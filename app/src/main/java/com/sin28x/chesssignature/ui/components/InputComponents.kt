package com.sin28x.chesssignature.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sin28x.chesssignature.model.ValidationResult
import com.sin28x.chesssignature.ui.theme.ChessTypography
import com.sin28x.chesssignature.ui.theme.chessColors

/**
 * Поле отображения текущего ввода хода.
 * Показывает введенный текст с цветовой индикацией валидности.
 *
 * @param input Текущий введенный текст
 * @param validationResult Результат валидации ввода
 * @param modifier Модификатор для кастомизации
 */
@Composable
fun MoveInputField(
    input: String,
    validationResult: ValidationResult,
    modifier: Modifier = Modifier
) {
    val borderColor = when (validationResult) {
        is ValidationResult.Valid -> MaterialTheme.chessColors.validInput
        is ValidationResult.Invalid -> MaterialTheme.chessColors.invalidInput
        is ValidationResult.Incomplete -> MaterialTheme.chessColors.incompleteInput
    }
    
    val backgroundColor = when (validationResult) {
        is ValidationResult.Valid -> MaterialTheme.chessColors.validInput.copy(alpha = 0.1f)
        is ValidationResult.Invalid -> MaterialTheme.chessColors.invalidInput.copy(alpha = 0.1f)
        is ValidationResult.Incomplete -> MaterialTheme.colorScheme.surface
    }
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .border(
                border = BorderStroke(2.dp, borderColor),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 16.dp, vertical = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = input.ifEmpty { "Введите ход" },
            style = ChessTypography.inputField,
            color = if (input.isEmpty()) 
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
            else
                MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

/**
 * Индикатор валидации с иконкой и текстом.
 * Показывает статус валидации введенного хода.
 *
 * @param validationResult Результат валидации
 * @param modifier Модификатор для кастомизации
 */
@Composable
fun ValidationIndicator(
    validationResult: ValidationResult,
    modifier: Modifier = Modifier
) {
    val (iconColor, textColor, message) = when (validationResult) {
        is ValidationResult.Valid -> Triple(
            MaterialTheme.chessColors.validInput,
            MaterialTheme.chessColors.validInput,
            "Ход корректен"
        )
        is ValidationResult.Invalid -> Triple(
            MaterialTheme.chessColors.invalidInput,
            MaterialTheme.chessColors.invalidInput,
            validationResult.message.ifEmpty { "Неверный формат" }
        )
        is ValidationResult.Incomplete -> Triple(
            MaterialTheme.chessColors.incompleteInput,
            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            "Продолжайте ввод"
        )
    }
    
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Цветной индикатор
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(iconColor)
        )
        
        // Текст статуса
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = textColor
        )
    }
}

/**
 * Компактный индикатор валидации (только цветная точка).
 * Используется когда нужно сэкономить место.
 *
 * @param validationResult Результат валидации
 * @param modifier Модификатор для кастомизации
 */
@Composable
fun CompactValidationIndicator(
    validationResult: ValidationResult,
    modifier: Modifier = Modifier
) {
    val color = when (validationResult) {
        is ValidationResult.Valid -> MaterialTheme.chessColors.validInput
        is ValidationResult.Invalid -> MaterialTheme.chessColors.invalidInput
        is ValidationResult.Incomplete -> MaterialTheme.chessColors.incompleteInput
    }
    
    Box(
        modifier = modifier
            .size(16.dp)
            .clip(CircleShape)
            .background(color)
    )
}

/**
 * Поле ввода с встроенным индикатором валидации.
 * Комбинирует MoveInputField и ValidationIndicator.
 *
 * @param input Текущий введенный текст
 * @param validationResult Результат валидации ввода
 * @param modifier Модификатор для кастомизации
 * @param showValidationText Показывать ли текст валидации
 */
@Composable
fun MoveInputWithValidation(
    input: String,
    validationResult: ValidationResult,
    modifier: Modifier = Modifier,
    showValidationText: Boolean = true
) {
    Box(modifier = modifier) {
        MoveInputField(
            input = input,
            validationResult = validationResult,
            modifier = Modifier.fillMaxWidth()
        )
        
        if (showValidationText && input.isNotEmpty()) {
            ValidationIndicator(
                validationResult = validationResult,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 16.dp, bottom = 4.dp)
            )
        } else if (input.isNotEmpty()) {
            CompactValidationIndicator(
                validationResult = validationResult,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 12.dp, top = 12.dp)
            )
        }
    }
}