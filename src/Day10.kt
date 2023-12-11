import kotlin.math.abs

enum class Direction(val ind: Int) {
    NORTH(1), SOUTH(-1), WEST(1), EAST(-1)
}

fun callExplore(y: Int, x: Int, puzzleMatrix: Array<Array<Char>>, valueMatrix: Array<Array<Int>>, valueToIns: Int, orgDirection: Direction) {
    if ((valueMatrix[y][x] == -1 || valueToIns < valueMatrix[y][x]) && x < puzzleMatrix[0].size && y < puzzleMatrix.size) {
        //valueMatrix.forEach{value -> value.forEach{print("$it ")}; print("\n")}
        valueMatrix[y][x] = valueToIns
        explore(y, x, puzzleMatrix, valueMatrix, orgDirection)
    }

}


fun explore(y: Int, x: Int, puzzleMatrix: Array<Array<Char>>, valueMatrix: Array<Array<Int>>, orgDirection: Direction) {
    println("Exploring at y:$y, x:$x from $orgDirection, character ${puzzleMatrix[y][x]}")


    when (puzzleMatrix[y][x]) {
        '.' -> return
        'S' -> return
        '|' -> if (orgDirection == Direction.NORTH || orgDirection == Direction.SOUTH) callExplore(y + orgDirection.ind, x, puzzleMatrix, valueMatrix, valueMatrix[y][x] + 1, orgDirection)
        '-' -> if (orgDirection == Direction.WEST || orgDirection == Direction.EAST) callExplore(y, x + orgDirection.ind, puzzleMatrix, valueMatrix, valueMatrix[y][x] + 1, orgDirection)
        'J' -> when (orgDirection) {
            Direction.NORTH -> callExplore(y, x - 1, puzzleMatrix, valueMatrix, valueMatrix[y][x] + 1, Direction.EAST)
            Direction.WEST -> callExplore(y - 1, x, puzzleMatrix, valueMatrix, valueMatrix[y][x] + 1, Direction.SOUTH)
            else -> return
        }

        'L' -> when (orgDirection) {
            Direction.NORTH -> callExplore(y, x + 1, puzzleMatrix, valueMatrix, valueMatrix[y][x] + 1, Direction.WEST)
            Direction.EAST -> callExplore(y - 1, x, puzzleMatrix, valueMatrix, valueMatrix[y][x] + 1, Direction.SOUTH)
            else -> return
        }

        '7' -> when (orgDirection) {
            Direction.SOUTH -> callExplore(y, x - 1, puzzleMatrix, valueMatrix, valueMatrix[y][x] + 1, Direction.EAST)
            Direction.WEST -> callExplore(y + 1, x, puzzleMatrix, valueMatrix, valueMatrix[y][x] + 1, Direction.NORTH)
            else -> return
        }

        'F' -> when (orgDirection) {
            Direction.SOUTH -> callExplore(y, x + 1, puzzleMatrix, valueMatrix, valueMatrix[y][x] + 1, Direction.WEST)
            Direction.EAST -> callExplore(y + 1, x, puzzleMatrix, valueMatrix, valueMatrix[y][x] + 1, Direction.NORTH)
            else -> return
        }
    }
}

