package com.example.hanadroid.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.example.hanadroid.databinding.ActivityDogMediaBinding
import com.example.hanadroid.ui.EntryActivity.Companion.ACTIVITY_RESULT_CODE
import com.example.hanadroid.ui.uistate.DogsUiState
import com.example.hanadroid.viewmodels.DogsViewModel
import com.example.hanadroid.viewmodels.HanaViewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DogsMediaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDogMediaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDogMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: DogsViewModel by viewModels {
            HanaViewModelFactory(this, savedInstanceState)
        }

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.bindData(viewModel.dogsUiState)
    }

    private fun ActivityDogMediaBinding.bindData(
        dogsUiState: StateFlow<DogsUiState>
    ) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                dogsUiState.collect { uiState ->
                    Glide.with(woofDog)
                        .load(uiState.woofImageUrl)
                        .into(woofDog)
                    Glide.with(ceoDog)
                        .load(uiState.ceoImageUrl)
                        .into(ceoDog)

                    finishAndReturn(uiState.woofImageUrl)
                }
            }
        }
    }

    private fun finishAndReturn(woofImageUrl: String?) {
        lifecycleScope.launch {
            setResult(RESULT_OK, Intent().apply {
                putExtra(DOG_ACTIVITY_NAME, woofImageUrl)
                putExtra(ACTIVITY_RESULT_CODE, 25)
            })
            delay(3000)
            finish()
        }
    }

    companion object {
        const val DOG_ACTIVITY_NAME = "DOG_ACTIVITY_NAME"
    }
}
