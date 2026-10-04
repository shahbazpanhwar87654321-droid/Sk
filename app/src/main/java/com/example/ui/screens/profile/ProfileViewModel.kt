package com.example.ui.screens.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.billing.DefaultSubscriptionManager
import com.example.billing.PlanType
import com.example.billing.SubscriptionManager
import com.example.billing.SubscriptionState
import com.example.data.database.AppDatabase
import com.example.data.repository.ProfileRepository
import com.example.domain.model.CreatorProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ProfileUiState(
    val isEditingProfile: Boolean = false,
    val showUpgradeDialog: Boolean = false,
    val showAboutDialog: Boolean = false,
    val showPrivacyDialog: Boolean = false,
    val showTermsDialog: Boolean = false,
    val toastMessage: String? = null
)

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val repo = ProfileRepository(db.creatorProfileDao())
    val subscriptionManager: SubscriptionManager = DefaultSubscriptionManager()

    val profile: StateFlow<CreatorProfile> = repo.profile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CreatorProfile())

    val subscriptionState: StateFlow<SubscriptionState> = subscriptionManager.state

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun updateProfile(
        channelName: String,
        channelDescription: String,
        defaultGame: String,
        creatorGoal: String,
        subscribersGoal: Int,
        currentSubscribers: Int
    ) {
        viewModelScope.launch {
            val current = profile.value
            val updated = current.copy(
                channelName = channelName,
                channelDescription = channelDescription,
                defaultGame = defaultGame,
                creatorGoal = creatorGoal,
                subscribersGoal = subscribersGoal,
                currentSubscribers = currentSubscribers
            )
            repo.saveProfile(updated)
            _uiState.value = _uiState.value.copy(
                isEditingProfile = false,
                toastMessage = "Profile updated!"
            )
        }
    }

    fun setEditingProfile(editing: Boolean) {
        _uiState.value = _uiState.value.copy(isEditingProfile = editing)
    }

    fun setShowUpgradeDialog(show: Boolean) {
        _uiState.value = _uiState.value.copy(showUpgradeDialog = show)
    }

    fun setShowAboutDialog(show: Boolean) {
        _uiState.value = _uiState.value.copy(showAboutDialog = show)
    }

    fun setShowPrivacyDialog(show: Boolean) {
        _uiState.value = _uiState.value.copy(showPrivacyDialog = show)
    }

    fun setShowTermsDialog(show: Boolean) {
        _uiState.value = _uiState.value.copy(showTermsDialog = show)
    }

    fun toggleDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            repo.saveProfile(profile.value.copy(isDarkMode = enabled))
        }
    }

    fun toggleNotifications(enabled: Boolean) {
        viewModelScope.launch {
            repo.saveProfile(profile.value.copy(notificationsEnabled = enabled))
        }
    }

    fun setExportQuality(quality: String) {
        viewModelScope.launch {
            repo.saveProfile(profile.value.copy(exportQuality = quality))
        }
    }

    fun upgradePlan(plan: PlanType) {
        subscriptionManager.upgradeToPremium(plan)
        viewModelScope.launch {
            repo.saveProfile(profile.value.copy(isPremium = true))
            _uiState.value = _uiState.value.copy(
                showUpgradeDialog = false,
                toastMessage = "Upgraded to Pro Creator!"
            )
        }
    }

    fun clearLocalData() {
        viewModelScope.launch {
            db.clearAllTables()
            _uiState.value = _uiState.value.copy(toastMessage = "Local data cleared!")
        }
    }

    fun clearToast() {
        _uiState.value = _uiState.value.copy(toastMessage = null)
    }
}
