package com.example.ui.screens.ai

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.AiContentType
import com.example.ui.components.GamingButton
import com.example.ui.components.SecondaryGamingButton
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.GamingDarkBackground
import com.example.ui.theme.GamingSurface
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonRed
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DescriptionGeneratorScreen(
    viewModel: AiViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

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
            title = { Text("Description Generator", fontWeight = FontWeight.Bold) },
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
            item {
                OutlinedTextField(
                    value = uiState.descGame,
                    onValueChange = { viewModel.updateDescriptionInputs(it, uiState.descTitle, uiState.descTopic, uiState.descKeywords) },
                    label = { Text("Game Name") },
                    modifier = Modifier.fillMaxWidth().testTag("input_desc_game"),
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
                    value = uiState.descTitle,
                    onValueChange = { viewModel.updateDescriptionInputs(uiState.descGame, it, uiState.descTopic, uiState.descKeywords) },
                    label = { Text("Video Title") },
                    modifier = Modifier.fillMaxWidth().testTag("input_desc_title"),
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
                    value = uiState.descTopic,
                    onValueChange = { viewModel.updateDescriptionInputs(uiState.descGame, uiState.descTitle, it, uiState.descKeywords) },
                    label = { Text("Video Topic & Key Moments") },
                    modifier = Modifier.fillMaxWidth().testTag("input_desc_topic"),
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
                    value = uiState.descKeywords,
                    onValueChange = { viewModel.updateDescriptionInputs(uiState.descGame, uiState.descTitle, uiState.descTopic, it) },
                    label = { Text("Keywords (optional, comma separated)") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonCyan,
                        unfocusedBorderColor = BorderSubtle,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )
            }

            item {
                GamingButton(
                    text = "Generate Description",
                    icon = Icons.Default.AutoAwesome,
                    isLoading = uiState.isLoading,
                    onClick = { viewModel.generateDescription() },
                    modifier = Modifier.fillMaxWidth().testTag("btn_generate_desc")
                )
            }

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

            if (uiState.generatedDescription.isNotBlank()) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Generated Description (Editable)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = TextPrimary
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            SecondaryGamingButton(
                                text = "Copy",
                                icon = Icons.Default.ContentCopy,
                                onClick = {
                                    viewModel.copyToClipboard(uiState.generatedDescription, "Description")
                                },
                                modifier = Modifier.height(38.dp)
                            )
                            SecondaryGamingButton(
                                text = "Save",
                                icon = Icons.Default.Bookmark,
                                onClick = {
                                    viewModel.saveAiResult(
                                        type = AiContentType.DESCRIPTION,
                                        game = uiState.descGame,
                                        topic = uiState.descTopic,
                                        content = uiState.generatedDescription
                                    )
                                },
                                modifier = Modifier.height(38.dp)
                            )
                        }
                    }
                }

                item {
                    OutlinedTextField(
                        value = uiState.generatedDescription,
                        onValueChange = { viewModel.updateGeneratedDescription(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = BorderSubtle,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )
                }
            }
        }
    }
}
