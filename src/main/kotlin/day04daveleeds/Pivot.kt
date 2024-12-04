package lib.string

fun List<String>.pivot(): List<String> {
    val characters = this
    return buildList {
        for (x in characters[0].indices) {
            add(buildString {
                for (y in characters.indices) {
                    append(characters[y][x])
                }
            })
        }
    }
}
