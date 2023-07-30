package com.example.hanadroid.util

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.hanadroid.ui.EntryActivity.Companion.KEY_USER_COMMENT_TEXT

class EmotionalAnalysisWorker(
    val context: Context,
    userParameters: WorkerParameters
) : Worker(context, userParameters) {

    companion object {
        const val KEY_USER_EMOTION_RESULT = "key.user.emotion.result"
    }

    override fun doWork(): Result {
        val text = inputData.getString(KEY_USER_COMMENT_TEXT)

        try {
            for (i in 0..200) {
                Log.i("~!@#$", "Emotion Analysing $i")
            }

            val outputData = Data.Builder()
                .putString(KEY_USER_EMOTION_RESULT, userEmotion)
                .build()

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.sendNotification(context)

            return Result.success(outputData)
        } catch (e: Exception) {
            return Result.failure()
        }
    }

    private val userEmotion = listOf("Sad", "Happy", "Angry", "Surprise", "Tired", "Bored").random()

}
