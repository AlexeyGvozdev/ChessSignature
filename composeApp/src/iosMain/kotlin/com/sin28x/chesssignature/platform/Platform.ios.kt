package com.sin28x.chesssignature.platform

import platform.UIKit.UIPasteboard
import platform.UIKit.UIAlertController
import platform.UIKit.UIAlertAction
import platform.UIKit.UIAlertActionStyleDefault
import platform.UIKit.UIApplication
import platform.UIKit.UIWindow

actual class Platform {
    actual fun copyToClipboard(text: String) {
        UIPasteboard.generalPasteboard.string = text
    }
    
    actual fun showToast(message: String) {
        val alert = UIAlertController.alertControllerWithTitle(
            title = null,
            message = message,
            preferredStyle = platform.UIKit.UIAlertControllerStyleAlert
        )
        
        val okAction = UIAlertAction.actionWithTitle(
            title = "OK",
            style = UIAlertActionStyleDefault,
            handler = null
        )
        
        alert.addAction(okAction)
        
        val window = UIApplication.sharedApplication.keyWindow
        window?.rootViewController?.presentViewController(
            alert,
            animated = true,
            completion = null
        )
    }
}