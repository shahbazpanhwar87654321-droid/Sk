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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.AiContentType
import com.example.ui.components.GamingButton
import com.example.ui.components.NeonCard
import com.example.ui.components.SecondaryGamingButton
import com.example.ui.navigation.Screen
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.GamingDarkBackground
import com.example.ui.theme.GamingSurface
import com.example.ui.theme.GamingSurfaceElevated
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonRed
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThumbnailTextGeneratorScreen(
    viewModel: AiViewModel,
    onNavigateBack: () -> Unit,
    onOpenInEditor: (String) -> Unit
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
            title = { Text("Thumbnail Hook Generator", fontWeight = FontWeight.Bold) },
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
                Text(
                    text = "High CTR hooks should be punchy, short (1-4 words) and in ALL CAPS.",
                    fontSize = 13.sp,
                    color = TextSecondary
                )
            }

            item {
                OutlinedTextField(
                    value = uiState.thumbTopic,
                    onValueChange = { viewModel.updateThumbnailTextInput(it) },
                    label = { Text("Video Title or Topic (e.g. Final Boss, 30 Kills, Survival)") },
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
                    text = "Generate Short Hooks",
                    icon = Icons.Default.AutoAwesome,
                    isLoading = uiState.isLoading,
                    onClick = { viewModel.generateThumbnailText() },
                    modifier = Modifier.fillMaxWidth()
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

            if (uiState.generatedThumbnailTexts.isNotEmpty()) {
                item {
                    Text(
                        text = "Punchy Thumbnail Hooks",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = TextPrimary
                    )
                }

                items(uiState.generatedThumbnailTexts) { hook ->
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
                                text = hook,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = NeonRed,
                                modifier = Modifier.weight(1f)
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = { viewModel.copyToClipboard(hook, "Hook") }
                                ) {
                                    Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = NeonCyan)
                                }
                                IconButton(
                                    onClick = {
                                        viewModel.saveAiResult(
                                            type = AiContentType.THUMBNAIL_TEXT,
                                            game = "Gaming",
                                            topic = uiState.thumbTopic,
                                            content = hook
                                        )
                                    }
                                ) {
                                    Icon(Icons.Default.Bookmark, contentDescription = "Save", tint = TextSecondary)
                                }
                                IconButton(
                                    onClick = {
                                        onOpenInEditor(hook)
                                    }
                                ) {
                                    Icon(Icons.Default.Edit, contentDescription = "Use in Editor", tint = NeonCyan)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
