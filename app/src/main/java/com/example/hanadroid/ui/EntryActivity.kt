package com.example.hanadroid.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
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
import com.example.hanadroid.databinding.ActivityEntryBinding
import com.example.hanadroid.util.createNotificationChannel

class EntryActivity : AppCompatActivity() {

    private var _binding: ActivityEntryBinding? = null
    private val binding get() = _binding!!

    private val myBroadcastReceiver by lazy { AirplaneModeBroadcastReceiver() }

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

    private val workerActivityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
            }
        }

    private val requestNotificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) createNotificationChannel(this)
            else Toast.makeText(this, "No Notif Permission", Toast.LENGTH_LONG)
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

        binding.fabFancyItem.apply {
            setOnClickListener { launchFancyItemActivity() }
        }

        binding.startLauncherActivity.setOnClickListener {
            launchImplicitIntentsActivity()
        }

        binding.startTimerActivity.setOnClickListener {
            launchTimerActivity()
        }

        binding.startLongRunningTaskActivity.setOnClickListener {
            launchWorkerActivity()
        }

        checkPermissions()
        checkNotificationPermission()
    }

    override fun onResume() {
        super.onResume()
        initBroadcastReceiver()
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

    private fun launchFancyItemActivity() {
        val intent = Intent(this, FancyListActivity::class.java)
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

    private fun launchWorkerActivity() {
        workerActivityLauncher.launch(Intent(this, WorkerActivity::class.java))
    }

    private fun initBroadcastReceiver() {
        val listenToBroadcastsFromOtherApps = false
        val receiverFlags = if (listenToBroadcastsFromOtherApps) {
            ContextCompat.RECEIVER_EXPORTED
        } else {
            ContextCompat.RECEIVER_NOT_EXPORTED
        }
        IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED).also {
            registerReceiver(myBroadcastReceiver, it, receiverFlags)
        }
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(myBroadcastReceiver)
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

    private fun checkPermissions() {
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
}
