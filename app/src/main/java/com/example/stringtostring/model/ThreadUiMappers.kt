package com.example.stringtostring.model

/**
 * Расширение (extension-функция) для преобразования объекта ThreadEntity в модель представления (UI-модель).
 *
 * Использование в слоях отображения (UI) для изоляции структуры данных, полученных из базы,
 * от интерфейсных компонентов.
 *
 * @receiver ThreadEntity               Объект, представляющий сущность нити из базы данных.
 * @param manufacturerName             Название производителя, передаваемое отдельно (не хранится в ThreadEntity).
 * @return ThreadUiModel               Объект, пригодный для отображения в UI.
 */
fun ThreadEntity.toUiModel(manufacturerName: String): ThreadUiModel {
    return ThreadUiModel(
        colorCode = this.colorCode,
        rgbCode = this.rgbCode,
        manufacturer = manufacturerName
    )
}

/**
 * Расширение для преобразования объекта ThreadMatch в UI-модель.
 *
 * Представление нити с процентом совпадения, где в UI используется только базовая информация о нити.
 *
 * @receiver ThreadMatch               Объект сопоставления нити с процентом.
 * @param manufacturerName             Название производителя, связанного с нитью.
 * @return ThreadUiModel               Модель для отображения в интерфейсе.
 */
fun ThreadMatch.toUiModel(manufacturerName: String): ThreadUiModel {
    return this.thread.toUiModel(manufacturerName)
}

/**
 * Расширение для преобразования объекта ShelfThreadEntity в модель для отображения.
 *
 * Использование для визуализации нитей, уже добавленных на "полку" пользователя.
 *
 * @receiver ShelfThreadEntity         Сущность нити, добавленной пользователем в коллекцию.
 * @return ThreadUiModel               Преобразованная модель для отображения в UI.
 */
fun ShelfThreadEntity.toUiModel(): ThreadUiModel {
    return ThreadUiModel(
        colorCode = this.colorCode,
        rgbCode = this.rgbCode,
        manufacturer = this.manufacturerName
    )
}
