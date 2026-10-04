package com.example.ui.screens.thumbnail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.TemplateCatalog
import com.example.domain.model.ThumbnailTemplate
import com.example.ui.components.GamingButton
import com.example.ui.components.NeonCard
import com.example.ui.components.SectionHeader
import com.example.ui.navigation.Screen
import com.example.ui.theme.BorderNeon
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CyberViolet
import com.example.ui.theme.GamingDarkBackground
import com.example.ui.theme.GamingSurface
import com.example.ui.theme.GamingSurfaceElevated
import com.example.ui.theme.GamingSurfaceVariant
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonGold
import com.example.ui.theme.NeonRed
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun TemplatesScreen(
    onNavigate: (String) -> Unit
) {
    var selectedCategoryId by remember { mutableStateOf("all") }

    val filteredTemplates = remember(selectedCategoryId) {
        if (selectedCategoryId == "all") {
            TemplateCatalog.templates
        } else {
            TemplateCatalog.templates.filter { it.category == selectedCategoryId }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(GamingDarkBackground),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Top Header
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "Thumbnail Maker & Templates",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Black,
                    color = TextPrimary
                )
                Text(
                    text = "Pick a battle-tested gaming preset or start blank",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Blank Template Action
                GamingButton(
                    text = "Start With Blank Canvas (16:9)",
                    icon = Icons.Default.Add,
                    onClick = { onNavigate(Screen.ThumbnailEditor.createRoute()) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("btn_blank_canvas")
                )
            }
        }

        // Category Filter Chips
        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(TemplateCatalog.categories) { category ->
                    val isSelected = category.id == selectedCategoryId
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategoryId = category.id },
                        label = { Text(category.name, fontSize = 12.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = NeonCyan.copy(alpha = 0.2f),
                            selectedLabelColor = NeonCyan,
                            containerColor = GamingSurface,
                            labelColor = TextSecondary
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = if (isSelected) NeonCyan else BorderSubtle,
                            selectedBorderColor = NeonCyan
                        )
                    )
                }
            }
        }

        item {
            SectionHeader(
                title = "Preset Templates (${filteredTemplates.size})",
                subtitle = "Fully editable layers, text effects & backgrounds"
            )
        }

        items(filteredTemplates) { template ->
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                TemplateItemCard(
                    template = template,
                    onClick = {
                        onNavigate(Screen.ThumbnailEditor.createRoute(templateId = template.id))
                    }
                )
            }
        }
    }
}

@Composable
fun TemplateItemCard(
    template: ThumbnailTemplate,
    onClick: () -> Unit
) {
    NeonCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        borderColor = BorderSubtle,
        backgroundColor = GamingSurfaceElevated
    ) {
        Column {
            // 16:9 Visual Preview Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .background(
                        when (template.backgroundGradient) {
                            "FIRE" -> Brush.verticalGradient(listOf(Color(0xFF3D0A0A), Color(0xFFFF4500)))
                            "CYBER" -> Brush.linearGradient(listOf(Color(0xFF09101E), Color(0xFF00F0FF).copy(alpha = 0.4f)))
                            "GOLD" -> Brush.linearGradient(listOf(Color(0xFF261D03), Color(0xFFFFD700).copy(alpha = 0.5f)))
                            "NATURE" -> Brush.verticalGradient(listOf(Color(0xFF05190D), Color(0xFF00FF88).copy(alpha = 0.3f)))
                            else -> Brush.verticalGradient(listOf(Color(0xFF0A0005), Color(0xFFFF1144).copy(alpha = 0.3f)))
                        }
                    )
                    .padding(14.dp),
                contentAlignment = Alignment.Center
            ) {
                // Badge top right
                Surface(
                    modifier = Modifier.align(Alignment.TopEnd),
                    shape = RoundedCornerShape(6.dp),
                    color = Color.Black.copy(alpha = 0.7f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan.copy(alpha = 0.5f))
                ) {
                    Text(
                        text = template.badge,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = NeonCyan,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }

                // Center main hook text preview
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = template.gameTag.uppercase(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = NeonGold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = template.title,
                        fontWeight = FontWeight.Black,
                        fontSize = 22.sp,
                        color = Color.White,
                        maxLines = 1
                    )
                }
            }

            // Bottom Info Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = template.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = TextPrimary
                    )
                    Text(
                        text = template.description,
                        fontSize = 12.sp,
                        color = TextSecondary,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = NeonCyan.copy(alpha = 0.15f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = NeonCyan,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "EDIT",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = NeonCyan
                        )
                    }
                }
            }
        }
    }
}
