package com.example.ui.screens.ai

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.AiContentType
import com.example.ui.components.GamingButton
import com.example.ui.components.NeonCard
import com.example.ui.components.SecondaryGamingButton
import com.example.ui.components.SectionHeader
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.GamingDarkBackground
import com.example.ui.theme.GamingSurface
import com.example.ui.theme.GamingSurfaceElevated
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonRed
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitleGeneratorScreen(
    viewModel: AiViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    val styles = listOf(
        "Gameplay", "Funny", "Challenge", "Boss Fight", "Tutorial", "Story", "Multiplayer", "Update", "Secrets"
    )

    LaunchedEffect(uiState.toastMessage) {
        uiState.toastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }
    }

    BackHandler { onNavigateBack() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GamingDarkBackground)
    ) {
        TopAppBar(
            title = { Text("AI Title Generator", fontWeight = FontWeight.Bold) },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = GamingSurface)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Inputs
            item {
                OutlinedTextField(
                    value = uiState.titleGame,
                    onValueChange = { viewModel.updateTitleInputs(it, uiState.titleTopic, uiState.titleStyle) },
                    label = { Text("Game Name (e.g. Stick War Saga, PUBG)") },
                    modifier = Modifier.fillMaxWidth().testTag("input_title_game"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonCyan,
                        unfocusedBorderColor = BorderSubtle,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )
            }

            item {
                OutlinedTextField(
                    value = uiState.titleTopic,
                    onValueChange = { viewModel.updateTitleInputs(uiState.titleGame, it, uiState.titleStyle) },
                    label = { Text("Video Topic (e.g. Final Lunarclops Boss)") },
                    modifier = Modifier.fillMaxWidth().testTag("input_title_topic"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonCyan,
                        unfocusedBorderColor = BorderSubtle,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )
            }

            // Video Style selector
            item {
                Text("Video Style:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
                Spacer(modifier = Modifier.height(6.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(styles) { style ->
                        val isSelected = uiState.titleStyle.equals(style, ignoreCase = true)
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.updateTitleInputs(uiState.titleGame, uiState.titleTopic, style) },
                            label = { Text(style, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = NeonCyan.copy(alpha = 0.2f),
                                selectedLabelColor = NeonCyan,
                                containerColor = GamingSurface,
                                labelColor = TextSecondary
                            )
                        )
                    }
                }
            }

            // Generate Button
            item {
                GamingButton(
                    text = "Generate 5 Titles",
                    icon = Icons.Default.AutoAwesome,
                    isLoading = uiState.isLoading,
                    onClick = { viewModel.generateTitles() },
                    modifier = Modifier.fillMaxWidth().testTag("btn_generate_titles")
                )
            }

            // Error display
            if (uiState.errorMessage != null) {
                item {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        color = NeonRed.copy(alpha = 0.15f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, NeonRed)
                    ) {
                        Text(
                            text = uiState.errorMessage ?: "",
                            color = NeonRed,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }

            // Results Section
            if (uiState.generatedTitles.isNotEmpty()) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Title Suggestions (5)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = TextPrimary
                        )
                        Row {
                            SecondaryGamingButton(
                                text = "Copy All",
                                icon = Icons.Default.ContentCopy,
                                onClick = {
                                    val all = uiState.generatedTitles.joinToString("\n")
                                    viewModel.copyToClipboard(all, "All Titles")
                                },
                                modifier = Modifier.height(38.dp)
                            )
                        }
                    }
                }

                items(uiState.generatedTitles) { title ->
                    NeonCard(
                        modifier = Modifier.fillMaxWidth(),
                        borderColor = BorderSubtle,
                        backgroundColor = GamingSurfaceElevated
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = title,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary,
                                modifier = Modifier.weight(1f)
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = { viewModel.copyToClipboard(title, "Title") }
                                ) {
                                    Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = NeonCyan)
                                }
                                IconButton(
                                    onClick = {
                                        viewModel.saveAiResult(
                                            type = AiContentType.TITLE,
                                            game = uiState.titleGame,
                                            topic = uiState.titleTopic,
                                            content = title
                                        )
                                    }
                                ) {
                                    Icon(Icons.Default.Bookmark, contentDescription = "Save", tint = TextSecondary)
                                }
                            }
                        }
                    }
                }

                item {
                    GamingButton(
                        text = "Regenerate",
                        icon = Icons.Default.Refresh,
                        isLoading = uiState.isLoading,
                        onClick = { viewModel.generateTitles() },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
