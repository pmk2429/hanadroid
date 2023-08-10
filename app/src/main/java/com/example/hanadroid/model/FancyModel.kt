package com.example.hanadroid.model

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

data class FancyModel(
    val randomNum: Int = 0,
    val content: String = "",
    val description: String = "",
    val createdAt: Long = 0L
)

val FancyModel.createdText: String get() = dateFormatter.format(LocalDateTime.now())
private val dateFormatter = DateTimeFormatter.ofPattern("DD MM yyyy")

fun generateRandomTimestampsForPastMonth(): List<Long> {
    val currentTimeMillis = System.currentTimeMillis()
    val thirtyDaysMillis = TimeUnit.DAYS.toMillis(30)
    val timestamps = mutableListOf<Long>()

    for (i in 1..10) {
        val randomOffset = (Math.random() * thirtyDaysMillis).toLong()
        val randomTimestamp = currentTimeMillis - randomOffset
        timestamps.add(randomTimestamp)
    }

    return timestamps
}

fun Long.toFormattedDateString(): String {
    val instant = Instant.ofEpochMilli(this)
    val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
    val zonedDateTime = instant.atZone(ZoneId.systemDefault())
    return formatter.format(zonedDateTime)
}

