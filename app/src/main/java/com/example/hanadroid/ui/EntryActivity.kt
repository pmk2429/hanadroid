package com.example.hanadroid.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.work.*
import com.example.hanadroid.R
import com.example.hanadroid.databinding.ActivityEntryBinding
import com.example.hanadroid.util.EmotionalAnalysisWorker
import com.example.hanadroid.util.EmotionalAnalysisWorker.Companion.KEY_USER_EMOTION_RESULT
import com.example.hanadroid.util.UploadDataWorker
import com.example.hanadroid.util.WORKER_CHANNEL_ID
import java.util.concurrent.TimeUnit

class EntryActivity : AppCompatActivity() {

    private var _binding: ActivityEntryBinding? = null
    private val binding get() = _binding!!

    private val universityActivityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            // handle the Result
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
            }
        }

    private val paginationActivityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
            }
        }

    private val dogsActivityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let {
                    val dogUrl = it.getStringExtra("DOGGO")
                    Log.i("~!@#$", "Doggo Data --- : $dogUrl")
                }
            }
        }

    private val boredActivityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let {
                    val boredActivity =
                        it.getStringExtra(BoredActivityLauncherActivity.BORED_ACTIVITY_NAME)
                    Log.i("~!@#$", "Bored Activity --- : $boredActivity")
                }
            }
        }

    private val disneyCharacterActivityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let {
                }
            }
        }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fab.apply {
            setOnTouchListener(handleTouchListener)
            setOnClickListener { launchUniversityActivity() }
        }

        binding.fabBoredActivity.apply {
            setOnTouchListener(handleTouchListener)
            setOnClickListener { launchBoredActivity() }
        }

        binding.fabFetchDogs.apply {
            setOnClickListener { launchDogsActivity() }
        }

        binding.fabRedditPosts.apply {
            setOnClickListener { launchRedditPostsActivity() }
        }

        binding.fabDisneyCharacters.apply {
            setOnClickListener { launchDisneyCharactersActivity() }
        }

        binding.switchUploadData.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                startUploadingDogsData()
            } else {
                cancelUploadingDataRequest()
            }
        }

        binding.startLauncherActivity.setOnClickListener {
            launchImplicitIntentsActivity()
        }

        binding.startTimerActivity.setOnClickListener {
            launchTimerActivity()
        }

        checkNotificationPermission()
    }

    private fun launchUniversityActivity() {
        universityActivityLauncher.launch(Intent(this, UniversityMainActivity::class.java))
    }

    private fun launchBoredActivity() {
        boredActivityLauncher.launch(Intent(this, BoredActivityLauncherActivity::class.java))
    }

    private fun launchPaginationActivity() {
        paginationActivityLauncher.launch(Intent(this, ArticleListPaginationActivity::class.java))
    }

    private fun launchDogsActivity() {
        dogsActivityLauncher.launch(Intent(this, DogsMediaActivity::class.java))
    }

    private fun launchRedditPostsActivity() {
        val intent = Intent(this, RickAndMortyCharactersActivity::class.java)
        startActivity(intent)
    }

    private fun launchDisneyCharactersActivity() {
        val intent = Intent(this, DisneyCharactersActivity::class.java)
        startActivity(intent)
    }

    private fun launchImplicitIntentsActivity() {
        val intent = Intent(this, LaunchingIntentsActivity::class.java)
        startActivity(intent)
    }

    private fun launchTimerActivity() {
        val intent = Intent(this, TimerActivity::class.java)
        startActivity(intent)
    }

    /**
     * This gives you the touch event coordinates relative to the view that has the touch listener
     * assigned to it. The top left corner of the view is (0, 0).
     * If you move your finger above the view, then y will be negative.
     * If you move your finger left of the view, then x will be negative.
     */
    @SuppressLint("ClickableViewAccessibility")
    private val handleTouchListener = View.OnTouchListener { _, event ->
        val x = event.x.toInt()
        val y = event.y.toInt()

        val xRelToScreen = event.rawX.toInt()
        val yRelToScreen = event.rawY.toInt()

        when (event.action) {
            MotionEvent.ACTION_DOWN -> Log.i("~!@#", "touched down ($x, $y)")
            MotionEvent.ACTION_MOVE -> Log.i("~!@#", "moving: ($x, $y)")
            MotionEvent.ACTION_UP -> Log.i("~!@#", "touched up ($x, $y)")
        }
        false
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        val name = getString(R.string.channel_name)
        val descriptionText = getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(WORKER_CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun startUploadingDogsData() {
        val workManager = WorkManager.getInstance(this)

        val sendingLog = PeriodicWorkRequestBuilder<UploadDataWorker>(10, TimeUnit.SECONDS)
            .addTag(TAG_WORKER_LOG)
            .build()

        workManager.enqueue(sendingLog)

        workManager.getWorkInfoByIdLiveData(sendingLog.id).observe(this) { workInfo ->
            binding.textViewWorkState.text = workInfo.state.name
        }
    }

    private fun cancelUploadingDataRequest() {
        WorkManager.getInstance(this)
            .cancelAllWorkByTag(TAG_WORKER_LOG)
    }

    private fun setOneTimeEmotionalAnalysisWorkRequest(userText: String) {
        val workManager = WorkManager.getInstance(this)

        val inputData = Data.Builder()
            .putString(KEY_USER_COMMENT_TEXT, userText)
            .build()

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val emotionalAnalysisWorker = OneTimeWorkRequestBuilder<EmotionalAnalysisWorker>()
            .setInputData(inputData)
            .setConstraints(constraints)
            .build()

        workManager.enqueue(emotionalAnalysisWorker)

        workManager.getWorkInfoByIdLiveData(emotionalAnalysisWorker.id)
            .observe(this) { workInfo ->
                binding.textViewWorkState.text = workInfo.state.name

                if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                    val userEmotionResult = workInfo.outputData.getString(KEY_USER_EMOTION_RESULT)
                    Toast.makeText(this, userEmotionResult, Toast.LENGTH_SHORT).show()
                }
            }
    }

    private val requestNotificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) createNotificationChannel()
            else Toast.makeText(this, "No Notif Permission", Toast.LENGTH_LONG)
        }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun checkNotificationPermission() {
        val permission = Manifest.permission.POST_NOTIFICATIONS
        when {
            ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED -> {
                // make your action here
            }

            shouldShowRequestPermissionRationale(permission) -> {
                // permission denied permanently
            }

            else -> {
                requestNotificationPermission.launch(permission)
            }
        }
    }

    companion object {
        private const val TAG_WORKER_LOG = "worker_log"
        const val KEY_USER_COMMENT_TEXT = "key.user.comment.text"
    }
}
