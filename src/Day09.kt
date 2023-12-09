fun main() {
    fun mutableLists(line: String): ArrayList<MutableList<Int>> {
        val arrays = ArrayList<MutableList<Int>>()
        arrays.add("-?\\d+".toRegex().findAll(line).map { it.groupValues[0].toInt() }.toMutableList())

        while (!arrays.last().all { it == 0 } && arrays.last().isNotEmpty()) {
            arrays.add(
                arrays.last()
                    .mapIndexed { index, i -> if (index != arrays.last().size - 1) arrays.last()[index + 1] - i else -1 }
                    .toMutableList()
            )
            arrays.last().removeLast()
        }
        return arrays
    }

    fun part1(input: List<String>): Int {
        var sum = 0
        input.forEach { line ->
            val arrays = mutableLists(line)
            arrays.forEach { sum += it.last() }
        }
        return sum
    }

    fun part2(input: List<String>): Int {
        var sum = 0
        input.forEach { line ->
            val arrays = mutableLists(line)
            arrays.forEachIndexed { index, it -> if (index % 2 == 0) sum += it.first() else sum -= it.first() }
        }
        return sum
    }

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}