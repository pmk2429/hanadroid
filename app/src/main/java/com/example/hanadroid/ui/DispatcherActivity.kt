package com.example.hanadroid.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.hanadroid.R
import com.example.hanadroid.data.universityapi.UniversityApiHelper
import com.example.hanadroid.networking.NetworkResult
import com.example.hanadroid.networking.dispatcher.NetworkOperationTask
import com.example.hanadroid.networking.dispatcher.QueueManager
import kotlinx.coroutines.launch
import javax.inject.Inject

class DispatcherActivity : AppCompatActivity() {

    @Inject
    lateinit var queueManager: QueueManager

    @Inject
    lateinit var universityApiHelper: UniversityApiHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dispatcher)

        super.onStart()
        // Enqueue tasks
        queueManager.enqueueTask {
            // Task 1
        }

        queueManager.enqueueTask {
            // Task 2
        }

        // Enqueue Retrofit network request
        queueManager.executeRequestOnWifi(this@DispatcherActivity, {
            // Make your Retrofit network request here
        }, { result ->
            result.onSuccess { data ->
                // Handle successful response
            }
            result.onFailure { error ->
                // Handle failure
            }
        })


        // Create an OperationTask and execute it
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                // TODO: Move this code to ViewModel and handle all cases there.
                val networkOperationTask = NetworkOperationTask { universityApiHelper.getUniversities() }
                val result = networkOperationTask.invoke()
                when (result) {
                    is NetworkResult.Success -> {
                        val data = result.data
                        // Handle successful response
                    }

                    is NetworkResult.Error -> {
                        val code = result.code
                        val message = result.message
                        // Handle error
                    }

                    is NetworkResult.Exception -> {
                        val exception = result.e
                        // Handle exception
                    }
                }
            }
        }
    }
}