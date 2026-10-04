package com.example.ui.screens.onboarding

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.AppDatabase
import com.example.data.preferences.UserPreferencesRepository
import com.example.data.repository.ProfileRepository
import com.example.domain.model.CreatorProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class OnboardingStep(
    val title: String,
    val description: String,
    val iconName: String,
    val badge: String
)

class OnboardingViewModel(application: Application) : AndroidViewModel(application) {

    private val prefsRepo = UserPreferencesRepository(application)
    private val db = AppDatabase.getInstance(application)
    private val profileRepo = ProfileRepository(db.creatorProfileDao())

    val isOnboardingCompleted: StateFlow<Boolean> = prefsRepo.isOnboardingCompleted
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    private val _currentStep = MutableStateFlow(0)
    val currentStep: StateFlow<Int> = _currentStep.asStateFlow()

    private val _channelNameInput = MutableStateFlow("")
    val channelNameInput: StateFlow<String> = _channelNameInput.asStateFlow()

    private val _isAskingChannelName = MutableStateFlow(false)
    val isAskingChannelName: StateFlow<Boolean> = _isAskingChannelName.asStateFlow()

    val steps = listOf(
        OnboardingStep(
            title = "Create Better Thumbnails",
            description = "Design high-converting 16:9 gaming thumbnails with battle-tested presets, neon glowing text effects, and custom stickers.",
            iconName = "PhotoLibrary",
            badge = "STEP 1 OF 3"
        ),
        OnboardingStep(
            title = "Generate YouTube Content",
            description = "Let AI generate viral gaming titles, SEO descriptions, trending hashtags, video ideas, and complete script outlines in seconds.",
            iconName = "AutoAwesome",
            badge = "STEP 2 OF 3"
        ),
        OnboardingStep(
            title = "Plan Your Gaming Channel",
            description = "Schedule upcoming video uploads, track recording & editing progress, and monitor your subscriber goals in one dashboard.",
            iconName = "CalendarToday",
            badge = "STEP 3 OF 3"
        )
    )

    fun updateChannelNameInput(name: String) {
        _channelNameInput.value = name
    }

    fun nextStep() {
        if (_currentStep.value < steps.size - 1) {
            _currentStep.value += 1
        } else {
            _isAskingChannelName.value = true
        }
    }

    fun completeOnboarding(onFinished: () -> Unit) {
        viewModelScope.launch {
            val name = _channelNameInput.value.ifBlank { "Gaming Videos Creator" }
            profileRepo.saveProfile(
                CreatorProfile(
                    channelName = name,
                    channelDescription = "Gaming videos, epic challenges and boss fights!",
                    defaultGame = "Stick War Saga",
                    creatorGoal = "Reach 10,000 Subscribers"
                )
            )
            prefsRepo.setOnboardingCompleted(true, name)
            onFinished()
        }
    }
}
