# HanaDroid

# Ricky&MortyAPIDemo

A simple app that
showcases [Guide to App Architecture](https://developer.android.com/topic/architecture).

A simple app that consumes The Rick and Morty API which is a REST API based on the television show
Rick and Morty.

## Setup Requirements

- Android device or emulator
- Android Studio

## Getting Started

In order to get the app running yourself, you need to:

1. clone this project
2. Import the project into Android Studio
3. Connect the android device with USB or just use your emulator
4. In Android Studio, click on the "Run" button.

## Libraries

Libraries used in the whole application are:

- [Kotlin](https://developer.android.com/kotlin) - Kotlin is a programming language that can run on
  JVM. Google has announced Kotlin as one of its officially supported programming languages in
  Android Studio; and the Android community is migrating at a pace from Java to Kotlin
- [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) -The ViewModel
  class is designed to store and manage UI-related data in a lifecycle conscious way
- [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) - A
  lifecycle-aware data holder with the observer pattern
- [Kotlin Coroutines](https://developer.android.com/kotlin/coroutines) - A concurrency design
  pattern that you can use on Android to simplify code that executes asynchronously
- [Paging 3 library](https://developer.android.com/topic/libraries/architecture/paging/v3-overview)
    - The Paging library helps you load and display pages of data from a larger dataset from local
      storage or over network. This approach allows your app to use both network bandwidth and
      system resources more efficiently.
- [Retrofit](https://square.github.io/retrofit) - Retrofit is a REST Client for Java and Android by
  Square inc under Apache 2.0 license. Its a simple network library that used for network
  transactions. By using this library we can seamlessly capture JSON response from web service/web
  API.
- [UseCases](https://developer.android.com/topic/architecture/domain-layer) - The domain layer is
  responsible for encapsulating complex business logic, or simple business logic that is reused by
  multiple ViewModels. This layer is optional because not all apps will have these requirements. You
  should only use it when needed-for example, to handle complexity or favor reusability.
- [Glide](https://github.com/bumptech/glide)- An image loading and caching library for Android
  focused on smooth scrolling.
