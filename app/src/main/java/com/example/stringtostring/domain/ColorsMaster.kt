package com.example.stringtostring.domain

import com.example.stringtostring.model.Manufacturer
import com.example.stringtostring.model.ThreadEntity
import com.example.stringtostring.model.ThreadMatch
import com.example.stringtostring.model.ThreadPerfectMatch
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Объект ColorsMaster.
 *
 * Предоставление утилитарной логики для сравнения цветовых значений нитей.
 * Использование расстояний RGB и преобразование их в процентное выражение
 * сходства. Поддержка поиска наиболее похожих и идеально совпадающих нитей.
 */
object ColorsMaster {

    /**
     * Поиск наиболее близких по цвету нитей к целевой.
     *
     * @param targetThread — целевая нить для сравнения.
     * @param allThreads — полный список нитей для анализа.
     * @param matchesAmount — количество совпадений, которые требуется вернуть (по умолчанию: 100).
     * @return Список объектов [ThreadMatch], отсортированных по убыванию процентного совпадения.
     */
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

    /**
     * Сопоставление нитей одного производителя с идеально совпадающими нитями других производителей.
     *
     * @param targetManufacturer — производитель, чьи нити служат целевыми.
     * @param secondaryManufacturers — список производителей, среди которых производится поиск совпадений.
     * @param allThreads — общий список всех нитей.
     * @return Список объектов [ThreadPerfectMatch] с идеально совпадающими RGB-кодами нитей.
     */
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

    /**
     * Расчёт евклидова расстояния между двумя RGB-значениями.
     *
     * @param firstRGB — строка с шестнадцатеричным RGB-значением (например, "#FFAA33").
     * @param secondRGB — строка с другим RGB-значением.
     * @return Число, представляющее евклидово расстояние между двумя цветами.
     */
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

    /**
     * Преобразование расстояния между цветами в процентное совпадение.
     *
     * Основано на максимальном евклидовом расстоянии в RGB-пространстве (~441.67).
     *
     * @param distance — расстояние между цветами.
     * @return Значение от 0 до 100, где 100 — полное совпадение.
     */
    fun convertDistanceToPercents(distance: Double): Double {
        val similarity = 100 - (distance / 441.67 * 100)
        return similarity
    }

    /**
     * Преобразование шестнадцатеричного RGB-кода в список чисел (R, G, B).
     *
     * @param hex — строка в формате "#RRGGBB".
     * @return Список из трёх целых чисел: красного(R), зелёного(G) и синего(B) компонентов.
     */
    fun parseRGB(hex: String): List<Int> {
        return hex.removePrefix("#")
            .chunked(2)
            .map { it.toInt(16) }
    }
}
