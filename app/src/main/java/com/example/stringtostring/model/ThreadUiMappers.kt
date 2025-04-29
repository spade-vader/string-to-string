package com.example.stringtostring.model

fun ThreadEntity.toUiModel(manufacturerName: String): ThreadUiModel {
    return ThreadUiModel(
        colorCode = this.colorCode,
        rgbCode = this.rgbCode,
        manufacturer = manufacturerName
    )
}

fun ThreadMatch.toUiModel(manufacturerName: String): ThreadUiModel {
    return this.thread.toUiModel(manufacturerName)
}

fun ShelfThreadEntity.toUiModel(): ThreadUiModel {
    return ThreadUiModel(
        colorCode = this.colorCode,
        rgbCode = this.rgbCode,
        manufacturer = this.manufacturerName
    )
}