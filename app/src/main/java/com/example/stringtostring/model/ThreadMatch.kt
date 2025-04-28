package com.example.stringtostring.model

data class ThreadMatch(
    val thread: ThreadEntity,
    val percent: Double
)

data class ThreadPerfectMatch(
    val thread: ThreadEntity,
    val perfectMatches: Map<Manufacturer, ThreadEntity?>
)