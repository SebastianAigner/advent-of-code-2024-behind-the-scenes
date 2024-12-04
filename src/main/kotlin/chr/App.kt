package io.sebi

import androidx.compose.animation.Animatable
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalAccessibilityManager
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.platform.LocalAccessibilityManager
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.singleWindowApplication
import day04chr.ColorEvent
import day04chr.Grid
import day04chr.Vec2
import day04chr.colorFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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

val CROP = 40
val rawInput = File("input/day04.txt")
val input = rawInput.readLines()
val grid = Grid(
    input.take(CROP).map {
        it.toCharArray().toList().take(CROP)
    }
)

@Composable
fun App() {
    val colorMap = remember { mutableStateMapOf<Vec2, ColorEvent>() }
    val lastTouchedMap = remember { mutableStateMapOf<Vec2, Long>() }
    LaunchedEffect(Unit) {
        colorFlow.collect {
            println(it)
            lastTouchedMap[it.loc] = System.currentTimeMillis()
            val curr = colorMap[it.loc]
            if (curr != null) {
                if (curr.priority > it.priority) {
                    println("prios ${curr.priority} ${it.priority}")
                    colorMap[it.loc] = it
                }
            } else {
                colorMap[it.loc] = it
            }
        }
    }
    Box(
        Modifier.background(Color.LightGray).verticalScroll(rememberScrollState())
            .semantics(mergeDescendants = true) {}) {
        Box(Modifier.horizontalScroll(rememberScrollState())) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                for (y in grid.elems.indices) {
                    Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                        for (x in grid.elems[0].indices) {
                            GridCell(
                                letter = grid.elems[y][x],
                                color = colorMap[Vec2(x, y)]?.color ?: Color.White,
                                lastTouched = lastTouchedMap[Vec2(x, y)] ?: 0L,
                            )
                        }
                    }
                }
            }
        }
        val coroutineScope = rememberCoroutineScope()
        Column(
            modifier = Modifier.align(Alignment.BottomEnd),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(
                onClick = {
                    coroutineScope.launch {
                        grid.indices.forEach {
                            grid.countXmasWordsForPosition(it.first, it.second)
                        }
                    }
                }
            ) {
                Text("Find words")
            }
            Button(
                onClick = {
                    coroutineScope.launch {
                        grid.indices.drop(CROP * CROP / 2).forEach {
                            grid.countXmasWordsForPosition(it.first, it.second)
                        }
                    }
                }
            ) {
                Text("Find words (2)")
            }
            Button(
                onClick = {
                    coroutineScope.launch {
                        grid.indices.forEach {
                            grid.isMASCrossAtPosition(it.first, it.second)
                        }
                    }
                }
            ) {
                Text("Find MAS-crosses")
            }
            Button(
                onClick = {
                    coroutineScope.launch {
                        grid.indices.drop(CROP * CROP / 2).forEach {
                            grid.isMASCrossAtPosition(it.first, it.second)
                        }
                    }
                }
            ) {
                Text("Find MAS-crosses (2)")
            }
        }
    }
}


@Composable
fun GridCell(letter: Char, color: Color, lastTouched: Long = 0L) {
    val scale = remember { androidx.compose.animation.core.Animatable(1f) }
    LaunchedEffect(lastTouched) {
        if (lastTouched == 0L) return@LaunchedEffect
        scale.animateTo(1.5f, tween(durationMillis = 100))
        scale.animateTo(1f, tween(durationMillis = 30))
    }

    val animatedColor by animateColorAsState(targetValue = color, animationSpec = tween(durationMillis = 500))
    val modifier =
        if (scale.value > 1.25f) {
            Modifier.background(Color.Red)
        } else {
            Modifier
        }

    Box(
        Modifier.size(20.dp)
            .background(animatedColor)
            .then(modifier),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Text(
            letter.toString(),
            Modifier.clearAndSetSemantics { }.scale(scale.value),
            fontFamily = FontFamily.Monospace
        )
    }
}