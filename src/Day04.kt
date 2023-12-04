import kotlin.math.pow

data class ScratchTicket(val numbers: ArrayList<Int>, val winningNumbers: ArrayList<Int>, var timesToPlay: Int = 1)
fun main() {

    fun part1(input: ArrayList<ScratchTicket>): Int {
        var points = 0
        input.forEach { ticket ->
            var found = 0
            ticket.numbers.forEach { if (ticket.winningNumbers.contains(it)) found++ }
            if(found != 0) points += (2.0.pow(found-1).toInt())
        }
        return points
    }

    fun part2(input: ArrayList<ScratchTicket>): Int {
        var cardsPlayed = 0
        input.forEachIndexed { index, ticket ->
            var found = 0
            ticket.numbers.forEach { if (ticket.winningNumbers.contains(it)) found++}
            cardsPlayed+=ticket.timesToPlay
             //println("Playing card $index+1, won $found numbers")
            for(i in index+1..index+found){
                    if(i >= input.size) break
                    input[i].timesToPlay += ticket.timesToPlay
            }

        }
        return cardsPlayed
    }

    val input = readInput("Day04")
    val formattedInput = ArrayList<ScratchTicket>()

    input.forEachIndexed{index, line ->
        val ticket = line.substring(9).split(" | ")
        formattedInput.add(ScratchTicket(ArrayList(), ArrayList()))
        "\\d+".toRegex().findAll(ticket[0]).toList().forEach{formattedInput[index].numbers.add(it.value.toInt())}
        "\\d+".toRegex().findAll(ticket[1]).toList().forEach{formattedInput[index].winningNumbers.add(it.value.toInt())}
         //println(formattedInput[index])
    }
    println(part1(formattedInput))
    println(part2(formattedInput))
}