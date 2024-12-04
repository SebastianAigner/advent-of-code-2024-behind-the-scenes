package io.sebi

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.singleWindowApplication
import day04chr.Grid
import org.jetbrains.compose.reload.DevelopmentEntryPoint
import java.io.File
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

val rawInput = File("input/day04.txt")
val input = rawInput.readLines()
val grid = Grid(input.map { it.toCharArray().toList() })

@Composable
fun App() {
    Box(Modifier.verticalScroll(rememberScrollState()).semantics(mergeDescendants = true){}) {
        Box(Modifier.horizontalScroll(rememberScrollState())) {
            Column(
            ) {
                for (y in grid.elems.indices) {
                    Row() {
                        for (x in grid.elems[0].indices) {
                            Text(grid.elems[y][x].toString())
                        }
                    }
                }
            }
        }
    }
}
