package com.example.hanadroid.ui.views

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatTextView

class CustomTextView(
    context: Context,
    attrs: AttributeSet?
) : AppCompatTextView(context, attrs) {
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        Log.d("Drawing View", "TextView: entering onMeasure(). Measured width: $measuredWidth")
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Log.d("Drawing View", "TextView: leaving onMeasure(). Measured width: $measuredWidth")
    }

    override fun layout(l: Int, t: Int, r: Int, b: Int) {
        Log.d("Drawing View", "TextView: entering layout(). Actual width: $width")
        super.layout(l, t, r, b)
        Log.d("Drawing View", "TextView: leaving layout(). Actual width: $width")
    }

    override fun draw(canvas: Canvas) {
        Log.d("Drawing View", "TextView: draw() executed")
        super.draw(canvas)
    }
}