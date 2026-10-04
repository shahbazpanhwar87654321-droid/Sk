package com.example.ui.screens.projects

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DriveFileRenameOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.SavedAiResult
import com.example.domain.model.ThumbnailDesign
import com.example.ui.components.NeonCard
import com.example.ui.components.SecondaryGamingButton
import com.example.ui.navigation.Screen
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CyberViolet
import com.example.ui.theme.GamingDarkBackground
import com.example.ui.theme.GamingSurface
import com.example.ui.theme.GamingSurfaceElevated
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonGold
import com.example.ui.theme.NeonRed
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ProjectsScreen(
    viewModel: ProjectsViewModel,
    onNavigate: (String) -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val thumbnails by viewModel.thumbnails.collectAsState()
    val aiResults by viewModel.aiResults.collectAsState()
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    val dateFormat = remember { SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()) }

    LaunchedEffect(uiState.toastMessage) {
        uiState.toastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GamingDarkBackground)
    ) {
        // Header
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
            Text(
                text = "My Projects",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Black,
                color = TextPrimary
            )
            Text(
                text = "Saved thumbnails, titles, scripts, and hashtags",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = { viewModel.setSearchQuery(it) },
                placeholder = { Text("Search saved designs & AI content...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextMuted) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonCyan,
                    unfocusedBorderColor = BorderSubtle,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                )
            )
        }

        // Tabs
        TabRow(
            selectedTabIndex = if (uiState.activeTab == ProjectTab.THUMBNAILS) 0 else 1,
            containerColor = GamingSurface,
            contentColor = NeonCyan,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(
                        tabPositions[if (uiState.activeTab == ProjectTab.THUMBNAILS) 0 else 1]
                    ),
                    color = NeonCyan,
                    height = 3.dp
                )
            }
        ) {
            Tab(
                selected = uiState.activeTab == ProjectTab.THUMBNAILS,
                onClick = { viewModel.setActiveTab(ProjectTab.THUMBNAILS) },
                text = {
                    Text(
                        text = "Thumbnails (${thumbnails.size})",
                        fontWeight = if (uiState.activeTab == ProjectTab.THUMBNAILS) FontWeight.Bold else FontWeight.Normal,
                        color = if (uiState.activeTab == ProjectTab.THUMBNAILS) NeonCyan else TextSecondary
                    )
                }
            )
            Tab(
                selected = uiState.activeTab == ProjectTab.AI_CONTENT,
                onClick = { viewModel.setActiveTab(ProjectTab.AI_CONTENT) },
                text = {
                    Text(
                        text = "AI Content (${aiResults.size})",
                        fontWeight = if (uiState.activeTab == ProjectTab.AI_CONTENT) FontWeight.Bold else FontWeight.Normal,
                        color = if (uiState.activeTab == ProjectTab.AI_CONTENT) NeonCyan else TextSecondary
                    )
                }
            )
        }

        // List Content
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (uiState.activeTab == ProjectTab.THUMBNAILS) {
                if (thumbnails.isEmpty()) {
                    item {
                        EmptyProjectsView(
                            message = "No saved thumbnails found",
                            subMessage = "Create a new thumbnail design to save it here."
                        )
                    }
                } else {
                    items(thumbnails) { project ->
                        ProjectThumbnailCard(
                            project = project,
                            dateString = dateFormat.format(Date(project.updatedAt)),
                            onOpen = { onNavigate(Screen.ThumbnailEditor.createRoute(projectId = project.id)) },
                            onRename = { viewModel.openRenameDialog(project) },
                            onDuplicate = { viewModel.duplicateProject(project) },
                            onDelete = { viewModel.deleteProject(project.id) }
                        )
                    }
                }
            } else {
                if (aiResults.isEmpty()) {
                    item {
                        EmptyProjectsView(
                            message = "No saved AI results found",
                            subMessage = "Use AI Tools to generate and bookmark titles, scripts & hashtags."
                        )
                    }
                } else {
                    items(aiResults) { result ->
                        SavedAiResultCard(
                            result = result,
                            dateString = dateFormat.format(Date(result.createdAt)),
                            onCopy = {
                                clipboard.setPrimaryClip(ClipData.newPlainText("AI Content", result.content))
                                Toast.makeText(context, "Copied to clipboard!", Toast.LENGTH_SHORT).show()
                            },
                            onDelete = { viewModel.deleteAiResult(result.id) }
                        )
                    }
                }
            }
        }
    }

    // Rename Dialog
    if (uiState.renamingProject != null) {
        val proj = uiState.renamingProject!!
        var titleText by remember { mutableStateOf(proj.title) }

        AlertDialog(
            onDismissRequest = { viewModel.closeRenameDialog() },
            title = { Text("Rename Project", fontWeight = FontWeight.Bold) },
            text = {
                OutlinedTextField(
                    value = titleText,
                    onValueChange = { titleText = it },
                    label = { Text("Project Title") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonCyan,
                        unfocusedBorderColor = BorderSubtle,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (titleText.isNotBlank()) {
                            viewModel.renameProject(titleText)
                        }
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.closeRenameDialog() }) {
                    Text("Cancel")
                }
            },
            containerColor = GamingSurfaceElevated,
            titleContentColor = TextPrimary,
            textContentColor = TextPrimary
        )
    }
}

