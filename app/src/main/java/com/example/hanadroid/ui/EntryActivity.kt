package com.example.hanadroid.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.NotificationManager
import android.content.Intent
import android.content.IntentFilter
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
import com.example.hanadroid.broadcastreceivers.AirplaneModeBroadcastReceiver
import com.example.hanadroid.broadcastreceivers.NotificationIntentReceiver
import com.example.hanadroid.databinding.ActivityEntryBinding
import com.example.hanadroid.util.createNotificationChannel
import com.example.hanadroid.util.setDebouncedOnClickListener
import com.example.hanadroid.util.showNotificationWithAcceptAndDeclineActionsPendingIntent

class EntryActivity : AppCompatActivity() {

    private var _binding: ActivityEntryBinding? = null
    private val binding get() = _binding!!

    private val myBroadcastReceiver by lazy { AirplaneModeBroadcastReceiver() }
    private val notificationIntentReceiver by lazy { NotificationIntentReceiver() }

    private val notificationManager: NotificationManager by lazy {
        getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }

    private val requestNotificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) createNotificationChannel(this)
            else Toast.makeText(this, "No Notif Permission", Toast.LENGTH_LONG)
        }

    private val activityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let {
                    if (it.getIntExtra(ACTIVITY_RESULT_CODE, -1) == 24) {
                        val boredActivity =
                            it.getStringExtra(BoredActivityLauncherActivity.BORED_ACTIVITY_NAME)
                        Log.i("~!@#$", "Bored Activity --- : $boredActivity")
                    } else if (it.getIntExtra(ACTIVITY_RESULT_CODE, -1) == 25) {
                        val dogUrl = it.getStringExtra("DOGGO")
                        Log.i("~!@#$", "Doggo Data --- : $dogUrl")
                    } else Unit
                }
            }
        }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkAllPermissions()
        checkNotificationPermission()
        bindOnClicks()
    }

    override fun onResume() {
        super.onResume()
        initBroadcastReceivers()
    }

    private fun bindOnClicks() {
        binding.apply {

            fab.apply {
                setOnTouchListener(handleTouchListener)
                setOnClickListener { launchActivity(UniversityMainActivity::class.java) }
            }

            fabBoredActivity.apply {
                setOnTouchListener(handleTouchListener)
                setOnClickListener { launchActivity(BoredActivityLauncherActivity::class.java) }
            }

            fabFetchDogs.setOnClickListener { launchActivity(DogsMediaActivity::class.java) }

            fabRickAndMorty.setOnClickListener { launchActivity(RickAndMortyCharactersActivity::class.java) }

            fabBeerData.setOnClickListener { launchActivity(BeerDataActivity::class.java) }

            fabFancyItem.setOnClickListener { launchActivity(FancyListActivity::class.java) }

            fabDownloadFile.setOnClickListener { launchActivity(DownloadStuffActivity::class.java) }

            fabMusic.setOnClickListener { launchActivity(MusicPlayerActivity::class.java) }

            fabDisney.setOnClickListener { launchActivity(DisneyCharactersActivity::class.java) }

            startIntentsLauncherActivity.setOnClickListener {
                launchActivity(
                    LaunchingIntentsActivity::class.java
                )
            }

            startTimerActivity.setOnClickListener { launchActivity(TimerActivity::class.java) }

            startWorkerActivity.setOnClickListener { launchActivity(WorkerActivity::class.java) }

            startTabbedFragmentsActivity.setOnClickListener { launchActivity(TabbedFragmentActivity::class.java) }

            mediaActivity.setDebouncedOnClickListener {
                Log.i("~!@#", "debounced clicked")
                launchActivity(MediaActivity::class.java)
            }

            customMediaNotification.setOnClickListener {
                notificationManager.showNotificationWithAcceptAndDeclineActionsPendingIntent(
                    this@EntryActivity,
                    EntryActivity::class.java
                )
            }
        }
    }

    private fun launchActivity(activityClass: Class<*>?) {
        activityLauncher.launch(Intent(this, activityClass))
    }

    private fun initBroadcastReceivers() {
        val listenToBroadcastsFromOtherApps = false
        val receiverFlags = if (listenToBroadcastsFromOtherApps) {
            ContextCompat.RECEIVER_EXPORTED
        } else {
            ContextCompat.RECEIVER_NOT_EXPORTED
        }
        IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED).also {
            registerReceiver(myBroadcastReceiver, it, receiverFlags)
        }

        // register Notification Receiver
        // In this case, sendBroadcast(intent) will be called by Android System and delivers the Intent
        val intentFilter = IntentFilter().apply {
            addAction(NotificationIntentReceiver.ACCEPT_NOTIFICATION_ACTION)
            addAction(NotificationIntentReceiver.DECLINE_NOTIFICATION_ACTION)
        }
        registerReceiver(notificationIntentReceiver, intentFilter)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(myBroadcastReceiver)
        unregisterReceiver(notificationIntentReceiver)
    }

    /**
     * This gives you the touch event coordinates relative to the view that has the touch listener
     * assigned to it. The top left corner of the view is (0, 0).
     * If you move your finger above the view, then y will be negative.
     * If you move your finger left of the view, then x will be negative.
     */
    @SuppressLint("ClickableViewAccessibility")
    val handleTouchListener = View.OnTouchListener { _, event ->
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

    private fun checkAllPermissions() {
        val packageManager = packageManager
        val packageName = packageName

        // Get the list of permissions declared in the manifest
        val permissions = packageManager.getPackageInfo(
            packageName,
            PackageManager.GET_PERMISSIONS
        ).requestedPermissions

        // Print the list of permissions
        permissions?.forEach { permission ->
            println("Declared permission: $permission")
        }
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
        const val ACTIVITY_RESULT_CODE = "ACTIVITY_RESULT_CODE" // 24 for Bored Activity
    }
}
