package com.example.hanadroid.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.hanadroid.databinding.SingleRowViewBinding

class SingleRowView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: SingleRowViewBinding = SingleRowViewBinding.inflate(
        LayoutInflater.from(context), this
    )

    init {
        binding.apply {
            // nothing to do. Intentionally left blank
        }
    }

    var title: String = ""
        set(value) {
            field = value
            binding.activityTitle.text = value
        }

    var value: String = ""
        set(value) {
            field = value
            binding.activityValue.text = value
        }

}
