package com.example.hanadroid.ui.fragments

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Insets
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.*
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.hanadroid.R
import com.example.hanadroid.databinding.FragmentRandomBoredActivityBinding
import com.example.hanadroid.ui.EntryActivity
import com.example.hanadroid.ui.views.SingleRowView
import com.example.hanadroid.util.*
import com.example.hanadroid.viewmodels.BoredActivityViewModel
import com.example.hanadroid.viewmodels.HanaViewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * Fragment that fetches and displays Bored Random Activities.
 */
class BoredActivityFragment @JvmOverloads constructor(
    factoryProducer: (() -> HanaViewModelFactory)? = null
) : Fragment() {

    private var _binding: FragmentRandomBoredActivityBinding? = null
    private val binding get() = _binding!!

    private val boredActivityViewModel: BoredActivityViewModel by viewModels(
        factoryProducer = factoryProducer ?: {
            HanaViewModelFactory(this, arguments)
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRandomBoredActivityBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = boredActivityViewModel

        setupObservers()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listAllChildViewsOfType(binding.boredActivityRowsContainer)
        listAllDirectChildren(binding.boredActivityContainer)
        //listAllChildViews(binding.boredActivityContainer)
        //testExtensions()
        testLambda()
        enumTesting()
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            // The block passed to repeatOnLifecycle is executed when the lifecycle
            // is at least STARTED and is cancelled when the lifecycle is STOPPED.
            // It automatically restarts the block when the lifecycle is STARTED again.
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Safely collect from boredActivityUiState (StateFlow) when the lifecycle is STARTED
                // and stops collection when the lifecycle is STOPPED
                boredActivityViewModel.boredActivityUiState.collect { uiState ->
                    Log.i("~!@#", "SUCCESS --> ${uiState.name}")
                    Log.i("~!@#", "LOADING --> ${uiState.isLoading}")
                    Log.i("~!@#", "ERROR --> ${uiState.failureMessage}")
                    launchNotification(uiState.name)
                }
            }
        }
    }

    private fun testExtensions() {
        val demo = "Pavitra"
        val first = demo.findFirstIndex { t -> t == 'a' }
        Log.i("~!@#", "$first")
        val last = demo.findLastIndex { t -> t == 'a' }
        Log.i("~!@#", "$last")
    }

    private fun testLambda() {
        val summation = sum(10, 20, addTwoNumbers())
        println(summation)
    }

    /** val lambdaName : () -> ReturnType = { args1, args2, ... -> codeBody } */
    private fun addTwoNumbers(): (Int, Int) -> Int = { first, second -> first + second }

    private fun sum(
        x: Int,
        y: Int,
        action: (Int, Int) -> Int // Notice how we are passing the lambda as a parameter to sum() method.
    ) = action(x, y)

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun listAllChildViewsOfType(parent: ViewGroup) {
        val singleRowViews = parent.getViewsByType(SingleRowView::class.java)
        for (view in singleRowViews) {
            Log.i("~!@#", view.toString())
        }
    }

    private fun listAllDirectChildren(parent: ViewGroup) {
        val allViews = parent.allDirectChildren<View>()
        for (view in allViews) {
            Log.i("~!@#", view.toString())
        }
        Log.i("~!@#", "------------------------------------------------------------------")
    }

    private fun listAllChildViews(parent: ViewGroup) {
        val allChildren = parent.allChildViews<View>()
        for (view in allChildren) {
            Log.i("~!@#", view.toString())
        }
    }

    /**
     * Same as extension function Extensions#allDirectChildren()
     */
    private fun ViewGroup.getAllChildren(): List<View> {
        val children = ArrayList<View>()
        for (i in 0 until this.childCount) {
            children.add(this.getChildAt(i))
        }
        return children
    }

    private fun scheduleWithCoroutine() = runBlocking {
        launch {
            delay(2000L)
            println("Function in scheduleWithCoroutine executed with delay ")
        }
    }

    private fun enumTesting() {
        val yellowColor = Colors.values().find {
            it.name == "Yellow"
        }
        val a: Colors = enumValueOf("Red")
        Log.i("~!@#", "enumTesting: $a")
    }

    @SuppressLint("NewApi")
    private fun getWindowMetrics() {
        val screenHeight = resources.displayMetrics.heightPixels
        val screenWidth = resources.displayMetrics.widthPixels

        // OR --- the new way
        val metrics: WindowMetrics = requireActivity().windowManager.currentWindowMetrics

        // Gets all excluding insets
        val windowInsets = metrics.windowInsets
        val insets: Insets = windowInsets.getInsetsIgnoringVisibility(
            WindowInsets.Type.navigationBars()
                    or WindowInsets.Type.displayCutout()
        )

        val insetsWidth: Int = insets.right + insets.left
        val insetsHeight: Int = insets.top + insets.bottom

        // Legacy size that Display#getSize reports
        val bounds: Rect = metrics.bounds
        val legacySize: Size = Size(
            bounds.width() - insetsWidth,
            bounds.height() - insetsHeight
        )
    }

    private fun launchNotification(name: String) {
        val notificationManager =
            requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationChannel =
            NotificationChannel(CHANNEL_ID, "Description", NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(notificationChannel)

        val newMessageNotification = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_comment)
            .setContentTitle("Random Activity")
            .setContentText(name)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager.notify(NOTIFICATION_ID, newMessageNotification)
    }

    private fun createNotificationChannel() {
        // Create an explicit intent for an Activity in your app
        val intent = Intent(requireContext(), EntryActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(requireContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_comment)
            .setContentTitle("My notification")
            .setContentText("Go back to Entry Activity!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            // Set the intent that will fire when the user taps the notification
            .setContentIntent(pendingIntent)
            // Notice this code calls setAutoCancel(), which automatically removes the notification when the user taps it.
            .setAutoCancel(true)

    }

    companion object {
        private const val CHANNEL_ID = "new_notification"
        private const val NOTIFICATION_ID = 12345
    }

}
