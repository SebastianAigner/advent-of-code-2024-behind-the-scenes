package co.zsmb

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.singleWindowApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

val example = """
    RRRRIICCFF
    RRRRIICCCF
    VVRRRCCFFF
    VVRCCCJFFF
    VVVVCJJCFE
    VVIVCCJJEE
    VVIIICJJEE
    MIIIIIJJEE
    MIIISIJEEE
    MMMISSJEEE
""".trimIndent()

data class Vec2(val x: Int, val y: Int)

data class ColorEvent(val loc: Vec2, val color: Color, val priority: Int)

val colorFlow = MutableSharedFlow<ColorEvent>()

val colorMap = mutableStateMapOf<Vec2, ColorEvent>()

var rawInput by mutableStateOf(example)

fun main() {
    singleWindowApplication {
        App()
    }
}

@Composable
fun App() {
    val input = remember(rawInput) {
        colorReset()
        rawInput.lines()
    }

    val xRange = input[0].indices
    val yRange = input.indices

    fun get(x: Int, y: Int) = if (x !in xRange || y !in yRange) null else input.getOrNull(y)?.getOrNull(x)

    val defDensity = LocalDensity.current.density
    var density by remember { mutableFloatStateOf(defDensity) }

    var fontSize by remember { mutableFloatStateOf(12f) }

    val lastTouchedMap = remember { mutableStateMapOf<Vec2, Long>() }
    LaunchedEffect(Unit) {
        colorFlow.collect {
            lastTouchedMap[it.loc] = System.currentTimeMillis()
            val curr = colorMap[it.loc]
            if (curr != null) {
                if (curr.priority < it.priority) {
                    colorMap[it.loc] = it
                }
            } else {
                colorMap[it.loc] = it
            }
        }
    }
    Row(
        Modifier.fillMaxSize()
            .background(Color.LightGray),
//            .semantics(mergeDescendants = true) {}
//            .clearAndSetSemantics {}
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            Modifier
                .graphicsLayer {
                    scaleX = density
                    scaleY = density
                    transformOrigin = TransformOrigin(0f, 0f)
                }
                .aspectRatio(1f)
                .fillMaxSize()
                .weight(1f),
        ) {
            val fsizeSp = fontSize.sp
            for (y in yRange) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    for (x in xRange) {
                        GridCell(
                            letter = get(x, y)!!,
                            color = colorMap[Vec2(x, y)]?.color ?: Color.White,
                            fontSize = fsizeSp,
                            modifier = Modifier.aspectRatio(1f).weight(1f),
                        )
                    }
                }
            }
        }

        ControlPanel(
            input = rawInput,
            onInputChange = { rawInput = it },
            scale = density,
            onScaleChange = { density = it },
            fontSize = fontSize,
            onFontSizeChange = { fontSize = it },
        )
    }
}

@Composable
private fun ControlPanel(
    input: String,
    onInputChange: (String) -> Unit,
    scale: Float,
    onScaleChange: (Float) -> Unit,
    fontSize: Float,
    onFontSizeChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope({ Dispatchers.Default })

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White).padding(10.dp).width(150.dp),
        horizontalAlignment = Alignment.End,
    ) {
        TextField(input, onInputChange, modifier = Modifier.fillMaxWidth().height(100.dp))
        Button(onClick = {
            rawInput = example
        }) {
            Text("Reset input")
        }
        Button(
            onClick = {
                coroutineScope.launch {
                    part1(input.lines())
                }
            }
        ) {
            Text("Part 1")
        }
        Button(
            onClick = {
                coroutineScope.launch {
                    part2(input.lines())
                }
            }
        ) {
            Text("Part 2")
        }

        Text("Zoom")
        Slider(scale, onScaleChange, valueRange = 0.01f..3f)
        Text("Font size")
        Slider(fontSize, onFontSizeChange, valueRange = 4f..20f)
    }
}


@Composable
fun GridCell(
    letter: Char,
    color: Color,
    fontSize: TextUnit,
    modifier: Modifier = Modifier,
) {
    val animatedColor by animateColorAsState(
        targetValue = color,
        animationSpec = tween(durationMillis = DELAY.toInt() * 10)
    )

    Box(
        modifier
            .size(20.dp)
            .background(Color.LightGray)
            .padding(1.dp)
            .background(animatedColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            letter.toString(),
            fontSize = fontSize,
            fontFamily = FontFamily.Monospace
        )
    }
}


data class Coords(val x: Int, val y: Int)

val DELAY = 20L

