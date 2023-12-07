import java.math.BigInteger

data class Hand(val hand: String, val bid: Int, var type: Int, var score: BigInteger, var rank: Int)

// Rough day TODO: Find a better solution than the BigInteger score system
fun main() {
    fun calculateScore(hand: Hand, cardMap: Map<Char, Int>) {
        hand.score = (hand.type.toBigInteger() * (100.0.toBigDecimal().pow(hand.hand.length + 1)).toBigInteger())
        //println("Score: ${hand.score}, ${hand.type.toBigInteger()} * ${(1000.0.toBigDecimal().pow(hand.hand.length+1)).toBigInteger()}")

        hand.hand.forEachIndexed { index, value ->
            if (value.isDigit()) {
                hand.score = hand.score.add(
                    (100.0.toBigDecimal().pow(hand.hand.length - index).toBigInteger()) * value.digitToInt()
                        .toBigInteger()
                )
                //println("${hand.hand}: (${value}) Added ${1000.0.toBigDecimal().pow(hand.hand.length-index).toBigInteger()} * ${value.digitToInt().toBigInteger()} = ${hand.score}")
            } else {
                hand.score = hand.score.add(
                    (100.0.toBigDecimal().pow(hand.hand.length - index)
                        .toBigInteger()) * cardMap[value]!!.toBigInteger()
                )
                //println("${hand.hand}: (${value}) Added ${1000.0.toBigDecimal().pow(hand.hand.length-index).toBigInteger()} * ${cardMap[value]!!.toBigInteger()} = ${hand.score}")
            }
        }
    }



    fun part1(input: ArrayList<Hand>): Long {
        val cardMap = mapOf('A' to 50, 'K' to 40, 'Q' to 30, 'J' to 20, 'T' to 10)

        input.forEach{hand ->
            val map = hand.hand.groupingBy{it}.eachCount().values.sortedBy { it }
            //println(map)
            if(map.size == 5) hand.type = 1
            else if(map.size == 4) hand.type = 2 // One pair
            else if(map == listOf(1, 2, 2)) hand.type = 3 // Two pair
            else if(map == listOf(1, 1, 3)) hand.type = 4 // Three of a kind
            else if(map == listOf(2, 3)) hand.type = 5 // Full house
            else if(map == listOf(1, 4)) hand.type = 6 // Four of a kind
            else if(map.size == 1) hand.type = 7 // Five of a kind
            else println("Unknown card found: $map")

            calculateScore(hand, cardMap)
            //rintln("Card $map, type ${hand.type}")

        }

        var sum : Long = 0
        input.sortBy{it.score}
        input.forEachIndexed{index, hand ->
            sum += ((index + 1) * hand.bid)
            //println("Hand ${hand.hand} (score: ${hand.score}) got rank ${index+1}, added ${hand.bid} * ${index+1}")
        }

        return sum
    }


    fun part2(input: ArrayList<Hand>): Long {
        val cardMap = mapOf('A' to 50, 'K' to 40, 'Q' to 30, 'J' to 1, 'T' to 10)

        input.forEach{hand ->
            val map : MutableMap<Char, Int> = hand.hand.groupingBy{it}.eachCount().toList().sortedByDescending { it.second }.toMap().toMutableMap()

            // Handle joker
            if(map.any{it.key == 'J'} && map.size > 1){
                map[map.entries.find { it.key != 'J' }!!.key] = map.getValue('J') + map[map.entries.find { it.key != 'J' }!!.key]!!
                map.remove('J')
            }
            //println("${hand.hand}: $map")

            val mapCheck = map.values.toList().sortedBy { it }

            if(mapCheck.size == 5) hand.type = 1
            else if(mapCheck.size == 4) hand.type = 2 // One pair
            else if(mapCheck == listOf(1, 2, 2)) hand.type = 3 // Two pair
            else if(mapCheck == listOf(1, 1, 3)) hand.type = 4 // Three of a kind
            else if(mapCheck == listOf(2, 3)) hand.type = 5 // Full house
            else if(mapCheck == listOf(1, 4)) hand.type = 6 // Four of a kind
            else if(mapCheck.size == 1) hand.type = 7 // Five of a kind
            else println("Unknown card found: ${hand.hand}: $map")

            calculateScore(hand, cardMap)

            //println("Card $map, type ${hand.type}")

        }

        var sum : Long = 0
        input.sortBy{it.score}
        input.forEachIndexed{index, hand ->
            sum += ((index + 1) * hand.bid)
            //println("Hand ${hand.hand} (score: ${hand.score}, type:${hand.type}) got rank ${index+1}, added ${hand.bid} * ${index+1}")
        }

        return sum
    }

    val input = readInput("Day07")
    val formattedInput = ArrayList<Hand>()

    input.forEach{formattedInput.add(Hand(it.split(" ")[0], it.split(" ")[1].toInt(), 0, BigInteger("0"), -1)) }

    println(formattedInput)

    println(part1(formattedInput))
    println(part2(formattedInput))
}