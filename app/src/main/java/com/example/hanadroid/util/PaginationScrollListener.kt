package com.example.hanadroid.util

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * When you start loading the list, set isLoadingEmails value to true. Once loaded, set it to false.
 * This can be managed by view state triggered through **StateFlow**.
 * ```
 * viewModel.isAllEmailLoaded
 * ```
 * value depends on API call implementation.
 * If your API provides the total count of list items, you can simply implement it as
 * ```
 * isAllEmailLoaded = currentList.size == totalListItems
 * ```.
 */
abstract class PaginationScrollListener(
    private val layoutManager: LinearLayoutManager
) : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val visibleItemCount: Int = layoutManager.childCount
        val totalItemCount: Int = layoutManager.itemCount
        val firstVisibleItemPosition: Int = layoutManager.findFirstVisibleItemPosition()
        if (!isLoading() && !isLastPage()) {
            if (visibleItemCount + firstVisibleItemPosition >= totalItemCount &&
                firstVisibleItemPosition >= 0
            ) {
                loadMoreItems()
            }
        }
    }

    protected abstract fun loadMoreItems()
    abstract fun isLastPage(): Boolean
    abstract fun isLoading(): Boolean
}
