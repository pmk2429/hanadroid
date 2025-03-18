// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath ("com.android.tools.build:gradle:8.4.0")
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.10")
        classpath ("com.google.dagger:hilt-android-gradle-plugin:2.51.1")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

plugins {
    id("org.jetbrains.kotlin.jvm") version ("1.9.23") apply false
    id("org.jetbrains.kotlin.android") version ("1.9.23") apply false
    id("com.google.devtools.ksp") version ("1.9.10-1.0.13")
}
