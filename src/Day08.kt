fun main() {
    fun gcd(a: Long, b: Long): Long {
        if (b == 0.toLong()) return a
        return gcd(b, a % b)
    }

    fun lcm(a: Long, b: Long): Long {
        return a / gcd(a, b) * b
    }

    fun findLCMOfListOfNumbers(numbers: List<Long>): Long {
        var result = numbers[0]
        for (i in 1 until numbers.size) {
            result = lcm(result, numbers[i])
        }
        return result
    }

    fun part1(moves: String, input: MutableMap<String, List<String>>): Int {
        var current = "AAA"
        var count = 0
        while(true){
            moves.forEach{
                //println("Current: $current, moving $it")
                current = if(it == 'R') input[current]!![1]
                else input[current]!![0]

                count++

                if(current == "ZZZ") return count
            }
        }
    }

    // This time around bruteforcing takes forever, meaning more than a few hours because I didn't let it finish
    fun part2Bruteforce(moves: String, input: MutableMap<String, List<String>>): Long {
        var current = input.keys.filter{it.endsWith("A")}.toMutableList()
        println("Current: $current")
        var count : Long = 0
        while(true){
            moves.forEach{move ->
                //println("Current: $current, moving $move")
                current = current.map {
                    if(move == 'R') input[it]!![1]
                    else input[it]!![0]
                }.toMutableList()
                count++
                if(current.all{it.endsWith("Z")}) return count
            }
        }
    }

    fun part2(moves: String, input: MutableMap<String, List<String>>): Long {
        var current = input.keys.filter{it.endsWith("A")}.toMutableList()
        var count : Long = 0
        while(current.any { item -> item.any{!it.isDigit()}  }){
                moves.forEach{move ->
                    current = current.map { place ->
                        if(place.endsWith("Z")) count.toString()
                        else if(place.all{it.isDigit()}) place
                        else if(move == 'R') input[place]!![1]
                        else input[place]!![0]
                    }.toMutableList()
                    count++
                }
        }

        return findLCMOfListOfNumbers(current.map { it.toLong() })
    }

    val input = readInput("Day08")

    val moves = input[0]
    val formattedInput = mutableMapOf<String, List<String>>()

    input.forEach {
        if(it.length != 16) return@forEach
        formattedInput[it.substringBefore(" ")] = it.substringAfter("(").replaceFirst(")", "").replace(" ", "").split(',')
    }
    //println(formattedInput)

    println(part1(moves,formattedInput))
    println(part2(moves,formattedInput))
}