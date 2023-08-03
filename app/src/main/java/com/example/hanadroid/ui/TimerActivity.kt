package com.example.hanadroid.ui

import android.os.Bundle
import android.os.CountDownTimer
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

        timerDataHelper = TimerDataHelper(this)
        timerTask = MyTimeTask(timerDataHelper)

        // Clock Timer
        binding.btnStartTimer.setOnClickListener { startStopAction() }
        binding.btnResetTimer.setOnClickListener { resetAction() }

        // Countdown Timer
        binding.btnCountdownStartStop.setOnClickListener {
            updateButtonText()
            if (!isTimerRunning) {
                startCountdownTimer()
            } else {
                stopCountdownTimer()
            }
        }

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

    companion object {
        private const val TIMER_COUNT = 30000L
        private const val COUNTDOWN_INTERVAL = 1000L
    }
}
