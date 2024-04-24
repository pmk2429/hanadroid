package com.example.hanadroid.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.hanadroid.databinding.ActivityUserInteractionsBinding
import com.example.hanadroid.viewmodels.InteractionViewModel

class UserInteractionsActivity : AppCompatActivity() {

    private var _binding: ActivityUserInteractionsBinding? = null
    private val binding get() = _binding!!

    private val interactionViewModel: InteractionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityUserInteractionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle the interaction here
        interactionViewModel.interactionLiveData.observe(this) { interaction ->
            when (interaction) {
                is Pair<*, *> -> handleFormInteraction(
                    interaction.first.toString(),
                    interaction.second.toString()
                )

                "Submit" -> handleSubmit()
            }
        }


        binding.apply {
            etInteractions.addTextChangedListener {
                interactionViewModel.enqueueInteraction(Pair("editText", it.toString()))
            }

            radioInteractions.setOnCheckedChangeListener { _, isChecked ->
                interactionViewModel.enqueueInteraction(Pair("radioButton", isChecked))
            }

            checkboxInteractions.setOnCheckedChangeListener { _, isChecked ->
                interactionViewModel.enqueueInteraction(Pair("checkBox", isChecked))
            }

            buttonInteractions.setOnClickListener {
                interactionViewModel.enqueueInteraction("Submit")
            }
        }
    }

    /**
     * Collect form data and put it in a Dequeue.
     */
    private fun handleFormInteraction(key: String, value: Any) {
        interactionViewModel.addFormData(key, value)
    }

    private fun handleSubmit() {
        // Get all form data and submit
        val formData = interactionViewModel.getFormData()
        // Process the formData, e.g., submit it to a server
        // Reset the form data after submission if needed
        interactionViewModel.clearFormData()
    }
}