suspend fun part1(input: List<String>): Int {
    colorReset()

    val xRange = input[0].indices
    val yRange = input.indices

    fun get(x: Int, y: Int) = input[y][x]

    suspend fun visit(visited: MutableSet<Coords>, x: Int, y: Int, char: Char): Int {
        val dis = Coords(x, y)
        if (dis in visited) {
            return 0
        }
        if (x !in xRange || y !in yRange || get(x, y) != char) {
            return 1
        }

        colorFlow.emit(ColorEvent(Vec2(x, y), Color.DarkGray, 0))
        delay(DELAY)

        visited += Coords(x, y)

        val offsets = listOf(
            0 to 1,
            0 to -1,
            1 to 0,
            -1 to 0,
        )
        return offsets.sumOf { (dx, dy) ->
            visit(visited, x + dx, y + dy, char)
        }
    }


    val allVisited = mutableSetOf<Coords>()
    var sum = 0
    for (y in yRange) {
        for (x in xRange) {
            if (Coords(x, y) !in allVisited) {
                val visited = mutableSetOf<Coords>()
                val perimeter = visit(visited, x, y, input[y][x])


                val c = colors[cindex]
                cindex = (cindex + 1) % colors.size
                visited.forEach {
                    colorFlow.emit(ColorEvent(Vec2(it.x, it.y), c, 1))
                }

                sum += visited.size * perimeter

                allVisited.addAll(visited)
            }
        }
    }
    return sum
}

var cindex = 0
val colors = listOf(
    Color(0xFFFCF84A),
    Color(0xFFFDB60D),
    Color(0xFFFC801D),
    Color(0xFFFE2857),
    Color(0xFFDD1265),
    Color(0xFFFF318C),
    Color(0xFFFF45ED),
    Color(0xFFAF1DF5),
    Color(0xFF6B57FF),
    Color(0xFF087CFA),
    Color(0xFF07C3F2),
    Color(0xFF21D789),
    Color(0xFF3DEA62),
)

fun colorReset() {
    cindex = 0
    colorMap.clear()
}

suspend fun part2(input: List<String>): Int {
    val xRange = input[0].indices
    val yRange = input.indices

    fun get(x: Int, y: Int) = if (x !in xRange || y !in yRange) null else input[y][x]

    fun visit(visited: MutableSet<Coords>, x: Int, y: Int, char: Char) {
        val dis = Coords(x, y)
        if (dis in visited) {
            return
        }
        if (x !in xRange || y !in yRange || get(x, y) != char) {
            return
        }
        visited += Coords(x, y)

        val offsets = listOf(
            0 to 1,
            0 to -1,
            1 to 0,
            -1 to 0,
        )
        offsets.forEach { (dx, dy) ->
            visit(visited, x + dx, y + dy, char)
        }
    }

    fun Coords.corners(char: Char): Int {
        var c = 0

        // top left, outer then inner
        if (get(x - 1, y) != char && get(x, y - 1) != char)
            c++
        if (get(x - 1, y) == char && get(x, y - 1) == char && get(x - 1, y - 1) != char)
            c++

        // top right, outer then inner
        if (get(x + 1, y) != char && get(x, y - 1) != char)
            c++
        if (get(x + 1, y) == char && get(x, y - 1) == char && get(x + 1, y - 1) != char)
            c++

        // bottom left, outer then inner
        if (get(x - 1, y) != char && get(x, y + 1) != char)
            c++
        if (get(x - 1, y) == char && get(x, y + 1) == char && get(x - 1, y + 1) != char)
            c++

        // bottom right, outer then inner
        if (get(x + 1, y) != char && get(x, y + 1) != char)
            c++
        if (get(x + 1, y) == char && get(x, y + 1) == char && get(x + 1, y + 1) != char)
            c++

        return c
    }

    val allVisited = mutableSetOf<Coords>()
    var sum = 0
    for (y in yRange) {
        for (x in xRange) {
            if (Coords(x, y) !in allVisited) {
                val visited = mutableSetOf<Coords>()
                visit(visited, x, y, get(x, y)!!)

                val c = colors[cindex]
                cindex = (cindex + 1) % colors.size
                visited.forEach {
                    colorFlow.emit(ColorEvent(Vec2(it.x, it.y), c, 1))
                }

                delay(DELAY)
                delay(DELAY)

                val corners = visited.sumOf { it.corners(get(x, y)!!) }

                sum += visited.size * corners

                allVisited.addAll(visited)
            }
        }
    }
    return sum
}