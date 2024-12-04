package lib.string

fun List<String>.tippedRight(): List<String> {
    val characters = this
    val width = characters.first().length
    val height = characters.size
    return buildList {
        for (y in characters.indices) {
            add(buildString {
                var cx = 0
                var cy = y
                do {
                    append(characters[cy][cx])
                    cx += 1
                    cy -= 1
                } while (cx < width && cy >= 0)
            })
        }
        for (x in characters[0].indices.drop(1)) {
            add(buildString {
                var cx = x
                var cy = height - 1
                do {
                    append(characters[cy][cx])
                    cx += 1
                    cy -= 1
                } while (cx < width && cy >= 0)
            })
        }
    }
}

fun List<String>.tippedLeft(): List<String> {
    val characters = this
    val width = characters.first().length
    val height = characters.size
    return buildList {
        for (x in characters[0].indices.reversed()) {
            add(buildString {
                var cx = x
                var cy = 0
                do {
                    append(characters[cy][cx])
                    cx += 1
                    cy += 1
                } while (cx < width && cy < height)
            })
        }
        for (y in characters.indices.drop(1)) {
            add(buildString {
                var cx = 0
                var cy = y
                do {
                    append(characters[cy][cx])
                    cx += 1
                    cy += 1
                } while (cx < width && cy < height)
            })
        }
    }
}
