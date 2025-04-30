package com.example.stringtostring.model

/**
 * Модель представления соответствия нити с определённым процентом совпадения.
 *
 * Использование в логике сопоставления нитей между собой по цвету, производителю и другим признакам.
 *
 * Параметры конструктора:
 * @property thread   Экземпляр сущности ThreadEntity, представляющий основную нить.
 * @property percent  Значение степени совпадения с другой нитью (в процентах, от 0.0 до 100.0).
 */
data class ThreadMatch(
    val thread: ThreadEntity,
    val percent: Double
)

/**
 * Модель представления идеального соответствия между нитью и нитями других производителей.
 *
 * Использование для отображения или хранения точных аналогов данной нити у других брендов.
 *
 * Параметры конструктора:
 * @property thread          Исходная нить, для которой выполняется поиск точных соответствий.
 * @property perfectMatches  Карта соответствий: ключ — объект Manufacturer,
 *                           значение — экземпляр ThreadEntity или null, если соответствие не найдено.
 */
data class ThreadPerfectMatch(
    val thread: ThreadEntity,
    val perfectMatches: Map<Manufacturer, ThreadEntity?>
)
