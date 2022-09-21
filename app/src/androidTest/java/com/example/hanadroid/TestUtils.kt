package com.example.hanadroid

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle

@Suppress("Unused")
inline fun <reified F : Fragment> launchFragmentInContainer(
    fragmentArgs: Bundle? = null,
    factory: FragmentFactory? = null,
    initialState: Lifecycle.State = Lifecycle.State.RESUMED
): FragmentScenario<F> =
    launchFragmentInContainer(fragmentArgs, R.style.Theme_HanaDroid, initialState, factory)
