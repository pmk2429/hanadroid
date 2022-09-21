package com.example.hanadroid.ui.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.load
import com.example.hanadroid.ui.views.SingleRowView
import java.text.SimpleDateFormat
import java.util.*

// https://www.raywenderlich.com/28513564-advanced-data-binding-in-android-binding-adapters
object ViewBindingAdapter {

    @BindingAdapter("android:visibility")
    fun View.setVisibility(visible: Boolean) {
        visibility = if (visible) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    @JvmStatic
    @BindingAdapter("activityTitle", requireAll = true)
    fun SingleRowView.setTitle(title: String) {
        this.title = title
    }

    @JvmStatic
    @BindingAdapter("activityValue", requireAll = true)
    fun SingleRowView.setValue(value: String) {
        this.value = value
    }

    @JvmStatic
    @BindingAdapter("capitalizeFirst")
    fun TextView.capitalizeFirst(value: String) {
        text =
            value.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
            }
    }

    @JvmStatic
    @BindingAdapter("date")
    fun TextView.formatDate(date: String) {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        formatter.parse(date)?.also {
            val finalFormatter = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
            text = finalFormatter.format(it)
        }
    }

    @JvmStatic
    @BindingAdapter("imageUrl")
    fun ImageView.loadImage(url: String) {
        load(url)
    }

}
