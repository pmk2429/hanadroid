package com.example.hanadroid.ui.views

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.LinearLayout

class CustomLinearLayout(
    context: Context,
    attrs: AttributeSet
) : LinearLayout(context, attrs) {
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        Log.d("Drawing View", "LinearLayout: entering onMeasure(). Measured width: $measuredWidth")
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Log.d("Drawing View", "LinearLayout: leaving onMeasure(). Measured width: $measuredWidth")
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        Log.d("Drawing View", "LinearLayout: entering onLayout(). Actual width: $width")
        super.onLayout(changed, l, t, r, b)
        Log.d("Drawing View", "LinearLayout: leaving onLayout(). Actual width: $width")
    }
}
