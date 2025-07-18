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
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.hanadroid.broadcastreceivers.AirplaneModeBroadcastReceiver
import com.example.hanadroid.broadcastreceivers.NotificationIntentReceiver
import com.example.hanadroid.databinding.ActivityEntryBinding
import com.example.hanadroid.util.HanaAppResultContract
import com.example.hanadroid.util.createNotificationChannel
import com.example.hanadroid.util.setDebouncedOnClickListener
import com.example.hanadroid.util.showNotificationWithAcceptAndDeclineActionsPendingIntent

class EntryActivity : AppCompatActivity() {

    private var _binding: ActivityEntryBinding? = null
    private val binding get() = _binding!!

    private var hanaActivityLauncher: ActivityResultLauncher<Any> = registerForActivityResult(
        HanaAppResultContract() // similar to ActivityResultContracts.StartActivityForResult()
    ) { result ->
        handleActivityResult(result)
    }

    private val myBroadcastReceiver by lazy { AirplaneModeBroadcastReceiver() }
    private val notificationIntentReceiver by lazy { NotificationIntentReceiver() }

    private val notificationManager: NotificationManager by lazy {
        getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }

    private val requestNotificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) createNotificationChannel(this)
            else Toast.makeText(this, "No Notif Permission", Toast.LENGTH_LONG).show()
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkAllPermissions()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkNotificationPermission()
        }
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

            countriesGqlCompose.setOnClickListener {
                launchActivity(CountriesComposeGQLActivity::class.java)
            }
        }
    }

    internal fun handleActivityResult(result: ActivityResult) {

    }

    private fun launchActivity(activityClass: Class<*>?) {
        activityLauncher.launch(Intent(this, activityClass))
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    private fun initBroadcastReceivers() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val listenToBroadcastsFromOtherApps = false
            val receiverFlags = if (listenToBroadcastsFromOtherApps) {
                ContextCompat.RECEIVER_EXPORTED
            } else {
                ContextCompat.RECEIVER_NOT_EXPORTED
            }

            // Register airplane mode receiver
            IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED).also {
                registerReceiver(myBroadcastReceiver, it, receiverFlags)
            }

            // Register Notification Receiver
            val intentFilter = IntentFilter().apply {
                addAction(NotificationIntentReceiver.ACCEPT_NOTIFICATION_ACTION)
                addAction(NotificationIntentReceiver.DECLINE_NOTIFICATION_ACTION)
            }
            registerReceiver(notificationIntentReceiver, intentFilter, receiverFlags)
        } else {
            // For older Android versions, use the original registration method
            IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED).also {
                registerReceiver(myBroadcastReceiver, it)
            }

            val intentFilter = IntentFilter().apply {
                addAction(NotificationIntentReceiver.ACCEPT_NOTIFICATION_ACTION)
                addAction(NotificationIntentReceiver.DECLINE_NOTIFICATION_ACTION)
            }
            registerReceiver(notificationIntentReceiver, intentFilter)
        }
    }

    override fun onStop() {
        super.onStop()
        try {
            unregisterReceiver(myBroadcastReceiver)
            unregisterReceiver(notificationIntentReceiver)
        } catch (e: IllegalArgumentException) {
            // Receiver was not registered, ignore
            Log.e("EntryActivity", "Receiver not registered: ${e.message}")
        }
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
        val permissions = try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                packageManager.getPackageInfo(
                    packageName,
                    PackageManager.PackageInfoFlags.of(PackageManager.GET_PERMISSIONS.toLong())
                ).requestedPermissions
            } else {
                packageManager.getPackageInfo(
                    packageName,
                    PackageManager.GET_PERMISSIONS
                ).requestedPermissions
            }
        } catch (e: Exception) {
            Log.e("EntryActivity", "Error getting permissions: ${e.message}")
            null
        }

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
                createNotificationChannel(this)
            }

            shouldShowRequestPermissionRationale(permission) -> {
                // permission denied permanently
                Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show()
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
