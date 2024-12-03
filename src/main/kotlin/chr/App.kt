package io.sebi

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.singleWindowApplication
import org.jetbrains.compose.reload.DevelopmentEntryPoint

fun main() {
    singleWindowApplication(
        alwaysOnTop = true
    ) {
        DevelopmentEntryPoint {
            Text("Text!")
        }
    }
}