
data class PuzzleNumber(val value: Int, val range: IntRange, var nearSymbol: Boolean = false)

fun main() {
    fun part1(input: List<String>, formattedInput: ArrayList<ArrayList<PuzzleNumber>>): Int {
        input.forEachIndexed { lineIndex, line -> line.forEachIndexed{charIndex, char ->
            if("[^A-Za-z0-9.\\n]".toRegex().matches(char.toString())) {
                for(i in lineIndex+1 downTo lineIndex-1){
                    formattedInput[i].forEach{
                        val expandedRange : IntRange = it.range.first-1..it.range.last+1
                        if(expandedRange.contains(charIndex)) it.nearSymbol = true
                    }
                }
            }
        } }
        var sum = 0

        formattedInput.forEach{line -> line.forEach{puzzleNumber -> if(puzzleNumber.nearSymbol) sum += puzzleNumber.value }}

        return sum
    }

    fun part2(input: List<String>, formattedInput: ArrayList<ArrayList<PuzzleNumber>>): Int {
        var sum = 0

        input.forEachIndexed { lineIndex, line -> line.forEachIndexed{charIndex, char ->
            if("[^A-Za-z0-9.\\n]".toRegex().matches(char.toString())) {
                val adiacentNumbers = ArrayList<PuzzleNumber>()
                for(i in lineIndex+1 downTo lineIndex-1){
                    formattedInput[i].forEach{
                        val expandedRange : IntRange = it.range.first-1..it.range.last+1
                        if(expandedRange.contains(charIndex)) adiacentNumbers.add(it)
                    }
                }
                if(adiacentNumbers.size == 2) sum += (adiacentNumbers[0].value * adiacentNumbers[1].value)
            }
        } }

        return sum
    }

    val input = readInput("Day03")
    val formattedInput = ArrayList<ArrayList<PuzzleNumber>>()
    for((index, line) in input.withIndex()){
        formattedInput.add(ArrayList())
        "\\d+".toRegex().findAll(line).toList().forEach { formattedInput[index].add(PuzzleNumber(it.value.toInt(), it.range)) }
    }
    println(part1(input, formattedInput))
    println(part2(input, formattedInput))
}