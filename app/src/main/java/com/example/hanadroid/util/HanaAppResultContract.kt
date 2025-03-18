package com.example.hanadroid.util

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContract

class HanaAppResultContract : ActivityResultContract<Any, ActivityResult>() {
    override fun createIntent(context: Context, input: Any): Intent =
        when (input) {
            is Intent -> {
                input
            }

            else -> {
                Intent()
            }
        }

    override fun parseResult(
        resultCode: Int,
        intent: Intent?
    ): ActivityResult = ActivityResult(resultCode, intent)
}