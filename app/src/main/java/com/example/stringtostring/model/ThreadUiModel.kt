package com.example.stringtostring.model

/**
 * Модель представления нити для отображения в пользовательском интерфейсе (UI).
 *
 * Используется для изоляции бизнес-логики и данных, хранящихся в базе, от компонентов, работающих с интерфейсом.
 *
 * Параметры конструктора:
 * @property colorCode      Строковый код цвета нити, заданный производителем.
 * @property rgbCode        Строковое представление цвета в формате RGB (например, "#FF5733").
 * @property manufacturer   Название производителя нити, для отображения в UI.
 */
data class ThreadUiModel(
    val colorCode: String,
    val rgbCode: String,
    val manufacturer: String
)
