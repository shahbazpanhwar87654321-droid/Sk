package com.example.ads

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * AdManager abstraction.
 * Uses Google AdMob official test IDs:
 * App ID: ca-app-pub-3940256099942544~3347511713
 * Banner Test ID: ca-app-pub-3940256099942544/6300978111
 * Interstitial Test ID: ca-app-pub-3940256099942544/1033173712
 */
object AdManager {
    const val TEST_BANNER_ID = "ca-app-pub-3940256099942544/6300978111"
    const val TEST_INTERSTITIAL_ID = "ca-app-pub-3940256099942544/1033173712"

    private val _isAdLoaded = MutableStateFlow(true)
    val isAdLoaded: StateFlow<Boolean> = _isAdLoaded.asStateFlow()

    fun showInterstitialAd(context: Context, onDismiss: () -> Unit) {
        // Safe development simulation: invokes onDismiss so flow is never blocked
        onDismiss()
    }
}