fun main() {

    val input = readInput("Day10")
    println(input)

    val matrix = Array(input.size) { Array(input[it].length) { char -> input[it][char] } }

    var sx = 0
    var sy = 0
    matrix.forEachIndexed { y, value ->
        value.forEachIndexed { x, c ->
            if (c == 'S') {
                sx = x; sy = y
            }
        }
    }
    println("Found S at $sx:$sy")

    val valueMatrix = Array(matrix.size) { Array(matrix[it].size) { -1 } }
    valueMatrix[sy][sx] = 0


    if (sx + 1 < matrix[0].size && matrix[sy][sx + 1] in listOf('-', '7', 'J')) callExplore(sy, sx + 1, matrix, valueMatrix, 1, Direction.WEST)
    if (sx - 1 > 0 && matrix[sy][sx - 1] in listOf('-', 'F', 'L')) callExplore(sy, sx - 1, matrix, valueMatrix, 1, Direction.EAST)
    if (sy + 1 < matrix.size && matrix[sy + 1][sx] in listOf('|', 'J', 'L')) callExplore(sy + 1, sx, matrix, valueMatrix, 1, Direction.NORTH)
    if (sy - 1 > 0 && matrix[sy - 1][sx] in listOf('|', '7', 'F')) callExplore(sy - 1, sx, matrix, valueMatrix, 1, Direction.SOUTH)


    fun reachBorder(
        y: Int,
        x: Int,
        floodTable: Array<Array<Boolean>>,
        arguments: MutableList<Pair<Int, Int>> = mutableListOf(Pair(y + 1, x), Pair(y, x + 1), Pair(y, x - 1), Pair(y - 1, x), Pair(y + 1, x + 1), Pair(y - 1, x - 1), Pair(y + 1, x - 1), Pair(y - 1, x + 1))
    ): Int {

        if (y >= valueMatrix.size || y < 0 || x >= valueMatrix[0].size || x < 0) return -1
        if (floodTable[y][x]) return -1
        else if (valueMatrix[y][x] != -1) return -1

        if ((y == 0 || y == valueMatrix.size - 1) || (x == valueMatrix[0].size - 1 || x == 0)) {
            return 1
        }

        floodTable[y][x] = true
        if (arguments.withIndex().any { (_, argument) ->
                reachBorder(argument.first, argument.second, floodTable) == 1
            }) return 1

        return -1
    }

    fun countHorz(letters: List<Char>, startY: Int, x: Int): Int {
        val pairs = letters.filterIndexed { index, v -> valueMatrix[index + startY][x] != -1 && v in listOf('F', 'L', 'J', '7') }.withIndex()
            .groupBy { it.index / 2 }
            .map { it.value.map { it.value } }

        //println(pairs)
        var count = 0

        pairs.forEach {
            when (it) {
                listOf('F', 'L') -> return@forEach
                listOf('7', 'J') -> return@forEach
                listOf('7', 'L') -> count += 2
                listOf('F', 'J') -> count += 2
                else -> println("ERROR: UNKOWN COMBO ${it}")
            }
        }

        return count
    }

    fun tubesCheck(y: Int, x: Int, count: Double, orgDirection: Direction): Double {
        var newCount = count

        if (valueMatrix[y][x] != -1) {
            when (orgDirection) {
                Direction.NORTH, Direction.SOUTH -> {
                    if (matrix[y][x] == '-') newCount += 2
                    // Use countHorz for symbols
                    //if (matrix[y][x] in listOf('F', 'J')) newCount += 1
                    //else if (matrix[y][x] in listOf('7', 'L')) newCount -= 1
                    //if (matrix[y][x] == 'S' && matrix[y][x+1] in listOf('-', '7', 'J') && matrix[y][x-1] in listOf('-', 'L', 'F')) newCount += 2
                }

                Direction.EAST, Direction.WEST -> {
                    if (matrix[y][x] == '|') newCount += 2
                    if (matrix[y][x] in listOf('7', 'L')) newCount += 1
                    else if (matrix[y][x] in listOf('J', 'F')) newCount -= 1

                    //if (matrix[y][x] == 'S' && matrix[y-1][x] in listOf('|', '7', 'F') && matrix[y + 1][x] in listOf('|', 'J', 'L')) newCount += 2
                }
            }
            //if(matrix[y][x] == 'S') newCount += 2
            //if (matrix[y][x] in symbs) newCount += 1
        }

        // Check if it has reached the border
        if ((y == 0 || y == valueMatrix.size - 1) || (x == valueMatrix[0].size - 1 || x == 0)) {
            return abs(newCount)
        } else {
            // Determine next coordinates based on direction
            val nextX = if (orgDirection == Direction.EAST || orgDirection == Direction.WEST) x + orgDirection.ind else x
            val nextY = if (orgDirection == Direction.NORTH || orgDirection == Direction.SOUTH) y + orgDirection.ind else y

            // Recursive call
            return tubesCheck(nextY, nextX, newCount, orgDirection)
        }
    }
    
    println("Part 1: ${valueMatrix.maxOf { row -> row.maxOf { it } }}")


    matrix[sy][sx] = '|'
    valueMatrix.forEachIndexed { y, row ->
        row.forEachIndexed { x, _ ->
            print("%5d ".format(valueMatrix[y][x]))
        }
        println()
    }

    val loopMatrix = matrix.clone()

    valueMatrix.forEachIndexed { y, row ->
        row.forEachIndexed { x, value ->
            if (value == -1) {
                //println("Checking char $value at $y,$x...")
                val floodTable = Array(valueMatrix.size) { Array(input[it].length) { false } }
                if (reachBorder(y, x, floodTable) == 1)
                    loopMatrix[y][x] = 'O'
                else if (
                    (tubesCheck(y, x, 0.0, Direction.NORTH) + countHorz(matrix.sliceArray(y + 1..<matrix.size).map { it[x] }, y+1, x)) % 4 == 0.0 &&
                    (tubesCheck(y, x, 0.0, Direction.SOUTH) + countHorz(matrix.sliceArray(0..<y).map { it[x] }, 0, x)) % 4 == 0.0 &&
                    tubesCheck(y, x, 0.0, Direction.EAST) % 4 == 0.0
                    && tubesCheck(y, x, 0.0, Direction.WEST) % 4 == 0.0
                ) {
                    if (
                        tubesCheck(y, x, 0.0, Direction.EAST) % 4 == 0.0
                        && tubesCheck(y, x, 0.0, Direction.WEST) % 4 == 0.0
                    )
                        loopMatrix[y][x] = 'V'
                    else
                        loopMatrix[y][x] = 'H'
                } else
                    loopMatrix[y][x] = 'I'

            }
            when (matrix[y][x]) {
                'O' -> print("\u001B[34m${loopMatrix[y][x]}\u001B[0m") // Blue
                'I' -> print("\u001B[31m${loopMatrix[y][x]}\u001B[0m") // Red
                'V' -> print("\u001B[32m${loopMatrix[y][x]}\u001B[0m") // Green
                'H' -> print("\u001B[38;2;255;165;0m${loopMatrix[y][x]}\u001B[0m") // Orange

                else -> {
                    if (valueMatrix[y][x] == 0)
                        print("\u001B[38;2;255;192;203mS\u001B[0m") // Pink
                    else if (valueMatrix[y][x] != -1)
                        print("\u001B[33m${loopMatrix[y][x]}\u001B[0m") // Yellow
                    else
                        print(loopMatrix[y][x]) // Default color
                }
            }
        }
        println()
    }

    //convertI(loopMatrix)

    valueMatrix.forEachIndexed { y, row ->
        row.forEachIndexed { x, _ ->
            when (loopMatrix[y][x]) {
                'O' -> print("\u001B[34m${valueMatrix[y][x]}\u001B[0m") // Blue
                'I' -> print("\u001B[31m${valueMatrix[y][x]}\u001B[0m") // Red
                'V' -> print("\u001B[32m${valueMatrix[y][x]}\u001B[0m") // Green
                'H' -> print("\u001B[38;2;255;165;0m${valueMatrix[y][x]}\u001B[0m") // Orange
                else -> print(" 1")
            }
        }
        println()
    }



    println("Part 2: ${loopMatrix.flatten().count { it == 'I' }}")
}