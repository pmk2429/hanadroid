package com.example.hanadroid.util

import com.example.hanadroid.model.FancyModel
import java.util.concurrent.TimeUnit
import kotlin.random.Random

object CreateFancyModel {
    val fancyItems: List<FancyModel> =
        mutableListOf<FancyModel>().apply {
            add(
                FancyModel(
                    1,
                    "Hey",
                    "Lego mini-figures: Have the largest population on Earth",
                    1690852225556
                )
            )
            add(
                FancyModel(
                    3,
                    "Hello",
                    "Lego mini-figures: Have the largest population on Earth",
                    1689944451776
                )
            )
            add(
                FancyModel(
                    6,
                    "Random",
                    "Lego mini-figures: Have the largest population on Earth",
                    1690832478617
                )
            )
            add(
                FancyModel(
                    4,
                    "Best",
                    "Lego mini-figures: Have the largest population on Earth",
                    1689713773401
                )
            )
            add(
                FancyModel(
                    8,
                    "Android",
                    "Lego mini-figures: Have the largest population on Earth",
                    1689283756088
                )
            )
            add(
                FancyModel(
                    9,
                    "Interview",
                    "Lego mini-figures: Have the largest population on Earth",
                    1690938115252
                )
            )
            add(
                FancyModel(
                    21,
                    "Mobile",
                    "Lego mini-figures: Have the largest population on Earth",
                    1691590235522
                )
            )
            add(
                FancyModel(
                    45,
                    "Design",
                    "Lego mini-figures: Have the largest population on Earth",
                    1691265469799
                )
            )
            add(
                FancyModel(
                    67,
                    "Hana",
                    "Lego mini-figures: Have the largest population on Earth",
                    1689909184097
                )
            )
            add(
                FancyModel(
                    83,
                    "Google",
                    "Lego mini-figures: Have the largest population on Earth",
                    1690776020532
                )
            )
        }

    fun generateRandomTimestampForPastTwoMonths(): Long {
        val currentTimeMillis = System.currentTimeMillis()
        val twoMonthsMillis = TimeUnit.DAYS.toMillis(60) // Assuming 30 days per month

        val randomOffset = (Math.random() * twoMonthsMillis).toLong()
        return currentTimeMillis - randomOffset
    }

    fun generateRandomText(length: Int): String {
        val characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        val random = Random(length)
        val text = StringBuilder()
        for (i in 0 until length) {
            text.append(characters[random.nextInt(characters.length)])
        }
        return text.toString()
    }

    fun randomFancyItem(): FancyModel {
        val random = (0..100).random()
        val randomContent = generateRandomText(5)
        return FancyModel(
            random,
            randomContent,
            "GOOG - best company on Earth",
            generateRandomTimestampForPastTwoMonths()
        )
    }
}
