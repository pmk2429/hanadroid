package com.example.hanadroid.ui

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
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
                    Toast.makeText(context, "Download completed!", Toast.LENGTH_SHORT).show()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDownloadStuffBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnDownload.setOnClickListener {
            // Show progress bar while downloading
            binding.downloadProgressBar.visibility = View.VISIBLE
            downloadFile("http://speedtest.ftp.otenet.gr/files/test10Mb.db", "SpeedTest10Mb.db")
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

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(downloadCompleteBroadcastReceiver)
    }

    @SuppressLint("Range")
    private fun downloadFile(url: String, fileName: String) {
        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        val request = DownloadManager.Request(Uri.parse(url))
            .setTitle("Downloading $fileName")
            .setDescription("Downloading file using DownloadManager.")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)

        downloadId = downloadManager.enqueue(request)

        // using query method to update the Progress for other states
        var finishDownload = false
        var progress = 0
        while (!finishDownload) {
            val cursor = downloadManager.query(DownloadManager.Query().setFilterById(downloadId))
            if (cursor.moveToFirst()) {
                val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                when (status) {
                    DownloadManager.STATUS_FAILED,
                    DownloadManager.STATUS_PAUSED,
                    DownloadManager.STATUS_PENDING -> {
                        binding.downloadProgressBar.visibility = View.INVISIBLE
                    }

                    DownloadManager.STATUS_RUNNING -> {
                        binding.downloadProgressBar.visibility = View.VISIBLE
                        val total =
                            cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
                        if (total >= 0) {
                            val downloaded =
                                cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                            progress = ((downloaded * 100L) / total).toInt()
                        }
                    }

                    DownloadManager.STATUS_SUCCESSFUL -> {
                        progress = 100
                        finishDownload = true
                        Toast.makeText(this, "Download Completed", Toast.LENGTH_SHORT).show()
                        binding.downloadProgressBar.visibility = View.INVISIBLE
                    }
                }
            }
        }
    }
}