@Composable
fun EmptyProjectsView(message: String, subMessage: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.Folder, contentDescription = null, tint = TextMuted, modifier = Modifier.size(48.dp))
            Spacer(modifier = Modifier.height(12.dp))
            Text(message, fontWeight = FontWeight.Bold, color = TextSecondary, fontSize = 15.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(subMessage, color = TextMuted, fontSize = 12.sp)
        }
    }
}

@Composable
fun ProjectThumbnailCard(
    project: ThumbnailDesign,
    dateString: String,
    onOpen: () -> Unit,
    onRename: () -> Unit,
    onDuplicate: () -> Unit,
    onDelete: () -> Unit
) {
    NeonCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = BorderSubtle,
        backgroundColor = GamingSurfaceElevated
    ) {
        Column {
            // Preview
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .background(
                        when (project.backgroundGradient) {
                            "FIRE" -> Brush.verticalGradient(listOf(Color(0xFF3D0A0A), Color(0xFFFF4500)))
                            "CYBER" -> Brush.linearGradient(listOf(Color(0xFF09101E), Color(0xFF00F0FF).copy(alpha = 0.3f)))
                            "GOLD" -> Brush.linearGradient(listOf(Color(0xFF261D03), Color(0xFFFFD700).copy(alpha = 0.4f)))
                            else -> Brush.verticalGradient(listOf(Color(project.backgroundColor), Color(0xFF070B12)))
                        }
                    )
                    .clickable(onClick = onOpen)
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                val topText = project.layers.firstOrNull { it.type == com.example.domain.model.LayerType.TEXT }?.text ?: project.title
                Text(
                    text = topText,
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Info & Actions
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = project.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = TextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${project.game} • $dateString",
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onOpen, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit", tint = NeonCyan, modifier = Modifier.size(18.dp))
                    }
                    IconButton(onClick = onRename, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.DriveFileRenameOutline, contentDescription = "Rename", tint = TextSecondary, modifier = Modifier.size(18.dp))
                    }
                    IconButton(onClick = onDuplicate, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.ContentCopy, contentDescription = "Duplicate", tint = TextSecondary, modifier = Modifier.size(18.dp))
                    }
                    IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = NeonRed, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun SavedAiResultCard(
    result: SavedAiResult,
    dateString: String,
    onCopy: () -> Unit,
    onDelete: () -> Unit
) {
    NeonCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = BorderSubtle,
        backgroundColor = GamingSurfaceElevated
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = NeonCyan.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = result.type.name.replace("_", " "),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonCyan,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Text(
                    text = dateString,
                    fontSize = 11.sp,
                    color = TextMuted
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${result.game} • ${result.topic}",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = NeonGold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = result.content,
                fontSize = 13.sp,
                color = TextPrimary,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                SecondaryGamingButton(
                    text = "Copy",
                    icon = Icons.Default.ContentCopy,
                    onClick = onCopy,
                    modifier = Modifier.height(34.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = onDelete, modifier = Modifier.size(34.dp)) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = NeonRed, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}
