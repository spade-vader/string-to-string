package com.example.stringtostring.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Представление сущности нитей (ThreadEntity) для хранения информации о доступных нитях в базе данных Room.
 *
 * Название таблицы в базе данных: "threads"
 *
 * Связь:
 * - Внешний ключ: manufacturer_id ссылается на id сущности Manufacturer.
 * - Поведение при обновлении и удалении: NO_ACTION (связанные записи остаются без изменений).
 *
 * Индексация:
 * - Создание индекса по колонке manufacturer_id для ускорения операций выборки по производителю.
 *
 * Параметры конструктора:
 * @property id               Уникальный идентификатор записи в таблице (автоматическая генерация).
 * @property colorCode        Строковый код цвета, определяемый производителем (например, "310").
 * @property rgbCode          Строковое представление цвета в формате RGB (например, "#000000").
 * @property manufacturerId   Внешний ключ, указывающий на производителя (id из таблицы Manufacturer).
 */
@Entity(
    tableName = "threads",
    foreignKeys = [
        ForeignKey(
            entity = Manufacturer::class,
            parentColumns = ["id"],
            childColumns = ["manufacturer_id"],
            onDelete = ForeignKey.NO_ACTION,
            onUpdate = ForeignKey.NO_ACTION
        )
    ],
    indices = [Index(value = ["manufacturer_id"])]
)
data class ThreadEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "color_code") val colorCode: String,
    @ColumnInfo(name = "rgb_code") val rgbCode: String,
    @ColumnInfo(name = "manufacturer_id") val manufacturerId: Int
)
