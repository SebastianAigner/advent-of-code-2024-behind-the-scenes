package day06chr

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.singleWindowApplication
import androidx.compose.ui.zIndex
import day06pg.Vec2
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import org.jetbrains.compose.reload.DevelopmentEntryPoint
import java.io.File

const val day06Sample = """....#.....
.........#
..........
..#.......
.......#..
..........
.#..^.....
........#.
#.........
......#..."""


class Maze(input: String) {
    val cells = mutableStateMapOf<Int, Cell>()
    val width: Int
    val height: Int
    private var direction = Direction.North
    private var guard = 0 to 0

    init {
        val parsed = input.lines().map { it.toCharArray() }.toTypedArray()
        height = parsed.size
        width = parsed.first().size
        for (y in parsed.indices) {
            for (x in 0 until width) {
                cells[x + y * width] = when (parsed[y][x]) {
                    '.' -> Cell.Empty
                    '^' -> {
                        guard = y to x
                        Cell.Guard
                    }

                    '#' -> Cell.Obstruction
                    else -> Cell.Visited
                }
            }
        }
    }

    suspend fun solve(pause: Long) {
        while (true) {
            cells[guard.first * width + guard.second] = Cell.Visited
            var next = direction.move(guard)
            if (next.first !in 0 until height || next.second !in 0 until width) {
                break
            }
            if (cells[next.first * width + next.second] == Cell.Obstruction) {
                bumpFlow.emit(day06pg.Vec2(next.second, next.first))
                direction = direction.turn()
                next = direction.move(guard)
            }
            guard = next
            cells[guard.first * width + guard.second] = Cell.Guard
            delay(pause)
        }
    }
}

val bumpFlow = MutableSharedFlow<Vec2>()

@Composable
fun Maze(maze: Maze, cells: SnapshotStateMap<Int, Cell>, scale: Float) {
    Column(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .horizontalScroll(rememberScrollState())
            .verticalScroll(rememberScrollState())
            .semantics(mergeDescendants = true) {}) {
        for (i in 0 until maze.height) {
            Row(Modifier.zIndex(0f)) {
                for (j in 0 until maze.width) {
                    SingleCell(cells, j, i, maze, scale)
                }
            }
        }
    }
}

@Composable
private fun SingleCell(
    cells: SnapshotStateMap<Int, Cell>,
    j: Int, // y
    i: Int, // x
    maze: Maze,
    scale: Float,
) {
    val bumpScale = remember { androidx.compose.animation.core.Animatable(1f) }
    LaunchedEffect(Unit) {
        bumpFlow.filter { (x, y) ->
            y == i && x == j
        }.collect { value ->
            bumpScale.animateTo(1.5f, tween(durationMillis = 500))
            bumpScale.animateTo(1f, tween(durationMillis = 1000))
        }
    }

    val cell = cells[j + i * maze.width]!!
    val color = when (cell) {
        Cell.Guard -> Color.Red
        Cell.Obstruction -> Color.Black
        Cell.Empty -> Color.White
        Cell.Visited -> Color.Black.copy(0.05f)
    }

    val zIndex = if(bumpScale.value > 1.00f) {
        1f
    } else {
        0f
    }
    val coolC = if(bumpScale.value > 1.1f) {
        Color(0xFFffa500)
    } else {
        color
    }
    Box(
        modifier = Modifier.graphicsLayer {
            this.transformOrigin = TransformOrigin.Center
            this.scaleX = bumpScale.value
            this.scaleY = bumpScale.value
            this.clip = false
        }.size(scale.dp).background(coolC).clearAndSetSemantics { }.zIndex(zIndex),
    ) {
    }
}

enum class Cell(val presentation: String) {
    Guard("💂"),
    Obstruction("🎁"),
    Empty("⬜️"),
    Visited("🟩"),
}

enum class Direction {
    North, South, East, West
    ;

    fun move(coords: Pair<Int, Int>): Pair<Int, Int> {
        return when (this) {
            North -> coords.first - 1 to coords.second
            South -> coords.first + 1 to coords.second
            East -> coords.first to coords.second + 1
            West -> coords.first to coords.second - 1
        }
    }

    fun turn(): Direction {
        return when (this) {
            North -> East
            West -> North
            South -> West
            East -> South
        }
    }
}

@Composable
fun Day6() {
    var isSample by remember { mutableStateOf(true) }
    var scale by remember { mutableStateOf(8f) }
    var pause by remember { mutableStateOf(100f) }

    val maze by remember(isSample) { mutableStateOf(Maze(if (isSample) day06Sample else File("input/day06.txt").readText())) }
    LaunchedEffect(isSample) {
        maze.solve(pause.toLong())
    }
    Row(modifier = Modifier.fillMaxSize()) {
        Maze(maze, maze.cells, scale)
        Column {
            IconToggleButton(isSample, onCheckedChange = { isSample = it }) {
                Text(if (isSample) "🔮" else "🌟")
            }
            Text("Size")
            Slider(scale, onValueChange = { scale = it }, valueRange = 5f..20f, steps = 16)
            Text("Delay")
            Slider(pause, onValueChange = { pause = it }, valueRange = 1f..1000f, steps = 10)
            Text("Visited: ${maze.cells.count { it.value == Cell.Visited }}")
        }
    }
}

fun main() {
    singleWindowApplication(
        title = "Day 6",
        alwaysOnTop = true
    ) {
        DevelopmentEntryPoint {
            Day6()
        }
    }
}