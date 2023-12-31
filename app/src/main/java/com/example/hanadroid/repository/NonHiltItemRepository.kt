package com.example.hanadroid.repository

import com.example.hanadroid.model.SampleItem

class NonHiltItemRepository {

    fun getItems(): List<SampleItem> {
        // Fetch items from a data source (e.g., API, database)
        return listOf(
            SampleItem(1, "Batman"),
            SampleItem(2, "Superman"),
            SampleItem(3, "Iron Man"),
            SampleItem(4, "Aqua Man"),
            SampleItem(5, "Spider Man"),
        )
    }
}
