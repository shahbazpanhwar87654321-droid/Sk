package com.example.ui.screens.planner

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.AppDatabase
import com.example.data.repository.PlannerRepository
import com.example.domain.model.VideoPlan
import com.example.domain.model.VideoStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

data class PlannerUiState(
    val filterStatus: VideoStatus? = null,
    val searchQuery: String = "",
    val editingPlan: VideoPlan? = null,
    val isAddDialogOpen: Boolean = false,
    val toastMessage: String? = null
)

class PlannerViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val repo = PlannerRepository(db.videoPlanDao())

    private val _uiState = MutableStateFlow(PlannerUiState())
    val uiState: StateFlow<PlannerUiState> = _uiState.asStateFlow()

    val plans: StateFlow<List<VideoPlan>> = combine(
        repo.allPlans,
        _uiState
    ) { allPlans, state ->
        allPlans.filter { plan ->
            val matchesFilter = state.filterStatus == null || plan.status == state.filterStatus
            val matchesSearch = state.searchQuery.isBlank() ||
                    plan.title.contains(state.searchQuery, ignoreCase = true) ||
                    plan.game.contains(state.searchQuery, ignoreCase = true)
            matchesFilter && matchesSearch
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun setFilterStatus(status: VideoStatus?) {
        _uiState.value = _uiState.value.copy(filterStatus = status)
    }

    fun openAddDialog(plan: VideoPlan? = null) {
        _uiState.value = _uiState.value.copy(
            isAddDialogOpen = true,
            editingPlan = plan
        )
    }

    fun closeDialog() {
        _uiState.value = _uiState.value.copy(
            isAddDialogOpen = false,
            editingPlan = null
        )
    }

    fun savePlan(title: String, game: String, status: VideoStatus, notes: String, plannedDate: Long) {
        viewModelScope.launch {
            val existing = _uiState.value.editingPlan
            val planToSave = existing?.copy(
                title = title,
                game = game,
                status = status,
                notes = notes,
                plannedDate = plannedDate,
                updatedAt = System.currentTimeMillis()
            ) ?: VideoPlan(
                id = UUID.randomUUID().toString(),
                title = title,
                game = game,
                status = status,
                notes = notes,
                plannedDate = plannedDate
            )
            repo.savePlan(planToSave)
            _uiState.value = _uiState.value.copy(
                isAddDialogOpen = false,
                editingPlan = null,
                toastMessage = "Plan saved!"
            )
        }
    }

    fun updateStatus(planId: String, newStatus: VideoStatus) {
        viewModelScope.launch {
            val plan = repo.getPlanById(planId) ?: return@launch
            repo.savePlan(plan.copy(status = newStatus))
        }
    }

    fun deletePlan(planId: String) {
        viewModelScope.launch {
            repo.deletePlan(planId)
            _uiState.value = _uiState.value.copy(toastMessage = "Plan deleted")
        }
    }

    fun clearToast() {
        _uiState.value = _uiState.value.copy(toastMessage = null)
    }
}
