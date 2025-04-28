package com.example.stringtostring.domain

import com.example.stringtostring.model.Manufacturer
import com.example.stringtostring.model.ThreadEntity
import com.example.stringtostring.model.ThreadMatch
import com.example.stringtostring.model.ThreadPerfectMatch
import kotlin.math.pow
import kotlin.math.sqrt

object ColorsMaster {
    fun findClosestThreads(
        targetThread: ThreadEntity,
        allThreads: List<ThreadEntity>,
        matchesAmount: Int = 100
    ): List<ThreadMatch> {
        val matches = mutableListOf<ThreadMatch>()
        val targetRGB = targetThread.rgbCode

        for (candidate in allThreads) {
            if (candidate.id == targetThread.id) continue

            val candidateRGB = candidate.rgbCode
            val percentMatch = convertDistanceToPercents(
                calculateDistance(targetRGB, candidateRGB)
            )

            matches.add(ThreadMatch(candidate, percentMatch))
        }

        return matches.sortedByDescending { it.percent }.take(matchesAmount)
    }

    fun manufacturersPerfectMatches(
        targetManufacturer: Manufacturer,
        secondaryManufacturers: List<Manufacturer>,
        allThreads: List<ThreadEntity>
    ): List<ThreadPerfectMatch> {
        if (secondaryManufacturers.isEmpty()) return emptyList()

        val targetThreads = allThreads.filter { it.manufacturerId == targetManufacturer.id }

        val secondaryThreads = secondaryManufacturers.associateWith { manufacturer ->
            allThreads.filter { it.manufacturerId == manufacturer.id }
        }

        return targetThreads.map { targetThread ->
            val matches = secondaryThreads.mapValues { (_, threads) ->
                threads.find { it.rgbCode == targetThread.rgbCode }
            }
            ThreadPerfectMatch(
                thread = targetThread,
                perfectMatches = matches
            )
        }
    }

    fun calculateDistance(firstRGB: String, secondRGB: String): Double {
        val (r1, g1, b1) = parseRGB(firstRGB)
        val (r2, g2, b2) = parseRGB(secondRGB)

        val distance = sqrt(
            ((r1 - r2).toDouble().pow(2)) +
                    ((g1 - g2).toDouble().pow(2)) +
                    ((b1 - b2).toDouble().pow(2))
        )
        return distance
    }

    fun convertDistanceToPercents(distance: Double): Double {
        val similarity = 100 - (distance / 441.67 * 100)
        return similarity
    }

    fun parseRGB(hex: String): List<Int> {
        return hex.removePrefix("#")
            .chunked(2)
            .map { it.toInt(16) }
    }
}