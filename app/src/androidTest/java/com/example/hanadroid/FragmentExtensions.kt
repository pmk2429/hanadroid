package com.hana.employees

import android.content.ComponentName
import android.content.Intent
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Bundle
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.Preconditions
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.navigation.NavHostController
import androidx.navigation.Navigation
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.core.app.ApplicationProvider

inline fun <reified T : Fragment> launchFragmentInHiltContainerDemo(
    fragmentArgs: Bundle? = null,
    @StyleRes themeResId: Int = R.style.Theme_SquareEmployeesDirectory,
    crossinline action: Fragment.() -> Unit = {}
) {
    val startActivityIntent = Intent.makeMainActivity(
        ComponentName(
            ApplicationProvider.getApplicationContext(),
            HiltTestActivity::class.java
        )
    ).putExtra(
        "androidx.fragment.app.testing.FragmentScenario.EmptyFragmentActivity.THEME_EXTRAS_BUNDLE_KEY",
        themeResId
    )

    ActivityScenario.launch<HiltTestActivity>(startActivityIntent).onActivity { activity ->
        val fragment: Fragment = activity.supportFragmentManager.fragmentFactory.instantiate(
            Preconditions.checkNotNull(T::class.java.classLoader),
            T::class.java.name
        )
        fragment.arguments = fragmentArgs
        activity.supportFragmentManager
            .beginTransaction()
            .add(android.R.id.content, fragment, "")
            .commitNow()

        (fragment as T).action()
    }
}

inline fun <reified F : Fragment> launchFragmentInHiltContainer(
    fragmentArgs: Bundle? = null,
    @StyleRes themeResId: Int = R.style.Theme_SquareEmployeesDirectory,
    factory: FragmentFactory? = null,
    customFragment: F? = null,
    navHostController: NavHostController? = null,
    crossinline action: Fragment.() -> Unit = {}
): ActivityScenario<HiltTestActivity> {
    return launchFragmentInHiltContainerWithActivity(
        fragmentArgs,
        themeResId,
        factory,
        customFragment,
        navHostController,
        action
    )
}

inline fun <reified F : Fragment, reified A : AppCompatActivity> launchFragmentInHiltContainerWithActivity(
    fragmentArgs: Bundle? = null,
    @StyleRes themeResId: Int = R.style.Theme_SquareEmployeesDirectory,
    factory: FragmentFactory? = null,
    customFragment: F? = null,
    navHostController: NavHostController? = null,
    crossinline action: Fragment.() -> Unit = {}
): ActivityScenario<A> {
    val startActivityIntent = Intent.makeMainActivity(
        ComponentName(
            ApplicationProvider.getApplicationContext(),
            A::class.java
        )
    ).putExtra(
        "androidx.fragment.app.testing.FragmentScenario.EmptyFragmentActivity.THEME_EXTRAS_BUNDLE_KEY",
        themeResId
    )

    return launch<A>(startActivityIntent).onActivity { activity ->
        val finalFragment =
            customFragment ?: checkNotNull(F::class.java.classLoader).let { classLoader ->
                factory?.let {
                    activity.supportFragmentManager.fragmentFactory = it
                }
                activity.supportFragmentManager.fragmentFactory.instantiate(
                    classLoader,
                    F::class.java.name
                )
            }

        finalFragment.apply {
            arguments = fragmentArgs
        }.also { fragment ->
            fragment.viewLifecycleOwnerLiveData.observeForever { viewLifeCycleOwner ->
                if (viewLifeCycleOwner != null) {
                    navHostController?.let {
                        Navigation.setViewNavController(fragment.requireView(), it)
                    }
                }
            }
        }.also {
            activity.supportFragmentManager
                .beginTransaction()
                .add(android.R.id.content, it, "")
                .commitNow()
            action(it as F)
        }
    }
}
