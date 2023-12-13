package com.example.hanadroid.ui

import android.app.AlertDialog
import android.app.NotificationManager
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.hanadroid.R
import com.example.hanadroid.broadcastreceivers.HanaBroadcastReceiver
import com.example.hanadroid.databinding.ActivityWorkerBinding
import com.example.hanadroid.util.sendNotification
import com.example.hanadroid.workers.EmotionalAnalysisWorker
import com.example.hanadroid.workers.EmotionalAnalysisWorker.Companion.EMOTIONAL_ANALYSIS_WORK_TAG
import com.example.hanadroid.workers.EmotionalAnalysisWorker.Companion.KEY_USER_COMMENT_TEXT
import com.example.hanadroid.workers.EmotionalAnalysisWorker.Companion.KEY_USER_EMOTION_RESULT
import com.example.hanadroid.workers.LongRunningTaskWorker
import com.example.hanadroid.workers.UploadDataWorker
import com.example.hanadroid.workers.UploadDataWorker.Companion.TAG_WORKER_LOG
import java.util.concurrent.TimeUnit


class WorkerActivity : AppCompatActivity() {

    private var _binding: ActivityWorkerBinding? = null
    val binding get() = _binding!!

    private val workManager: WorkManager by lazy { WorkManager.getInstance(this) }

    private val notificationManager: NotificationManager by lazy {
        getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }

