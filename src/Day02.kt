import java.lang.IllegalArgumentException
data class Turn(var red: Int = 0, var green: Int = 0, var blue: Int = 0){
    fun setColor(colorName: String?, value: Int?){
        if (value != null && colorName != null) {
            when (colorName.uppercase()){
                "RED" -> red = value
                "GREEN" -> green = value
                "BLUE" -> blue = value
                else -> throw IllegalArgumentException("Unknown color")
            }
        }
    }


}

fun main() {
    val checkBag = Turn(red = 12, green = 13, blue = 14)

    fun turnPossible(turn: Turn): Boolean {
        return turn.red <= checkBag.red && turn.green <= checkBag.green && turn.blue <= checkBag.blue
    }

    fun part1(input: ArrayList<ArrayList<Turn>>): Int {
        var sum = 0;

        input.forEachIndexed{index, game ->
            var possible = true
            game.forEach{turn -> if (!turnPossible(turn)) possible = false}
            if(possible) sum += index + 1
        }

        return sum
    }
    
    fun part2(input: ArrayList<ArrayList<Turn>>): Int {
        var sum = 0
        input.forEach{game ->
            val max = Turn()
            game.forEach{turn ->
                if (max.red < turn.red) max.red = turn.red
                if (max.blue < turn.blue) max.blue = turn.blue
                if (max.green < turn.green) max.green = turn.green
            }
            sum += (max.red * max.green * max.blue)
        }


        return sum
    }

    val input = readInput("Day02")

    val formattedInput = ArrayList<ArrayList<Turn>>()
    val colorRegex = "\\s(?<number>\\d{1,3})\\s(?<color>[^,;]+),?".toRegex()

    input.forEachIndexed { gameIndex, game ->
        formattedInput.add(gameIndex, ArrayList())
        game.split(";").forEachIndexed{index, turn ->
            formattedInput[gameIndex].add(index, Turn())
            val turnRegex = colorRegex.findAll(turn)
            turnRegex.forEach{
                formattedInput[gameIndex][index].setColor(it.groups["color"]?.value, it.groups["number"]?.value?.toInt())
            }
        }
    }
    println("We got ${formattedInput.size} giochi")
    //input.forEachIndexed { gameIndex, game -> game.split(";").forEachIndexed { index, turn -> turn.forEach{colorRegex.findAll(it).forEach{group -> println(group.groups["number"])}}} }
    println(part1(formattedInput))
    println(part2(formattedInput))
}