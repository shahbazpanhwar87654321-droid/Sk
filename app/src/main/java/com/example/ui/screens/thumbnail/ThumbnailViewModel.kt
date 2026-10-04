package com.example.ui.screens.thumbnail

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.MockAiService
import com.example.data.database.AppDatabase
import com.example.data.repository.ThumbnailRepository
import com.example.domain.model.FilterSettings
import com.example.domain.model.LayerType
import com.example.domain.model.ShapeType
import com.example.domain.model.TemplateCatalog
import com.example.domain.model.TextEffect
import com.example.domain.model.ThumbnailDesign
import com.example.domain.model.ThumbnailLayer
import com.example.export.ImageExporter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

data class EditorUiState(
    val design: ThumbnailDesign = ThumbnailDesign(
        id = UUID.randomUUID().toString(),
        title = "New Gaming Thumbnail"
    ),
    val selectedLayerId: String? = null,
    val canUndo: Boolean = false,
    val canRedo: Boolean = false,
    val isSaving: Boolean = false,
    val isExporting: Boolean = false,
    val exportedUri: Uri? = null,
    val toastMessage: String? = null,
    val activeTab: EditorTab = EditorTab.LAYERS
)

enum class EditorTab {
    LAYERS,
    TEXT,
    EFFECTS,
    STICKERS,
    SHAPES,
    BACKGROUND,
    FILTERS
}

class ThumbnailViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val repo = ThumbnailRepository(db.thumbnailDao())
    private val aiService = MockAiService()

    private val _uiState = MutableStateFlow(EditorUiState())
    val uiState: StateFlow<EditorUiState> = _uiState.asStateFlow()

    private val undoStack = mutableListOf<ThumbnailDesign>()
    private val redoStack = mutableListOf<ThumbnailDesign>()

    fun initEditor(projectId: String?, templateId: String?) {
        viewModelScope.launch {
            if (!projectId.isNullOrBlank()) {
                val existing = repo.getProjectById(projectId)
                if (existing != null) {
                    _uiState.value = _uiState.value.copy(
                        design = existing,
                        selectedLayerId = existing.layers.firstOrNull()?.id
                    )
                    return@launch
                }
            }

            if (!templateId.isNullOrBlank()) {
                val tpl = TemplateCatalog.templates.firstOrNull { it.id == templateId }
                if (tpl != null) {
                    val design = ThumbnailDesign(
                        id = UUID.randomUUID().toString(),
                        title = tpl.title,
                        game = tpl.gameTag,
                        layers = tpl.layers,
                        backgroundColor = tpl.backgroundColor,
                        backgroundGradient = tpl.backgroundGradient
                    )
                    _uiState.value = _uiState.value.copy(
                        design = design,
                        selectedLayerId = design.layers.firstOrNull()?.id
                    )
                    return@launch
                }
            }

            // Default new design with starter text
            if (_uiState.value.design.layers.isEmpty()) {
                val starterLayer = ThumbnailLayer(
                    id = UUID.randomUUID().toString(),
                    type = LayerType.TEXT,
                    name = "Gaming Title",
                    text = "INSANE GAMEPLAY!",
                    x = 640f,
                    y = 360f,
                    fontSize = 72f,
                    textColor = 0xFF00F0FF,
                    outlineColor = 0xFF000000,
                    outlineWidth = 6f,
                    effect = TextEffect.NEON,
                    zIndex = 1
                )
                val starter = _uiState.value.design.copy(layers = listOf(starterLayer))
                _uiState.value = _uiState.value.copy(
                    design = starter,
                    selectedLayerId = starterLayer.id
                )
            }
        }
    }

    private fun pushUndo() {
        undoStack.add(_uiState.value.design)
        if (undoStack.size > 20) undoStack.removeAt(0)
        redoStack.clear()
        updateUndoRedoStatus()
    }

    private fun updateUndoRedoStatus() {
        _uiState.value = _uiState.value.copy(
            canUndo = undoStack.isNotEmpty(),
            canRedo = redoStack.isNotEmpty()
        )
    }

    fun undo() {
        if (undoStack.isNotEmpty()) {
            val previous = undoStack.removeAt(undoStack.size - 1)
            redoStack.add(_uiState.value.design)
            _uiState.value = _uiState.value.copy(design = previous)
            updateUndoRedoStatus()
        }
    }

    fun redo() {
        if (redoStack.isNotEmpty()) {
            val next = redoStack.removeAt(redoStack.size - 1)
            undoStack.add(_uiState.value.design)
            _uiState.value = _uiState.value.copy(design = next)
            updateUndoRedoStatus()
        }
    }

    fun selectLayer(layerId: String?) {
        _uiState.value = _uiState.value.copy(selectedLayerId = layerId)
    }

    fun setActiveTab(tab: EditorTab) {
        _uiState.value = _uiState.value.copy(activeTab = tab)
    }

    fun updateProjectTitle(title: String) {
        _uiState.value = _uiState.value.copy(
            design = _uiState.value.design.copy(title = title)
        )
    }

    fun updateProjectGame(game: String) {
        _uiState.value = _uiState.value.copy(
            design = _uiState.value.design.copy(game = game)
        )
    }

    fun addTextLayer(text: String = "NEW TEXT", effect: TextEffect = TextEffect.NEON) {
        pushUndo()
        val currentMaxZ = _uiState.value.design.layers.maxOfOrNull { it.zIndex } ?: 0
        val newLayer = ThumbnailLayer(
            id = UUID.randomUUID().toString(),
            type = LayerType.TEXT,
            name = "Text: ${text.take(10)}",
            text = text,
            x = 640f,
            y = 360f,
            fontSize = 64f,
            textColor = 0xFFFFFFFF,
            outlineColor = 0xFF000000,
            outlineWidth = 5f,
            effect = effect,
            zIndex = currentMaxZ + 1
        )
        val updated = _uiState.value.design.copy(
            layers = _uiState.value.design.layers + newLayer
        )
        _uiState.value = _uiState.value.copy(
            design = updated,
            selectedLayerId = newLayer.id,
            activeTab = EditorTab.TEXT
        )
    }

    fun addImageLayer(uri: Uri) {
        pushUndo()
        val currentMaxZ = _uiState.value.design.layers.maxOfOrNull { it.zIndex } ?: 0
        val newLayer = ThumbnailLayer(
            id = UUID.randomUUID().toString(),
            type = LayerType.IMAGE,
            name = "Image Layer",
            imageUri = uri.toString(),
            x = 640f,
            y = 360f,
            width = 400f,
            height = 400f,
            zIndex = currentMaxZ + 1
        )
        val updated = _uiState.value.design.copy(
            layers = _uiState.value.design.layers + newLayer
        )
        _uiState.value = _uiState.value.copy(
            design = updated,
            selectedLayerId = newLayer.id,
            activeTab = EditorTab.LAYERS
        )
    }

    fun addShapeLayer(type: ShapeType, color: Long = 0xFF00E5FF) {
        pushUndo()
        val currentMaxZ = _uiState.value.design.layers.maxOfOrNull { it.zIndex } ?: 0
        val newLayer = ThumbnailLayer(
            id = UUID.randomUUID().toString(),
            type = LayerType.SHAPE,
            name = "Shape ${type.name}",
            shapeType = type,
            fillColor = color,
            strokeColor = 0xFFFFFFFF,
            strokeWidth = 3f,
            x = 640f,
            y = 360f,
            width = 320f,
            height = 180f,
            zIndex = currentMaxZ + 1
        )
        val updated = _uiState.value.design.copy(
            layers = _uiState.value.design.layers + newLayer
        )
        _uiState.value = _uiState.value.copy(
            design = updated,
            selectedLayerId = newLayer.id,
            activeTab = EditorTab.SHAPES
        )
    }

    fun addStickerLayer(stickerText: String, fillColor: Long = 0xFFFF2A55) {
        pushUndo()
        val currentMaxZ = _uiState.value.design.layers.maxOfOrNull { it.zIndex } ?: 0
        val newLayer = ThumbnailLayer(
            id = UUID.randomUUID().toString(),
            type = LayerType.STICKER,
            name = "Sticker: $stickerText",
            stickerText = stickerText,
            fillColor = fillColor,
            strokeColor = 0xFFFFFFFF,
            textColor = 0xFFFFFFFF,
            x = 640f,
            y = 520f,
            width = 380f,
            height = 76f,
            zIndex = currentMaxZ + 1
        )
        val updated = _uiState.value.design.copy(
            layers = _uiState.value.design.layers + newLayer
        )
        _uiState.value = _uiState.value.copy(
            design = updated,
            selectedLayerId = newLayer.id,
            activeTab = EditorTab.STICKERS
        )
    }

    fun updateSelectedLayer(updater: (ThumbnailLayer) -> ThumbnailLayer) {
        val selectedId = _uiState.value.selectedLayerId ?: return
        val currentLayers = _uiState.value.design.layers
        val updated = currentLayers.map { layer ->
            if (layer.id == selectedId) {
                if (layer.isLocked) layer else updater(layer)
            } else {
                layer
            }
        }
        _uiState.value = _uiState.value.copy(
            design = _uiState.value.design.copy(layers = updated)
        )
    }

    fun moveSelectedLayerBy(dx: Float, dy: Float) {
        updateSelectedLayer { layer ->
            layer.copy(
                x = (layer.x + dx).coerceIn(0f, 1280f),
                y = (layer.y + dy).coerceIn(0f, 720f)
            )
        }
    }

    fun scaleSelectedLayer(scaleFactor: Float) {
        updateSelectedLayer { layer ->
            layer.copy(scale = (layer.scale * scaleFactor).coerceIn(0.2f, 4.0f))
        }
    }

    fun rotateSelectedLayer(degreesDelta: Float) {
        updateSelectedLayer { layer ->
            layer.copy(rotation = (layer.rotation + degreesDelta) % 360f)
        }
    }

    fun deleteSelectedLayer() {
        val selectedId = _uiState.value.selectedLayerId ?: return
        pushUndo()
        val updatedLayers = _uiState.value.design.layers.filter { it.id != selectedId }
        _uiState.value = _uiState.value.copy(
            design = _uiState.value.design.copy(layers = updatedLayers),
            selectedLayerId = updatedLayers.lastOrNull()?.id
        )
    }

    fun duplicateSelectedLayer() {
        val selectedId = _uiState.value.selectedLayerId ?: return
        val current = _uiState.value.design.layers.firstOrNull { it.id == selectedId } ?: return
        pushUndo()
        val currentMaxZ = _uiState.value.design.layers.maxOfOrNull { it.zIndex } ?: 0
        val duplicated = current.copy(
            id = UUID.randomUUID().toString(),
            name = "${current.name} (Copy)",
            x = (current.x + 30f).coerceAtMost(1240f),
            y = (current.y + 30f).coerceAtMost(680f),
            zIndex = currentMaxZ + 1
        )
        _uiState.value = _uiState.value.copy(
            design = _uiState.value.design.copy(layers = _uiState.value.design.layers + duplicated),
            selectedLayerId = duplicated.id
        )
    }

    fun moveLayerForward() {
        val selectedId = _uiState.value.selectedLayerId ?: return
        pushUndo()
        val layers = _uiState.value.design.layers.sortedBy { it.zIndex }.toMutableList()
        val index = layers.indexOfFirst { it.id == selectedId }
        if (index in 0 until layers.size - 1) {
            val current = layers[index]
            val next = layers[index + 1]
            layers[index] = current.copy(zIndex = next.zIndex)
            layers[index + 1] = next.copy(zIndex = current.zIndex)
            _uiState.value = _uiState.value.copy(
                design = _uiState.value.design.copy(layers = layers)
            )
        }
    }

    fun moveLayerBackward() {
        val selectedId = _uiState.value.selectedLayerId ?: return
        pushUndo()
        val layers = _uiState.value.design.layers.sortedBy { it.zIndex }.toMutableList()
        val index = layers.indexOfFirst { it.id == selectedId }
        if (index > 0) {
            val current = layers[index]
            val prev = layers[index - 1]
            layers[index] = current.copy(zIndex = prev.zIndex)
            layers[index - 1] = prev.copy(zIndex = current.zIndex)
            _uiState.value = _uiState.value.copy(
                design = _uiState.value.design.copy(layers = layers)
            )
        }
    }

    fun toggleLockSelectedLayer() {
        updateSelectedLayer { it.copy(isLocked = !it.isLocked) }
    }

    fun toggleVisibilitySelectedLayer() {
        updateSelectedLayer { it.copy(isVisible = !it.isVisible) }
    }

    fun updateFilters(updater: (FilterSettings) -> FilterSettings) {
        val currentFilters = _uiState.value.design.filterSettings
        _uiState.value = _uiState.value.copy(
            design = _uiState.value.design.copy(filterSettings = updater(currentFilters))
        )
    }

    fun updateBackground(color: Long, gradient: String? = null) {
        pushUndo()
        _uiState.value = _uiState.value.copy(
            design = _uiState.value.design.copy(
                backgroundColor = color,
                backgroundGradient = gradient
            )
        )
    }

    fun removeBackgroundForSelectedImage() {
        val selected = _uiState.value.design.layers.firstOrNull { it.id == _uiState.value.selectedLayerId }
        if (selected == null || selected.type != LayerType.IMAGE || selected.imageUri == null) {
            _uiState.value = _uiState.value.copy(toastMessage = "Please select an image layer first")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true)
            val result = aiService.removeBackground(selected.imageUri)
            _uiState.value = _uiState.value.copy(isSaving = false)
            if (result.isSuccess) {
                updateSelectedLayer { it.copy(isCutout = true) }
                _uiState.value = _uiState.value.copy(toastMessage = "Background removed! Cutout effect applied.")
            } else {
                _uiState.value = _uiState.value.copy(toastMessage = "Background removal API not configured.")
            }
        }
    }

    fun saveProject(onDone: (() -> Unit)? = null) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true)
            repo.saveProject(_uiState.value.design)
            _uiState.value = _uiState.value.copy(
                isSaving = false,
                toastMessage = "Project saved successfully!"
            )
            onDone?.invoke()
        }
    }

    fun exportThumbnail(
        format: ImageExporter.ExportFormat = ImageExporter.ExportFormat.PNG,
        highQuality: Boolean = true,
        onExportComplete: (Uri) -> Unit
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isExporting = true)
            val width = if (highQuality) 1920 else 1280
            val height = if (highQuality) 1080 else 720
            val result = ImageExporter.renderAndExport(
                context = getApplication(),
                design = _uiState.value.design,
                width = width,
                height = height,
                format = format,
                quality = 100
            )
            _uiState.value = _uiState.value.copy(isExporting = false)
            result.onSuccess { uri ->
                _uiState.value = _uiState.value.copy(
                    exportedUri = uri,
                    toastMessage = "Exported to Pictures/GamingCreatorStudio!"
                )
                onExportComplete(uri)
            }.onFailure { err ->
                _uiState.value = _uiState.value.copy(
                    toastMessage = "Export failed: ${err.localizedMessage ?: "Unknown error"}"
                )
            }
        }
    }

    fun clearToast() {
        _uiState.value = _uiState.value.copy(toastMessage = null)
    }
}
