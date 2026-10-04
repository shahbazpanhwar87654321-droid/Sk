package com.example.ui.screens.ai

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.AiService
import com.example.ai.GeminiAiService
import com.example.ai.MockAiService
import com.example.data.database.AppDatabase
import com.example.data.repository.AiRepository
import com.example.domain.model.AiContentType
import com.example.domain.model.DescriptionGenRequest
import com.example.domain.model.GeneratedScript
import com.example.domain.model.HashtagGenRequest
import com.example.domain.model.SavedAiResult
import com.example.domain.model.ScriptGenRequest
import com.example.domain.model.ThumbnailTextGenRequest
import com.example.domain.model.TitleGenRequest
import com.example.domain.model.VideoIdeaGenRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

data class AiUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val toastMessage: String? = null,

    // Title Generator state
    val titleGame: String = "Stick War Saga",
    val titleTopic: String = "Final Lunarclops Boss",
    val titleStyle: String = "Boss Fight",
    val generatedTitles: List<String> = emptyList(),

    // Description Generator state
    val descGame: String = "Stick War Saga",
    val descTitle: String = "Stick War Saga FINAL LUNARCLOPS BOSS 😱🔥",
    val descTopic: String = "Defeating the impossible Lunarclops boss with no upgrades",
    val descKeywords: String = "stick war saga, boss fight, hardcore gameplay, walkthrough",
    val generatedDescription: String = "",

    // Hashtag Generator state
    val hashGame: String = "Stick War Saga",
    val hashTopic: String = "Final Boss Challenge",
    val hashTitle: String = "Stick War Final Battle",
    val generatedHashtags: List<String> = emptyList(),

    // Video Idea Generator state
    val ideaGame: String = "Stick War Saga",
    val ideaCategory: String = "Challenge",
    val ideaDifficulty: String = "Impossible",
    val generatedIdeas: List<String> = emptyList(),

    // Thumbnail Text Generator state
    val thumbTopic: String = "Final Boss",
    val generatedThumbnailTexts: List<String> = emptyList(),

    // Video Script Generator state
    val scriptGame: String = "Stick War Saga",
    val scriptTopic: String = "Final Boss Challenge",
    val scriptLength: String = "10 Minutes",
    val generatedScript: String = ""
)

class AiViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val repo = AiRepository(db.aiResultDao())
    private val aiService: AiService = GeminiAiService(fallbackService = MockAiService())
    private val clipboard = application.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    private val _uiState = MutableStateFlow(AiUiState())
    val uiState: StateFlow<AiUiState> = _uiState.asStateFlow()

    // Title methods
    fun updateTitleInputs(game: String, topic: String, style: String) {
        _uiState.value = _uiState.value.copy(titleGame = game, titleTopic = topic, titleStyle = style)
    }

    fun generateTitles() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = aiService.generateTitles(
                TitleGenRequest(
                    game = _uiState.value.titleGame,
                    topic = _uiState.value.titleTopic,
                    style = _uiState.value.titleStyle
                )
            )
            result.onSuccess { titles ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    generatedTitles = titles
                )
            }.onFailure { err ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = err.localizedMessage ?: "Failed to generate titles"
                )
            }
        }
    }

    // Description methods
    fun updateDescriptionInputs(game: String, title: String, topic: String, keywords: String) {
        _uiState.value = _uiState.value.copy(descGame = game, descTitle = title, descTopic = topic, descKeywords = keywords)
    }

    fun updateGeneratedDescription(text: String) {
        _uiState.value = _uiState.value.copy(generatedDescription = text)
    }

    fun generateDescription() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = aiService.generateDescription(
                DescriptionGenRequest(
                    game = _uiState.value.descGame,
                    title = _uiState.value.descTitle,
                    topic = _uiState.value.descTopic,
                    keywords = _uiState.value.descKeywords
                )
            )
            result.onSuccess { desc ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    generatedDescription = desc
                )
            }.onFailure { err ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = err.localizedMessage ?: "Failed to generate description"
                )
            }
        }
    }

    // Hashtags methods
    fun updateHashtagInputs(game: String, topic: String, title: String) {
        _uiState.value = _uiState.value.copy(hashGame = game, hashTopic = topic, hashTitle = title)
    }

    fun generateHashtags() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = aiService.generateHashtags(
                HashtagGenRequest(
                    game = _uiState.value.hashGame,
                    topic = _uiState.value.hashTopic,
                    title = _uiState.value.hashTitle
                )
            )
            result.onSuccess { tags ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    generatedHashtags = tags
                )
            }.onFailure { err ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = err.localizedMessage ?: "Failed to generate hashtags"
                )
            }
        }
    }

    // Video Ideas methods
    fun updateIdeaInputs(game: String, category: String, difficulty: String) {
        _uiState.value = _uiState.value.copy(ideaGame = game, ideaCategory = category, ideaDifficulty = difficulty)
    }

    fun generateVideoIdeas() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = aiService.generateVideoIdeas(
                VideoIdeaGenRequest(
                    game = _uiState.value.ideaGame,
                    category = _uiState.value.ideaCategory,
                    difficulty = _uiState.value.ideaDifficulty
                )
            )
            result.onSuccess { ideas ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    generatedIdeas = ideas
                )
            }.onFailure { err ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = err.localizedMessage ?: "Failed to generate video ideas"
                )
            }
        }
    }

    // Thumbnail Text methods
    fun updateThumbnailTextInput(topic: String) {
        _uiState.value = _uiState.value.copy(thumbTopic = topic)
    }

    fun generateThumbnailText() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = aiService.generateThumbnailText(
                ThumbnailTextGenRequest(topic = _uiState.value.thumbTopic)
            )
            result.onSuccess { texts ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    generatedThumbnailTexts = texts
                )
            }.onFailure { err ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = err.localizedMessage ?: "Failed to generate thumbnail text"
                )
            }
        }
    }

    // Script Generator methods
    fun updateScriptInputs(game: String, topic: String, length: String) {
        _uiState.value = _uiState.value.copy(scriptGame = game, scriptTopic = topic, scriptLength = length)
    }

    fun updateGeneratedScript(text: String) {
        _uiState.value = _uiState.value.copy(generatedScript = text)
    }

    fun generateScript() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = aiService.generateVideoScript(
                ScriptGenRequest(
                    game = _uiState.value.scriptGame,
                    topic = _uiState.value.scriptTopic,
                    length = _uiState.value.scriptLength
                )
            )
            result.onSuccess { script ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    generatedScript = script.toFormattedText()
                )
            }.onFailure { err ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = err.localizedMessage ?: "Failed to generate video script"
                )
            }
        }
    }

    // Common Copy and Save utilities
    fun copyToClipboard(text: String, label: String = "Text") {
        clipboard.setPrimaryClip(ClipData.newPlainText(label, text))
        _uiState.value = _uiState.value.copy(toastMessage = "Copied to clipboard!")
    }

    fun saveAiResult(type: AiContentType, game: String, topic: String, content: String) {
        viewModelScope.launch {
            repo.saveResult(
                SavedAiResult(
                    id = UUID.randomUUID().toString(),
                    type = type,
                    game = game,
                    topic = topic,
                    content = content
                )
            )
            _uiState.value = _uiState.value.copy(toastMessage = "Saved to My Projects!")
        }
    }

    fun clearToast() {
        _uiState.value = _uiState.value.copy(toastMessage = null)
    }
}
