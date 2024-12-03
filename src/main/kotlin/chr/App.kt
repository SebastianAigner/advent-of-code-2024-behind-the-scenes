package io.sebi

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.singleWindowApplication
import org.jetbrains.compose.reload.DevelopmentEntryPoint
import java.util.regex.PatternSyntaxException

fun main() {
    singleWindowApplication(
        alwaysOnTop = true
    ) {
        DevelopmentEntryPoint {
            App()
        }
    }
}

@Composable
fun App() {
    val enteredRegex = remember { mutableStateOf("") }
    Column {
        Text(
            modifier = Modifier.animateContentSize(),
            text = "I'm never gonna let you down!",
            fontSize = 50.sp,
            color = Color.Magenta,
        )
        Button(onClick = {}) {
            Text("Click me")
        }
        TextField(
            value = enteredRegex.value,
            onValueChange = { enteredRegex.value = it }
        )
        Button(onClick = {
            val foo = try {
                enteredRegex.value.toRegex()
            } catch (e: PatternSyntaxException) {
                println("not valid!")
                return@Button
            }
            println("now $foo")
        }) {
            Text("Advent me")
        }
    }
}
