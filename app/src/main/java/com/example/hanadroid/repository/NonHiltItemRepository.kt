package com.example.hanadroid.repository

import com.example.hanadroid.model.SampleItem

class NonHiltItemRepository {

    fun getItems(): List<SampleItem> {
        // Fetch items from a data source (e.g., API, database)
        return listOf(
            SampleItem(1, "Batman", 10),
            SampleItem(2, "Superman", 20),
            SampleItem(3, "Iron Man", 30),
            SampleItem(4, "Aqua Man", 40),
            SampleItem(5, "Spider Man", 50),
        )
    }
}
