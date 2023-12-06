data class Race(val time: Long, val distance: Long)

// TODO: Solution with quadratic formula instead of bruteforce https://www.reddit.com/r/adventofcode/comments/18bwuzt/comment/kc7blso/
fun main() {
    fun part1(input: ArrayList<Race>): Int {
        var sum = 1
        input.forEach{ race ->
            var tmp = 0
            for(i in race.time downTo 0) {
                if(i*(race.time-i) > race.distance) tmp++
            }
            if(tmp != 0) sum *= tmp
        }
        return if(sum != 1) sum
        else 0
    }

    fun part2(input: Race): Int {
        var sum = 0
        for(i in input.time downTo 0) {
            if(i*(input.time-i) > input.distance) sum++
        }

        return sum
    }

    val input = readInput("Day06")
    val formattedInput1 = ArrayList<Race>()

    // Part 1
    val time = "\\d+".toRegex().findAll(input[0]).map{it.groupValues[0]}.toList()
    val distance = "\\d+".toRegex().findAll(input[1]).map{it.groupValues[0]}.toList()
    time.forEachIndexed { index, t -> formattedInput1.add(Race(t.toLong(), distance[index].toLong())) }


    // Part 2
    val time1 = time.joinToString(separator="")
    val distance1 = distance.joinToString(separator="")
    val formattedInput2 = Race(time1.toLong(), distance1.toLong())
    //println(formattedInput2)

    println(part1(formattedInput1))
    println(part2(formattedInput2))
}