fun main() {
    fun part1(input: List<String>): Int {
        var sum = 0
        for (line in input) {
            val digits = ArrayList<String>()
            line.forEach { if (it.isDigit()) digits.add(it.toString()) }
            val number = digits[0] + digits[digits.size - 1]
            sum += number.toInt()
        }

        return sum
    }

    fun part2(input: ArrayList<String>): Int {
        //val oldInput = input.toArray()
        var sum = 0

        for ((index, line) in input.withIndex()) {
            val replacements = mapOf(
                "one" to "o1e",
                "two" to "t2o",
                "three" to "t3e",
                "four" to "f4r",
                "five" to "f5e",
                "six" to "s6x",
                "seven" to "s7n",
                "eight" to "e8t",
                "nine" to "n9e"
            )
            var newLine = line
            for((key, value) in replacements){
                newLine = newLine.replace(key, value)
            }
            input[index] = newLine
        }

        for ((index, line) in input.withIndex()) {
            val digits = ArrayList<String>()
            line.forEach { if (it.isDigit()) digits.add(it.toString()) }
            val number = digits[0] + digits[digits.size - 1]
            //println("${oldInput[index]} -> $line | $number")
            sum += number.toInt()
        }

        return sum
    }

    // test if implementation meets criteria from the description, like:
    //val testInput = readInput("Day01_test")
    //check(part1(testInput) == 1)

    val input = readInput("Day01")
    part1(input).println()
    part2(input as ArrayList<String>).println()
}
