package com.example.hanadroid.ui.recyclerView

import androidx.recyclerview.widget.RecyclerView

/**
 * Combines RecyclerView scroll listener with Glide's PreLoader.
 * Here we create an instance of [PaginationScrollListener] and pass it along with the
 * preloader to the [CombinedScrollListener].
 * Then, we add the combinedListener to the RecyclerView.
 * Now, whenever the [RecyclerView] is scrolled or its scroll state changes, both the
 * [PaginationScrollListener] and the preloader will be notified.
 * This approach allows you to add multiple OnScrollListener instances to your RecyclerView
 * without having to manage them separately.
 */
class CombinedScrollListener(
    private val listeners: List<RecyclerView.OnScrollListener>
) : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        listeners.forEach { it.onScrolled(recyclerView, dx, dy) }
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        listeners.forEach { it.onScrollStateChanged(recyclerView, newState) }
    }
}