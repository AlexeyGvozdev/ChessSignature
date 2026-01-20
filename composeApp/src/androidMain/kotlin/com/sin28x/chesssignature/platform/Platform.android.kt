package com.sin28x.chesssignature.platform

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast

actual class Platform(private val context: Context) {
    actual fun copyToClipboard(text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Chess Moves", text)
        clipboard.setPrimaryClip(clip)
    }
    
    actual fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}