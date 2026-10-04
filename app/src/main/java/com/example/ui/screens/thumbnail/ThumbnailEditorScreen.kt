package com.example.ui.screens.thumbnail

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.export.ImageExporter
import com.example.ui.components.GamingButton
import com.example.ui.screens.thumbnail.components.BackgroundTabContent
import com.example.ui.screens.thumbnail.components.EditorTabBar
import com.example.ui.screens.thumbnail.components.EffectsTabContent
import com.example.ui.screens.thumbnail.components.FiltersTabContent
import com.example.ui.screens.thumbnail.components.LayersTabContent
import com.example.ui.screens.thumbnail.components.ShapesTabContent
import com.example.ui.screens.thumbnail.components.StickersTabContent
import com.example.ui.screens.thumbnail.components.TextTabContent
import com.example.ui.screens.thumbnail.components.ThumbnailCanvas
import com.example.ui.screens.thumbnail.components.TransformBar
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CyberViolet
import com.example.ui.theme.GamingDarkBackground
import com.example.ui.theme.GamingSurface
import com.example.ui.theme.GamingSurfaceElevated
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThumbnailEditorScreen(
    viewModel: ThumbnailViewModel,
    projectId: String?,
    templateId: String?,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    var showExportDialog by remember { mutableStateOf(false) }
    var selectedFormat by remember { mutableStateOf(ImageExporter.ExportFormat.PNG) }
    var highQuality by remember { mutableStateOf(true) }

    // Android Photo Picker launcher
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.addImageLayer(uri)
        }
    }

    LaunchedEffect(projectId, templateId) {
        viewModel.initEditor(projectId, templateId)
    }

    LaunchedEffect(uiState.toastMessage) {
        uiState.toastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }
    }

    BackHandler {
        onNavigateBack()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GamingDarkBackground)
    ) {
        // 1. Top App Bar
        TopAppBar(
            title = {
                Text(
                    text = uiState.design.title.take(20),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TextPrimary
                    )
                }
            },
            actions = {
                IconButton(
                    onClick = { viewModel.undo() },
                    enabled = uiState.canUndo
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Undo,
                        contentDescription = "Undo",
                        tint = if (uiState.canUndo) NeonCyan else TextMuted
                    )
                }
                IconButton(
                    onClick = { viewModel.redo() },
                    enabled = uiState.canRedo
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Redo,
                        contentDescription = "Redo",
                        tint = if (uiState.canRedo) NeonCyan else TextMuted
                    )
                }
                IconButton(
                    onClick = {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.AddPhotoAlternate,
                        contentDescription = "Add Photo",
                        tint = NeonCyan
                    )
                }
                IconButton(onClick = { viewModel.saveProject() }) {
                    if (uiState.isSaving) {
                        CircularProgressIndicator(modifier = Modifier.size(18.dp), color = NeonCyan)
                    } else {
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = "Save Project",
                            tint = NeonCyan
                        )
                    }
                }
                IconButton(onClick = { showExportDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = "Export Thumbnail",
                        tint = CyberViolet
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = GamingSurface)
        )

        // 2. Center Thumbnail Canvas
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            ThumbnailCanvas(
                design = uiState.design,
                selectedLayerId = uiState.selectedLayerId,
                onSelectLayer = { viewModel.selectLayer(it) },
                onMoveLayer = { dx, dy -> viewModel.moveSelectedLayerBy(dx, dy) },
                onScaleLayer = { s -> viewModel.scaleSelectedLayer(s) },
                onRotateLayer = { r -> viewModel.rotateSelectedLayer(r) }
            )
        }

        // 3. Transform Bar for the selected layer
        val selectedLayer = uiState.design.layers.firstOrNull { it.id == uiState.selectedLayerId }
        TransformBar(
            selectedLayer = selectedLayer,
            onMove = { dx, dy -> viewModel.moveSelectedLayerBy(dx, dy) },
            onScale = { s -> viewModel.scaleSelectedLayer(s) },
            onRotate = { r -> viewModel.rotateSelectedLayer(r) },
            onDuplicate = { viewModel.duplicateSelectedLayer() },
            onDelete = { viewModel.deleteSelectedLayer() },
            onMoveForward = { viewModel.moveLayerForward() },
            onMoveBackward = { viewModel.moveLayerBackward() },
            onToggleLock = { viewModel.toggleLockSelectedLayer() },
            onRemoveBackground = { viewModel.removeBackgroundForSelectedImage() }
        )

        // 4. Editor Tab Bar
        EditorTabBar(
            activeTab = uiState.activeTab,
            onTabSelected = { viewModel.setActiveTab(it) }
        )

        // 5. Active Tab Content
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(GamingDarkBackground)
        ) {
            when (uiState.activeTab) {
                EditorTab.LAYERS -> {
                    LayersTabContent(
                        layers = uiState.design.layers,
                        selectedLayerId = uiState.selectedLayerId,
                        onSelectLayer = { viewModel.selectLayer(it) },
                        onToggleLock = { id ->
                            viewModel.selectLayer(id)
                            viewModel.toggleLockSelectedLayer()
                        },
                        onToggleVisibility = { id ->
                            viewModel.selectLayer(id)
                            viewModel.toggleVisibilitySelectedLayer()
                        },
                        onDeleteLayer = { id ->
                            viewModel.selectLayer(id)
                            viewModel.deleteSelectedLayer()
                        }
                    )
                }
                EditorTab.TEXT -> {
                    TextTabContent(
                        selectedLayer = selectedLayer,
                        onUpdateText = { t -> viewModel.updateSelectedLayer { it.copy(text = t) } },
                        onUpdateSize = { s -> viewModel.updateSelectedLayer { it.copy(fontSize = s) } },
                        onUpdateColor = { c -> viewModel.updateSelectedLayer { it.copy(textColor = c) } },
                        onUpdateOutline = { c, w -> viewModel.updateSelectedLayer { it.copy(outlineColor = c, outlineWidth = w) } },
                        onAddText = { viewModel.addTextLayer() }
                    )
                }
                EditorTab.EFFECTS -> {
                    EffectsTabContent(
                        selectedLayer = selectedLayer,
                        onApplyEffect = { eff -> viewModel.updateSelectedLayer { it.copy(effect = eff) } }
                    )
                }
                EditorTab.STICKERS -> {
                    StickersTabContent(
                        onAddSticker = { st -> viewModel.addStickerLayer(st) }
                    )
                }
                EditorTab.SHAPES -> {
                    ShapesTabContent(
                        onAddShape = { sh -> viewModel.addShapeLayer(sh) }
                    )
                }
                EditorTab.BACKGROUND -> {
                    BackgroundTabContent(
                        onSelectBackground = { col, grad -> viewModel.updateBackground(col, grad) }
                    )
                }
                EditorTab.FILTERS -> {
                    FiltersTabContent(
                        filters = uiState.design.filterSettings,
                        onUpdateFilters = { upd -> viewModel.updateFilters(upd) }
                    )
                }
            }
        }
    }

    // Export Dialog
    if (showExportDialog) {
        AlertDialog(
            onDismissRequest = { showExportDialog = false },
            title = { Text("Export Gaming Thumbnail", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Select Format:", fontSize = 13.sp, color = TextSecondary)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = selectedFormat == ImageExporter.ExportFormat.PNG,
                            onClick = { selectedFormat = ImageExporter.ExportFormat.PNG },
                            colors = RadioButtonDefaults.colors(selectedColor = NeonCyan)
                        )
                        Text("PNG (High Quality Lossless)", fontSize = 14.sp)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = selectedFormat == ImageExporter.ExportFormat.JPG,
                            onClick = { selectedFormat = ImageExporter.ExportFormat.JPG },
                            colors = RadioButtonDefaults.colors(selectedColor = NeonCyan)
                        )
                        Text("JPG (Smaller file size)", fontSize = 14.sp)
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Resolution:", fontSize = 13.sp, color = TextSecondary)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = highQuality,
                            onClick = { highQuality = true },
                            colors = RadioButtonDefaults.colors(selectedColor = NeonCyan)
                        )
                        Text("1080p Full HD (1920 × 1080)", fontSize = 14.sp)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = !highQuality,
                            onClick = { highQuality = false },
                            colors = RadioButtonDefaults.colors(selectedColor = NeonCyan)
                        )
                        Text("720p Standard HD (1280 × 720)", fontSize = 14.sp)
                    }

                    if (uiState.isExporting) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), color = NeonCyan)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("Rendering high resolution...", fontSize = 13.sp)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.exportThumbnail(
                            format = selectedFormat,
                            highQuality = highQuality
                        ) { uri ->
                            showExportDialog = false
                            // Trigger system share sheet
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = selectedFormat.mimeType
                                putExtra(Intent.EXTRA_STREAM, uri)
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Share Thumbnail"))
                        }
                    },
                    enabled = !uiState.isExporting,
                    modifier = Modifier.testTag("export_confirm_button")
                ) {
                    Text("Export & Share")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showExportDialog = false },
                    enabled = !uiState.isExporting
                ) {
                    Text("Cancel")
                }
            },
            containerColor = GamingSurfaceElevated,
            textContentColor = TextPrimary,
            titleContentColor = TextPrimary
        )
    }
}
