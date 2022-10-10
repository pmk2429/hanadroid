package com.example.hanadroid.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.hanadroid.repository.RedditRepository

class RedditPostsViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: RedditRepository,
) : ViewModel() {

}