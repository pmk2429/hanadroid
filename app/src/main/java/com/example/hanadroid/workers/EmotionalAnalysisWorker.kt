package com.example.hanadroid.workers

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.hanadroid.util.sendNotification

class EmotionalAnalysisWorker(
    val context: Context,
    userParameters: WorkerParameters
) : Worker(context, userParameters) {

    override fun doWork(): Result {
        val text = inputData.getString(KEY_USER_COMMENT_TEXT)

        try {
            for (i in 0..500) {
                Log.i("~!@#$", "Emotion Analysing $i")
            }

            val outputData = Data.Builder()
                .putString(KEY_USER_EMOTION_RESULT, getUserEmotion())
                .build()

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.sendNotification(context)

            return Result.success(outputData)
        } catch (e: Exception) {
            return Result.failure()
        }
    }

    companion object {
        const val KEY_USER_EMOTION_RESULT = "key.user.emotion.result"
        const val EMOTIONAL_ANALYSIS_WORK_TAG = "emotional_analysis_tag"
        const val KEY_USER_COMMENT_TEXT = "key.user.comment.text"
    }

    private fun getUserEmotion(): String {
        val emotions = listOf("Sad", "Happy", "Angry", "Surprise", "Tired", "Bored")
        return emotions[(emotions.indices).random()]
    }

}
