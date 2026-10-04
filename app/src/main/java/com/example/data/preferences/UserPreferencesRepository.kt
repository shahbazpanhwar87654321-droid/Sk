package com.example.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "gaming_creator_settings")

class UserPreferencesRepository(private val context: Context) {

    private val KEY_ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
    private val KEY_CHANNEL_NAME = stringPreferencesKey("channel_name")
    private val KEY_DARK_MODE = booleanPreferencesKey("dark_mode")
    private val KEY_IS_PREMIUM = booleanPreferencesKey("is_premium")

    val isOnboardingCompleted: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[KEY_ONBOARDING_COMPLETED] ?: false
    }

    val channelName: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[KEY_CHANNEL_NAME] ?: "Gaming Videos Creator"
    }

    val isDarkMode: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[KEY_DARK_MODE] ?: true
    }

    val isPremium: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[KEY_IS_PREMIUM] ?: false
    }

    suspend fun setOnboardingCompleted(completed: Boolean, channelName: String = "") {
        context.dataStore.edit { prefs ->
            prefs[KEY_ONBOARDING_COMPLETED] = completed
            if (channelName.isNotBlank()) {
                prefs[KEY_CHANNEL_NAME] = channelName
            }
        }
    }

    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[KEY_DARK_MODE] = enabled
        }
    }

    suspend fun setPremium(isPremium: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[KEY_IS_PREMIUM] = isPremium
        }
    }
}
