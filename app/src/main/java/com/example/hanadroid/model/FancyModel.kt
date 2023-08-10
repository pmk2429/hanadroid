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

fun getList(): List<FancyModel> {
    return mutableListOf<FancyModel>().apply {
        add(FancyModel(1, "Hey", "Lego mini-figures: Have the largest population on Earth", 1690852225556))
        add(FancyModel(3, "Hello", "Lego mini-figures: Have the largest population on Earth", 1689944451776))
        add(FancyModel(6, "Random", "Lego mini-figures: Have the largest population on Earth", 1690832478617))
        add(FancyModel(4, "Best", "Lego mini-figures: Have the largest population on Earth", 1689713773401))
        add(FancyModel(8, "Android", "Lego mini-figures: Have the largest population on Earth", 1689283756088))
        add(FancyModel(9, "Interview", "Lego mini-figures: Have the largest population on Earth", 1690938115252))
        add(FancyModel(21, "Mobile", "Lego mini-figures: Have the largest population on Earth", 1691590235522))
        add(FancyModel(45, "Design", "Lego mini-figures: Have the largest population on Earth", 1691265469799))
        add(FancyModel(67, "Hana", "Lego mini-figures: Have the largest population on Earth", 1689909184097))
        add(FancyModel(83, "Google", "Lego mini-figures: Have the largest population on Earth", 1690776020532))
    }
}
