package com.example.hanadroid.ui

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.hanadroid.databinding.ActivityDownloadStuffBinding

class DownloadStuffActivity : AppCompatActivity() {

    private var _binding: ActivityDownloadStuffBinding? = null
    val binding get() = _binding!!

    private var downloadId: Long = 0

    private val downloadCompleteBroadcastReceiver: BroadcastReceiver =
        object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                if (id == downloadId) {
                    binding.downloadProgressBar.visibility = View.INVISIBLE
                    Toast.makeText(context, "Download completed!", Toast.LENGTH_SHORT).show()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDownloadStuffBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnDownload.setOnClickListener {
            downloadFile("https://example.com/image.jpg", "MyImage.jpg")
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
        IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE).also {
            registerReceiver(downloadCompleteBroadcastReceiver, it, receiverFlags)
        }
    }

    private fun downloadFile(url: String, fileName: String) {
        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        val request = DownloadManager.Request(Uri.parse(url))
            .setTitle("Downloading $fileName")
            .setDescription("Downloading file using DownloadManager.")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)

        downloadId = downloadManager.enqueue(request)

        // Show progress bar while downloading
        binding.downloadProgressBar.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(downloadCompleteBroadcastReceiver)
    }
}