package com.example.hanadroid.ui

import android.app.PictureInPictureParams
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Rational
import android.view.View
import android.widget.Button
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.example.hanadroid.R

class PictureInPictureMediaPlaybackActivity : AppCompatActivity() {

    private lateinit var videoView: VideoView
    private lateinit var pipButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picture_in_picture_media_playback)

        videoView = findViewById(R.id.video_view)
        pipButton = findViewById(R.id.pip_button)

        // Set up the video playback
        videoView.setVideoPath("https://www.example.com/sample.mp4")
        videoView.setOnPreparedListener {
            // Start video playback
            videoView.start()
        }

        // Check if the device supports PiP mode
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE)) {
            pipButton.setOnClickListener {
                // Enter Picture-in-Picture mode
                enterPictureInPictureMode()
            }
        } else {
            // Hide the PiP button if PiP mode is not supported
            pipButton.visibility = View.GONE
        }
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        // Enter Picture-in-Picture mode when the user leaves the activity
        enterPictureInPictureMode()
    }

    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration
    ) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        if (isInPictureInPictureMode) {
            // Hide the controls in PiP mode
            pipButton.visibility = View.GONE
        } else {
            // Show the controls when exiting PiP mode
            pipButton.visibility = View.VISIBLE
        }
    }

    override fun enterPictureInPictureMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val aspectRatio = Rational(videoView.width, videoView.height)
            val pipBuilder = PictureInPictureParams.Builder()
            pipBuilder.setAspectRatio(aspectRatio)
            enterPictureInPictureMode(pipBuilder.build())
        } else {
            enterPictureInPictureMode()
        }
    }
}
