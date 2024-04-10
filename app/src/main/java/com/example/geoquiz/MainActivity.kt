package com.example.geoquiz

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.geoquiz.ui.ErrorScreen
import com.example.geoquiz.ui.theme.GeoQuizTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GeoQuizTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    observeNetworkConnectivity()
                }
            }
        }
    }

    override fun onBackPressed() {
        // Do nothing
    }

    private fun observeNetworkConnectivity() {
        val connectivityLiveData = ConnectivityLiveData(this)

        connectivityLiveData.observe(this, Observer { isConnected ->
            if (isConnected) {
                // Internet is available, show GeoQuizApp
                setContent {
                    GeoQuizTheme {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            GeoQuizApp()
                        }
                    }
                }
            } else {
                // Internet is not available, show ErrorApp
                setContent {
                    GeoQuizTheme {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            ErrorScreen()
                        }
                    }
                }
            }
        })
    }
}

class ConnectivityLiveData(private val context: Context) : LiveData<Boolean>() {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            postValue(true)
        }

        override fun onLost(network: Network) {
            postValue(false)
        }
    }

    override fun onActive() {
        super.onActive()
        val builder = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        connectivityManager.registerNetworkCallback(builder.build(), networkCallback)
    }

    override fun onInactive() {
        super.onInactive()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}