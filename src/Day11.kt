fun main() {


    fun explore(input: List<List<Boolean>>, y: Int, x: Int, steps: Int, destination: Pair<Int, Int>, markTable: Array<Array<Boolean>>, queue: ArrayDeque<Triple<Int, Int, Int>>) : Int {
        if(y == destination.first && x == destination.second) {
            return steps;
        }
        else {
            markTable[y][x] = true
            if(y-1 >= 0 && !markTable[y-1][x] && !queue.any{it.first == y-1 && it.second == x}) queue.add(Triple(y-1, x, steps+1))
            if(x-1 >= 0 && !markTable[y][x-1] && !queue.any{it.first == y && it.second == x-1}) queue.add(Triple(y, x-1, steps+1))
            if(y+1 < input.size && !markTable[y+1][x] && !queue.any{it.first == y+1 && it.second == x}) queue.add(Triple(y+1, x, steps+1))
            if(x+1 < input[0].size && !markTable[y][x+1] && !queue.any{it.first == y && it.second == x+1}) queue.add(Triple(y, x+1, steps+1))
        }

        return 0
    }

    fun part1(input: List<List<Boolean>>): Int {
        val galaxies = input.mapIndexed{y, row -> row.mapIndexed{x, v -> if(v) listOf(y, x) else null}}.flatten().filterNotNull()
        println("Galaxies: $galaxies")

        var count = 0
        var pairs = 0

        galaxies.forEachIndexed { s, coords ->
            galaxies.forEachIndexed dest@ {d, dCoords ->
                if(d <= s) return@dest
               // println("Going from galaxy ${s+1} to ${d+1}")
                pairs++
                val markTable = Array(input.size) { Array(input[it].size) { false } }
                val queue : ArrayDeque<Triple<Int, Int, Int>> = ArrayDeque()
                queue.add(Triple(coords[0],coords[1], 0))
                while(queue.isNotEmpty()) {
                    val el = queue.first()
                    queue.removeFirst()
                    //println("DEBUG: Going to ${el.first},${el.second}, queue: $queue")
                    val ret = explore(input, el.first,el.second,el.third,Pair(dCoords[0], dCoords[1]), markTable, queue)
                    if(ret != 0) {
                        count += ret
                        //println("Found shortest path at $ret steps")
                        return@dest
                    }
                }
            }
            println("Done $s/${galaxies.size}")
        }

        println("Final pairs: $pairs")

        return count
    }


    fun part2(input: List<String>): Int {
        return 0
    }

    val input = readInput("Day11")
    val boolInput = input.map{s -> s.map{ it != '.' }}
    //println(boolInput)

    //expandedGalaxy.forEach { println(it) }
    println(part1(expanded(boolInput, 2)))
    //println(part1(expanded(boolInput, 1000000)))
}

private fun expanded(input: List<List<Boolean>>, times: Int): List<List<Boolean>> {
    // Row
    val rowIndexes: ArrayList<Int> = ArrayList()
    input.forEachIndexed { index, r ->
        if (r.all { !it }) rowIndexes.add(index)
    }
    val expandedRows = input.map { listOf(it) }.flatMapIndexed { index, it -> if (rowIndexes.contains(index)) generateSequence { it }.take(times).toList() else listOf(it) }.flatten()

    println("Expanded rows!")

    // Columns
    val colIndexes: ArrayList<Int> = ArrayList()
    for (col in expandedRows[0].indices) {
        if (expandedRows.all { !it[col] }) colIndexes.add(col)
    }

    val expandedGalaxy = expandedRows.map { it.flatMapIndexed { index, v -> if (colIndexes.contains(index)) generateSequence { v }.take(times).toList() else listOf(v) } }

    println("Expanded complete galaxy!")

    return expandedGalaxy
}