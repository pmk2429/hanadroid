package com.example.hanadroid.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest

class NetworkManager(
    val context: Context
) {
    fun isNetworkAvailable(): Boolean {
        val connectivityManager: ConnectivityManager =
            context.getSystemService(ConnectivityManager::class.java) as ConnectivityManager
        var currentNetwork = connectivityManager.activeNetwork
        var currentNetworkCapabilities = connectivityManager.getNetworkCapabilities(currentNetwork)
        // val network = connectivityManager.activeNetwork ?: return false
        // val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                super.onCapabilitiesChanged(network, networkCapabilities)
                currentNetwork = network
                currentNetworkCapabilities = networkCapabilities
            }
        }

        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)

        return when {
            currentNetworkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true -> true
            currentNetworkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true -> true
            else -> false
        }
    }
}
