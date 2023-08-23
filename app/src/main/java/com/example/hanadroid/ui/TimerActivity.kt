package com.example.hanadroid.ui

import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.hanadroid.R
import com.example.hanadroid.databinding.ActivityTimerBinding
import com.example.hanadroid.sharedprefs.TimerDataHelper
import java.util.Date
import java.util.Timer
import java.util.TimerTask

class TimerActivity : AppCompatActivity() {

    private var _binding: ActivityTimerBinding? = null
    val binding get() = _binding!!

    private val timer = Timer()
    private lateinit var timerDataHelper: TimerDataHelper
    private lateinit var timerTask: MyTimeTask

    private var isTimerRunning: Boolean = false

    // Handler and Runnable based Timer
    private lateinit var handler: Handler
    private var secondsLeft = 60

    private val countDownTimer = object : CountDownTimer(
        TIMER_COUNT,
        COUNTDOWN_INTERVAL
    ) {
        // Callback function, fired on regular interval
        override fun onTick(millisUntilFinished: Long) {
            binding.apply {
                countdownText.text = "seconds remaining: " + millisUntilFinished / 1000
            }
        }

        override fun onFinish() {
            binding.countdownText.text = "00"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityTimerBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        initTimerTask()
        initCountdownTimer()
        initHandlerCountdownTimer()
    }

    private fun initTimerTask() {
        timerDataHelper = TimerDataHelper(this)
        timerTask = MyTimeTask(timerDataHelper)

        // Clock Timer
        binding.btnStartTimer.setOnClickListener { startStopAction() }
        binding.btnResetTimer.setOnClickListener { resetAction() }

        if (timerDataHelper.isTimerRunning) {
            startTimer()
        } else {
            stopTimer()
            if (timerDataHelper.startTime != null && timerDataHelper.stopTime != null) {
                val time = Date().time - timerTask.calcRestartTime().time
                displayTime(time)
            }
        }

        timer.scheduleAtFixedRate(timerTask, 0, 500)
    }

    private fun startStopAction() {
        if (timerDataHelper.isTimerRunning) {
            timerDataHelper.setStopTime(Date())
            stopTimer()
        } else {
            if (timerDataHelper.stopTime != null) {
                timerDataHelper.setStartTime(timerTask.calcRestartTime())
                timerDataHelper.setStopTime(null)
            } else {
                timerDataHelper.setStartTime(Date())
            }
            startTimer()
        }
    }

    private fun resetAction() {
        timerDataHelper.setStopTime(null)
        timerDataHelper.setStartTime(null)
        stopTimer()
        displayTime(0)
    }

    private fun stopTimer() {
        timerDataHelper.setTimerCounting(false)
        binding.btnStartTimer.text = getString(R.string.start)
    }

    private fun startTimer() {
        timerDataHelper.setTimerCounting(true)
        binding.btnStartTimer.text = getString(R.string.stop)
    }

    private fun displayTime(timeValue: Long) {
        binding.timeTextView.text = timerTask.timeStringFromLong(timeValue)
    }

    inner class MyTimeTask(
        private val timerDataHelper: TimerDataHelper
    ) : TimerTask() {

        override fun run() {
            if (timerDataHelper.isTimerRunning) {
                val time = Date().time - timerDataHelper.startTime!!.time
                displayTime(time)
            }
        }

        fun calcRestartTime(): Date {
            val diff = timerDataHelper.startTime!!.time - timerDataHelper.stopTime!!.time
            return Date(System.currentTimeMillis() + diff)
        }

        fun timeStringFromLong(millis: Long): String {
            val seconds = (millis / 1000) % 60
            val minutes = (millis / (1000 * 60) % 60)
            val hours = (millis / (1000 * 60 * 60) % 24)
            return makeTimeString(hours, minutes, seconds)
        }

        private fun makeTimeString(hours: Long, minutes: Long, seconds: Long): String {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds)
        }
    }

    // Countdown Timer
    private fun initCountdownTimer() {
        binding.btnCountdownStartStop.setOnClickListener {
            updateButtonText()
            if (!isTimerRunning) {
                startCountdownTimer()
            } else {
                stopCountdownTimer()
            }
        }
    }

    private fun initHandlerCountdownTimer() {
        handler = Handler(Looper.getMainLooper())

        handleViewBindings(secondsLeft.toString(), startEnabled = true)
        binding.apply {
            btnStartHandlerTimer.setOnClickListener { startHandlerCountdown() }
            btnStopHandlerTimer.setOnClickListener { stopHandlerCountdown() }
            btnResetHandlerTimer.setOnClickListener { resetHandlerCountdown() }
        }
    }

    private fun startCountdownTimer() {
        countDownTimer.start()
        isTimerRunning = !isTimerRunning
        updateButtonText()
    }

    private fun stopCountdownTimer() {
        countDownTimer.cancel()
        isTimerRunning = !isTimerRunning
        updateButtonText()
    }

    private fun updateButtonText() {
        binding.btnCountdownStartStop.text = if (isTimerRunning) "Stop" else "Start"
    }

    override fun onStop() {
        super.onStop()
        if (isTimerRunning) {
            stopCountdownTimer()
        }
    }

    private fun startHandlerCountdown() {
        handleViewBindings(secondsLeft.toString(), stopEnabled = true, resetEnabled = true)
        // Post a message to the handler to decrement the seconds left and update the UI
        handler.postDelayed({
            secondsLeft -= 1
            binding.handlerTimerText.text = secondsLeft.toString()

            // If the countdown is finished, stop the timer
            if (secondsLeft <= 0) {
                stopHandlerCountdown()
            } else {
                // Post another message to the handler to continue the countdown
                startHandlerCountdown()
            }
        }, 1000)
    }

    private fun stopHandlerCountdown() {
        // Remove all pending messages from the handler
        handler.removeCallbacksAndMessages(null)
        handleViewBindings("Countdown finished!", stopEnabled = true, resetEnabled = true)
    }

    private fun resetHandlerCountdown() {
        handler.removeCallbacksAndMessages(null)
        secondsLeft = 60
        handleViewBindings(secondsLeft.toString(), startEnabled = true)
    }

    private fun handleViewBindings(
        secondsLeft: String,
        startEnabled: Boolean = false,
        stopEnabled: Boolean = false,
        resetEnabled: Boolean = false
    ) {
        binding.apply {
            binding.handlerTimerText.text = secondsLeft
            binding.btnStartHandlerTimer.isEnabled = startEnabled
            binding.btnStopHandlerTimer.isEnabled = stopEnabled
            binding.btnResetHandlerTimer.isEnabled = resetEnabled
        }
    }

    companion object {
        private const val TIMER_COUNT = 30000L
        private const val COUNTDOWN_INTERVAL = 1000L
    }
}
