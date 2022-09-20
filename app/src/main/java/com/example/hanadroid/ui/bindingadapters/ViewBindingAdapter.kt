package com.example.hanadroid.ui.bindingadapters

import android.view.View
import androidx.databinding.BindingAdapter
import com.example.hanadroid.ui.views.SingleRowView

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
}