    private val hanaBroadcastReceiver: HanaBroadcastReceiver by lazy { HanaBroadcastReceiver() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityWorkerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnStartLongRunningTask.setOnClickListener {
                startLongRunningTask()
            }
            fabStopWorker.setOnClickListener {
                cancelLongRunningTask()
            }

            // Switch Worker
            switchUploadData.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    startUploadingLogs()
                } else {
                    cancelUploadingLogs()
                }
            }

            btnSubmitUserInput.setOnClickListener {
                val inputText = editTextUserInput.text.toString().trim()
                if (inputText.isNotEmpty()) {
                    setOneTimeEmotionalAnalysisWorkRequest(inputText)
                }
            }

            btnSendBroadcast.setOnClickListener {
                val intent = Intent(HanaBroadcastReceiver.CUSTOM_BROADCAST_ACTION)
                sendBroadcast(intent)
            }

            btnLaunchDialog.setOnClickListener {
                launchDialog()
            }
        }
    }

    private fun launchDialog() {
        val builder = AlertDialog.Builder(this)
        with(builder) {
            setTitle("Alert")
            setMessage("We have a message")
            setIcon(android.R.drawable.ic_dialog_alert)

            setPositiveButton(android.R.string.ok) { _, _ ->
                Toast.makeText(
                    this@WorkerActivity,
                    android.R.string.ok, Toast.LENGTH_SHORT
                ).show()
            }

            setNegativeButton(android.R.string.cancel) { dialog, _ ->
                dialog.cancel()
            }

            setNeutralButton("Maybe") { _, _ ->
                Toast.makeText(
                    this@WorkerActivity,
                    "Maybe", Toast.LENGTH_SHORT
                ).show()
            }

            show()
        }
    }

    override fun onResume() {
        super.onResume()
        val listenToBroadcastsFromOtherApps = false
        val receiverFlags = if (listenToBroadcastsFromOtherApps) {
            ContextCompat.RECEIVER_EXPORTED
        } else {
            ContextCompat.RECEIVER_NOT_EXPORTED
        }
        IntentFilter(HanaBroadcastReceiver.CUSTOM_BROADCAST_ACTION).also {
            registerReceiver(hanaBroadcastReceiver, it, receiverFlags)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(hanaBroadcastReceiver)
    }

    /**
     * 1. Create Worker with Constraints, InputData
     * 2. Create the LongRunningWorker Request object
     * 3. Enqueue LongRunningWorker on to to the WorkManager
     * 4. Measure the progress of the worker using LiveData using ProgressBar and TextView
     */
    private fun startLongRunningTask() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val inputData = Data.Builder()
            .putLong(LongRunningTaskWorker.KEY_DURATION, 10000L)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<LongRunningTaskWorker>()
            .setConstraints(constraints)
            .setInputData(inputData)
            .addTag(LongRunningTaskWorker.WORKER_TAG)
            .build()

        workManager.enqueue(workRequest)

        // now observe LiveData
        workManager.getWorkInfoByIdLiveData(workRequest.id).observe(this) { workInfo ->
            workInfo?.let {
                // here the progress is send by LongRunningTaskWorker using `setProgressAsync()`
                val progress = it.progress.getInt(LongRunningTaskWorker.KEY_PROGRESS, 0)
                updateUI(progress, it.state.name)
                // if (it.state.isFinished) {
                //    notifyWorkerCompleted()
                // }
                when (it.state) {
                    WorkInfo.State.SUCCEEDED -> {
                        notifyWorkerCompleted()
                    }

                    WorkInfo.State.CANCELLED -> {
                        notifyWorkerInterrupted()
                    }

                    else -> {}
                }
            }
        }
    }

    private fun updateUI(progress: Int, status: String) {
        binding.apply {
            workerProgress.progress = progress
            val progressValue = "Uploading: $progress"
            progressText.text = progressValue
            val textColor = ContextCompat.getColor(this@WorkerActivity, R.color.teal_700)
            workerStatus.setTextColor(textColor)
            workerStatus.text = status
        }
    }

    /**
     * Sends out Notification that Worker is completed.
     */
    private fun notifyWorkerCompleted() {
        notificationManager.sendNotification(this, "Hurray!!!", "Long Running Task Completed")
    }

    private fun cancelLongRunningTask() {
        workManager.cancelAllWorkByTag(LongRunningTaskWorker.WORKER_TAG)
    }

    private fun notifyWorkerInterrupted() {
        binding.apply {
            workerStatus.text = "Work Cancelled"
            val textColor = ContextCompat.getColor(this@WorkerActivity, R.color.red)
            // workerStatus.setTextColor(Color.parseColor("#FF0000"));
            workerStatus.setTextColor(textColor);
        }
    }

    private fun startUploadingLogs() {
        /**
         * The minimum time interval for triggering PeriodicWork is 15 Minutes.
         * https://developer.android.com/reference/androidx/work/PeriodicWorkRequest
         */
        val sendingLog = PeriodicWorkRequestBuilder<UploadDataWorker>(20, TimeUnit.MINUTES)
            .addTag(TAG_WORKER_LOG)
            .build()

        // workManager.enqueue(sendingLog)

        workManager.enqueueUniquePeriodicWork(
            "periodicNotificationWork",
            ExistingPeriodicWorkPolicy.UPDATE, // Replace any existing work with the same name
            sendingLog
        )

        workManager.getWorkInfoByIdLiveData(sendingLog.id).observe(this) { workInfo ->
            workInfo?.let {
                val progress = it.progress.getInt(UploadDataWorker.KEY_UPLOAD_WORKER_PROGRESS, 0)
                binding.workerSwitchStateProgress.text = it.state.name
            }
        }
    }

    private fun cancelUploadingLogs() {
        workManager.cancelAllWorkByTag(TAG_WORKER_LOG)
    }

    /**
     * Fetches Input Text value from EditText and then does Emotional Analysis on it.
     */
    private fun setOneTimeEmotionalAnalysisWorkRequest(userInputText: String) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val inputData = Data.Builder()
            .putString(KEY_USER_COMMENT_TEXT, userInputText)
            .build()

        val emotionalAnalysisWorker = OneTimeWorkRequestBuilder<EmotionalAnalysisWorker>()
            .setConstraints(constraints)
            .setInputData(inputData)
            .addTag(EMOTIONAL_ANALYSIS_WORK_TAG)
            .build()

        workManager.enqueue(emotionalAnalysisWorker)

        workManager.getWorkInfoByIdLiveData(emotionalAnalysisWorker.id).observe(this) { workInfo ->
            workInfo?.let {
                binding.workerEmotionalProgress.text = it.state.name
                if (it.state == WorkInfo.State.SUCCEEDED) {
                    val emotionAnalysisResult = it.outputData.getString(KEY_USER_EMOTION_RESULT)
                    binding.emotionalAnalysisResult.text = emotionAnalysisResult
                }
            }
        }
    }

    private fun cancelEmotionalAnalysisWorker() {
        workManager.cancelAllWorkByTag(EMOTIONAL_ANALYSIS_WORK_TAG)
    }

    fun showSoftKeyboard(view: View) {
        if (view.requestFocus()) {
            val imm = getSystemService(InputMethodManager::class.java)
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }
}
