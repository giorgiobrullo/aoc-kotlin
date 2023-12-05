data class Conversion(val sourceRange: LongRange, val destinationStart: Long)

fun main() {
    fun part1(seeds: List<String>, maps: ArrayList<ArrayList<Conversion>>): Long {
        var lowestReturn: Long = -1

        seeds.forEach{seed ->
            var currentSeed = seed.toLong()
            maps.forEach{map ->
                run loop@ {
                    map.forEach {conversion ->
                        if(conversion.sourceRange.contains(currentSeed)) {
                            //println("Seed $currentSeed is in range $conversion")
                            currentSeed = currentSeed - conversion.sourceRange.first + conversion.destinationStart
                            return@loop
                        }
                    }
                }
            }
            if(lowestReturn.compareTo(-1) == 0 || currentSeed < lowestReturn) lowestReturn = currentSeed
        }
        return lowestReturn
    }

    // TODO: Optimize using ranges instead of checking every seed in every range
    fun part2(seeds: List<String>, maps: ArrayList<ArrayList<Conversion>>): Long {
        var lowestReturn: Long = -1
        val seedsList = ArrayList<LongRange>()
        for(seed in seeds.indices step 2)
            seedsList.add(LongRange(seeds[seed].toLong(), seeds[seed].toLong() + seeds[seed+1].toLong()-1))
        println(seedsList)

        seedsList.forEach{seedRange ->
            seedRange.forEach {seed ->
                var currentSeed = seed
                maps.forEach{map ->
                    run loop@ {
                        map.forEach {conversion ->
                            if(conversion.sourceRange.contains(currentSeed)) {
                                //println("Seed $currentSeed is in range $conversion")
                                currentSeed = currentSeed - conversion.sourceRange.first + conversion.destinationStart
                                return@loop
                            }
                        }
                    }
                }
                if(lowestReturn.compareTo(-1) == 0 || currentSeed < lowestReturn) lowestReturn = currentSeed
            }
        }
        return lowestReturn
    }

    val input = readInput("Day05")
    val seeds = "\\d+".toRegex().findAll(input[0]).map {it.groupValues[0]}.toList()

    val maps = ArrayList<ArrayList<Conversion>>()

    ".+:\\n(?:\\d+ \\d+ \\d+\\n?)*".toRegex().findAll(input.joinToString(separator="\n")).map{it.groupValues[0].split("\n")}.forEachIndexed{index, map ->
        maps.add(ArrayList())
        map.forEach{
            val values = "(?<destinationRange>^\\d+) (?<sourceRangeStart>\\d+) (?<rangeLength>\\d+)\\n?".toRegex().find(it)
            if(values != null) maps[index].add(
                Conversion(
                    LongRange(values.groups["sourceRangeStart"]!!.value.toLong(), values.groups["sourceRangeStart"]!!.value.toLong() + values.groups["rangeLength"]!!.value.toLong() -1),
                    values.groups["destinationRange"]!!.value.toLong() ))
        }
    }

    println(part1(seeds, maps))
    println(part2(seeds, maps))
}