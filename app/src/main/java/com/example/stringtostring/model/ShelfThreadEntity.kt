package com.example.stringtostring.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Представление сущности нитей (ShelfThreadEntity) для хранения в локальной базе данных Room.
 *
 * Название таблицы в базе данных: "shelf_threads"
 *
 * Использование аннотации @Entity позволяет Room сгенерировать необходимые SQL-операции для работы
 * с этой сущностью.
 *
 * Структура данных предназначена для хранения информации о нитях, размещённых на "полке",
 * включая их уникальные идентификаторы, цветовые коды и производителя.
 *
 * Параметры конструктора:
 * @property id                 Уникальный идентификатор записи в таблице (автоматическая генерация).
 * @property threadId           Идентификатор нити, предоставляемый внешней системой или приложением.
 * @property colorCode          Строковый код цвета, заданный производителем (например, "DMC 321").
 * @property rgbCode            Строковое представление цвета в формате RGB (например, "#FF0000").
 * @property manufacturerName   Название производителя, к которому относится данная нить.
 */
@Entity(tableName = "shelf_threads")
data class ShelfThreadEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val threadId: Int,
    val colorCode: String,
    val rgbCode: String,
    val manufacturerName: String
)
