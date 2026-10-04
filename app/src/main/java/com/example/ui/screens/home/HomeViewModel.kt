package com.example.ui.screens.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.AppDatabase
import com.example.data.repository.AiRepository
import com.example.data.repository.PlannerRepository
import com.example.data.repository.ProfileRepository
import com.example.data.repository.ThumbnailRepository
import com.example.domain.model.CreatorProfile
import com.example.domain.model.ThumbnailDesign
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

data class HomeUiState(
    val recentProjects: List<ThumbnailDesign> = emptyList(),
    val totalThumbnails: Int = 0,
    val totalPlans: Int = 0,
    val totalAiResults: Int = 0,
    val profile: CreatorProfile = CreatorProfile()
)

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val thumbnailRepo = ThumbnailRepository(db.thumbnailDao())
    private val plannerRepo = PlannerRepository(db.videoPlanDao())
    private val aiRepo = AiRepository(db.aiResultDao())
    private val profileRepo = ProfileRepository(db.creatorProfileDao())

    val recentProjects: StateFlow<List<ThumbnailDesign>> = thumbnailRepo.allProjects
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalThumbnails: StateFlow<Int> = thumbnailRepo.projectsCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalPlans: StateFlow<Int> = plannerRepo.plansCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalAiResults: StateFlow<Int> = aiRepo.resultsCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val profile: StateFlow<CreatorProfile> = profileRepo.profile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CreatorProfile())
}
