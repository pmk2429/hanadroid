package com.example.hanadroid.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hanadroid.R

class InfiniteScrollingAdapter(
    private val items: MutableList<String>,
    private val onLoadMore: () -> Unit
) : RecyclerView.Adapter<InfiniteScrollingAdapter.MyViewHolder>() {

    private var isLoading = false
    private var isLastPage = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.fancy_item_layout, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(items[position])

        if (position == itemCount - 1 && !isLoading && !isLastPage) {
            // Load more data when near the end of the list
            isLoading = true
            onLoadMore()
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<String>) {
        isLoading = false
        isLastPage = newItems.isEmpty()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    inner class MyViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: String) {
            // Set item data to the view here
            (itemView as TextView).text = item
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        val layoutManager = recyclerView.layoutManager
        if (layoutManager is LinearLayoutManager) {
            val scrollListener = object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                    if (!isLoading && !isLastPage) {
                        if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                            // Load more data when near the end of the list
                            isLoading = true
                            onLoadMore()
                        }
                    }
                }
            }
            recyclerView.addOnScrollListener(scrollListener)
        }
    }
}
