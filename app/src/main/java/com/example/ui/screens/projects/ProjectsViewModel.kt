package com.example.ui.screens.projects

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.AppDatabase
import com.example.data.repository.AiRepository
import com.example.data.repository.ThumbnailRepository
import com.example.domain.model.AiContentType
import com.example.domain.model.SavedAiResult
import com.example.domain.model.ThumbnailDesign
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

enum class ProjectTab {
    THUMBNAILS,
    AI_CONTENT
}

data class ProjectsUiState(
    val activeTab: ProjectTab = ProjectTab.THUMBNAILS,
    val searchQuery: String = "",
    val renamingProject: ThumbnailDesign? = null,
    val toastMessage: String? = null
)

class ProjectsViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val thumbnailRepo = ThumbnailRepository(db.thumbnailDao())
    private val aiRepo = AiRepository(db.aiResultDao())

    private val _uiState = MutableStateFlow(ProjectsUiState())
    val uiState: StateFlow<ProjectsUiState> = _uiState.asStateFlow()

    val thumbnails: StateFlow<List<ThumbnailDesign>> = combine(
        thumbnailRepo.allProjects,
        _uiState
    ) { projects, state ->
        if (state.searchQuery.isBlank()) {
            projects
        } else {
            projects.filter {
                it.title.contains(state.searchQuery, ignoreCase = true) ||
                        it.game.contains(state.searchQuery, ignoreCase = true)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val aiResults: StateFlow<List<SavedAiResult>> = combine(
        aiRepo.allResults,
        _uiState
    ) { results, state ->
        if (state.searchQuery.isBlank()) {
            results
        } else {
            results.filter {
                it.content.contains(state.searchQuery, ignoreCase = true) ||
                        it.game.contains(state.searchQuery, ignoreCase = true) ||
                        it.topic.contains(state.searchQuery, ignoreCase = true)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setActiveTab(tab: ProjectTab) {
        _uiState.value = _uiState.value.copy(activeTab = tab)
    }

    fun setSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun openRenameDialog(project: ThumbnailDesign) {
        _uiState.value = _uiState.value.copy(renamingProject = project)
    }

    fun closeRenameDialog() {
        _uiState.value = _uiState.value.copy(renamingProject = null)
    }

    fun renameProject(newTitle: String) {
        val project = _uiState.value.renamingProject ?: return
        viewModelScope.launch {
            thumbnailRepo.saveProject(project.copy(title = newTitle))
            _uiState.value = _uiState.value.copy(renamingProject = null, toastMessage = "Renamed!")
        }
    }

    fun duplicateProject(project: ThumbnailDesign) {
        viewModelScope.launch {
            val newId = UUID.randomUUID().toString()
            thumbnailRepo.duplicateProject(project.id, newId, "${project.title} (Copy)")
            _uiState.value = _uiState.value.copy(toastMessage = "Project duplicated!")
        }
    }

    fun deleteProject(projectId: String) {
        viewModelScope.launch {
            thumbnailRepo.deleteProject(projectId)
            _uiState.value = _uiState.value.copy(toastMessage = "Thumbnail project deleted")
        }
    }

    fun deleteAiResult(resultId: String) {
        viewModelScope.launch {
            aiRepo.deleteResult(resultId)
            _uiState.value = _uiState.value.copy(toastMessage = "Saved AI result deleted")
        }
    }

    fun clearToast() {
        _uiState.value = _uiState.value.copy(toastMessage = null)
    }
}
