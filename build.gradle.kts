// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath ("com.android.tools.build:gradle:8.2.2")
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.21")
        classpath ("com.google.dagger:hilt-android-gradle-plugin:2.50")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

plugins {
    id("org.jetbrains.kotlin.jvm") version ("1.9.21") apply false
    id("com.google.devtools.ksp") version ("1.9.10-1.0.13")
    id("org.jetbrains.kotlin.android") version ("1.9.21") apply false
}
