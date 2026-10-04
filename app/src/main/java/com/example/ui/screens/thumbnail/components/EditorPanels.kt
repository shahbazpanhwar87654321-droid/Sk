package com.example.ui.screens.thumbnail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FormatColorFill
import androidx.compose.material.icons.filled.FormatPaint
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.RotateRight
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.FilterSettings
import com.example.domain.model.LayerType
import com.example.domain.model.ShapeType
import com.example.domain.model.TextEffect
import com.example.domain.model.ThumbnailLayer
import com.example.ui.components.GamingButton
import com.example.ui.components.NeonCard
import com.example.ui.components.SecondaryGamingButton
import com.example.ui.screens.thumbnail.EditorTab
import com.example.ui.theme.BorderNeon
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CyberViolet
import com.example.ui.theme.GamingDarkBackground
import com.example.ui.theme.GamingSurface
import com.example.ui.theme.GamingSurfaceElevated
import com.example.ui.theme.GamingSurfaceVariant
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonGold
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonRed
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun EditorTabBar(
    activeTab: EditorTab,
    onTabSelected: (EditorTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val tabs = EditorTab.entries

    TabRow(
        selectedTabIndex = tabs.indexOf(activeTab),
        modifier = modifier.fillMaxWidth(),
        containerColor = GamingSurface,
        contentColor = NeonCyan,
        indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[tabs.indexOf(activeTab)]),
                color = NeonCyan,
                height = 3.dp
            )
        }
    ) {
        tabs.forEach { tab ->
            Tab(
                selected = activeTab == tab,
                onClick = { onTabSelected(tab) },
                text = {
                    Text(
                        text = tab.name,
                        fontSize = 11.sp,
                        fontWeight = if (activeTab == tab) FontWeight.Bold else FontWeight.Normal,
                        color = if (activeTab == tab) NeonCyan else TextSecondary
                    )
                }
            )
        }
    }
}

@Composable
fun TransformBar(
    selectedLayer: ThumbnailLayer?,
    onMove: (Float, Float) -> Unit,
    onScale: (Float) -> Unit,
    onRotate: (Float) -> Unit,
    onDuplicate: () -> Unit,
    onDelete: () -> Unit,
    onMoveForward: () -> Unit,
    onMoveBackward: () -> Unit,
    onToggleLock: () -> Unit,
    onRemoveBackground: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp),
        color = GamingSurfaceElevated,
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
    ) {
        if (selectedLayer == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Tap any layer on canvas to transform",
                    fontSize = 12.sp,
                    color = TextMuted
                )
            }
        } else {
            LazyRow(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                item {
                    IconButton(onClick = { onScale(1.1f) }) {
                        Icon(Icons.Default.ZoomIn, contentDescription = "Enlarge", tint = NeonCyan)
                    }
                }
                item {
                    IconButton(onClick = { onScale(0.9f) }) {
                        Icon(Icons.Default.ZoomOut, contentDescription = "Shrink", tint = NeonCyan)
                    }
                }
                item {
                    IconButton(onClick = { onRotate(15f) }) {
                        Icon(Icons.Default.RotateRight, contentDescription = "Rotate", tint = NeonCyan)
                    }
                }
                item {
                    IconButton(onClick = onMoveForward) {
                        Icon(Icons.Default.ArrowUpward, contentDescription = "Bring Forward", tint = TextPrimary)
                    }
                }
                item {
                    IconButton(onClick = onMoveBackward) {
                        Icon(Icons.Default.ArrowDownward, contentDescription = "Send Backward", tint = TextPrimary)
                    }
                }
                item {
                    IconButton(onClick = onDuplicate) {
                        Icon(Icons.Default.ContentCopy, contentDescription = "Duplicate", tint = TextPrimary)
                    }
                }
                item {
                    IconButton(onClick = onToggleLock) {
                        Icon(
                            imageVector = if (selectedLayer.isLocked) Icons.Default.Lock else Icons.Default.LockOpen,
                            contentDescription = "Lock",
                            tint = if (selectedLayer.isLocked) NeonGold else TextPrimary
                        )
                    }
                }
                if (selectedLayer.type == LayerType.IMAGE) {
                    item {
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable(onClick = onRemoveBackground),
                            color = CyberViolet.copy(alpha = 0.2f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CyberViolet)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.AutoFixHigh, contentDescription = null, tint = CyberViolet, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Remove BG", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            }
                        }
                    }
                }
                item {
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = NeonRed)
                    }
                }
            }
        }
    }
}

@Composable
fun LayersTabContent(
    layers: List<ThumbnailLayer>,
    selectedLayerId: String?,
    onSelectLayer: (String) -> Unit,
    onToggleLock: (String) -> Unit,
    onToggleVisibility: (String) -> Unit,
    onDeleteLayer: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(layers.sortedByDescending { it.zIndex }) { layer ->
            val isSelected = layer.id == selectedLayerId
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { onSelectLayer(layer.id) },
                color = if (isSelected) GamingSurfaceElevated else GamingSurface,
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isSelected) NeonCyan else BorderSubtle
                )
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = when (layer.type) {
                                LayerType.TEXT -> Icons.Default.TextFields
                                LayerType.IMAGE -> Icons.Default.FormatPaint
                                LayerType.SHAPE -> Icons.Default.FormatColorFill
                                LayerType.STICKER -> Icons.Default.Layers
                            },
                            contentDescription = null,
                            tint = if (isSelected) NeonCyan else TextMuted,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = layer.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = TextPrimary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "Layer #${layer.zIndex} • ${layer.type.name}",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { onToggleVisibility(layer.id) }, modifier = Modifier.size(32.dp)) {
                            Icon(
                                imageVector = if (layer.isVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = "Toggle Visibility",
                                tint = if (layer.isVisible) TextPrimary else TextMuted,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        IconButton(onClick = { onToggleLock(layer.id) }, modifier = Modifier.size(32.dp)) {
                            Icon(
                                imageVector = if (layer.isLocked) Icons.Default.Lock else Icons.Default.LockOpen,
                                contentDescription = "Toggle Lock",
                                tint = if (layer.isLocked) NeonGold else TextMuted,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TextTabContent(
    selectedLayer: ThumbnailLayer?,
    onUpdateText: (String) -> Unit,
    onUpdateSize: (Float) -> Unit,
    onUpdateColor: (Long) -> Unit,
    onUpdateOutline: (Long?, Float) -> Unit,
    onAddText: () -> Unit
) {
    val presetColors = listOf(
        0xFFFFFFFF, 0xFF00F0FF, 0xFFFFD700, 0xFFFF2A55, 0xFF00FF88, 0xFFB026FF, 0xFFFF7A00, 0xFF000000
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            GamingButton(
                text = "Add New Text Layer",
                icon = Icons.Default.Add,
                onClick = onAddText,
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (selectedLayer != null && selectedLayer.type == LayerType.TEXT) {
            item {
                OutlinedTextField(
                    value = selectedLayer.text,
                    onValueChange = onUpdateText,
                    label = { Text("Text Content") },
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
                Text("Font Size: ${selectedLayer.fontSize.toInt()}sp", fontSize = 13.sp, color = TextSecondary)
                Slider(
                    value = selectedLayer.fontSize,
                    onValueChange = onUpdateSize,
                    valueRange = 24f..120f,
                    colors = SliderDefaults.colors(thumbColor = NeonCyan, activeTrackColor = NeonCyan)
                )
            }

            item {
                Text("Text Fill Color", fontSize = 13.sp, color = TextSecondary)
                Spacer(modifier = Modifier.height(6.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(presetColors) { col ->
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(col))
                                .border(
                                    2.dp,
                                    if (selectedLayer.textColor == col) NeonCyan else Color.Transparent,
                                    CircleShape
                                )
                                .clickable { onUpdateColor(col) }
                        )
                    }
                }
            }

            item {
                Text("Text Outline Width: ${selectedLayer.outlineWidth.toInt()}px", fontSize = 13.sp, color = TextSecondary)
                Slider(
                    value = selectedLayer.outlineWidth,
                    onValueChange = { onUpdateOutline(selectedLayer.outlineColor ?: 0xFF000000, it) },
                    valueRange = 0f..14f,
                    colors = SliderDefaults.colors(thumbColor = CyberViolet, activeTrackColor = CyberViolet)
                )
            }
        }
    }
}

@Composable
fun EffectsTabContent(
    selectedLayer: ThumbnailLayer?,
    onApplyEffect: (TextEffect) -> Unit
) {
    val effects = listOf(
        Triple(TextEffect.FIRE, "Fire Effect", 0xFFFF4500),
        Triple(TextEffect.ICE, "Ice Effect", 0xFF80E5FF),
        Triple(TextEffect.ELECTRIC, "Electric Glow", 0xFF00FF88),
        Triple(TextEffect.GOLD, "Gold Metallic", 0xFFFFD700),
        Triple(TextEffect.NEON, "Neon Cyan", 0xFF00F0FF),
        Triple(TextEffect.RED_GLOW, "Red Glow", 0xFFFF2A55),
        Triple(TextEffect.HORROR, "Horror Blood", 0xFF880011),
        Triple(TextEffect.EXPLOSION, "Explosion Burst", 0xFFFF7A00),
        Triple(TextEffect.METALLIC, "Chrome Steel", 0xFFE0E0E0)
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text(
                text = "Apply Gaming Text Styles",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextSecondary
            )
        }

        items(effects) { (effect, label, colorHex) ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onApplyEffect(effect) },
                color = GamingSurface,
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (selectedLayer?.effect == effect) NeonCyan else BorderSubtle
                )
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(Color(colorHex))
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = label,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color(colorHex)
                        )
                    }
                    if (selectedLayer?.effect == effect) {
                        Text("ACTIVE", fontSize = 11.sp, fontWeight = FontWeight.Black, color = NeonCyan)
                    }
                }
            }
        }
    }
}

@Composable
fun StickersTabContent(
    onAddSticker: (String) -> Unit
) {
    val gamingBadges = listOf(
        "VICTORY", "DEFEAT", "30 KILLS!", "VS", "100%", "FIRE 🔥", "SKULL 💀", "LEVEL UP", "NEW UPDATE", "NO WAY!", "INSANE", "1 HP CLUTCH"
    )
    val emojis = listOf(
        "🔥", "😱", "⚡", "👑", "💥", "🎮", "🏆", "💀", "🎯", "🚀", "💣", "⚔️", "🛡️", "💰"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text("Gaming Badges & Callouts", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextPrimary)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                gamingBadges.take(6).forEach { badge ->
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onAddSticker(badge) },
                        color = GamingSurfaceElevated,
                        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
                    ) {
                        Text(
                            text = badge,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                gamingBadges.drop(6).forEach { badge ->
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onAddSticker(badge) },
                        color = GamingSurfaceElevated,
                        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
                    ) {
                        Text(
                            text = badge,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        item {
            Text("Gaming Emojis", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextPrimary)
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(emojis) { emoji ->
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(GamingSurface)
                            .border(1.dp, BorderSubtle, RoundedCornerShape(10.dp))
                            .clickable { onAddSticker(emoji) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = emoji, fontSize = 24.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun ShapesTabContent(
    onAddShape: (ShapeType) -> Unit
) {
    val shapes = listOf(
        Pair(ShapeType.ROUNDED_RECTANGLE, "Rounded Banner"),
        Pair(ShapeType.CIRCLE, "Target Circle"),
        Pair(ShapeType.RECTANGLE, "Accent Bar"),
        Pair(ShapeType.BADGE, "Rating Badge")
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(shapes) { (shape, name) ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { onAddShape(shape) },
                color = GamingSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.FormatColorFill, contentDescription = null, tint = NeonCyan)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = name, fontWeight = FontWeight.Bold, color = TextPrimary)
                }
            }
        }
    }
}

@Composable
fun BackgroundTabContent(
    onSelectBackground: (Long, String?) -> Unit
) {
    val backgrounds = listOf(
        Pair("CYBER", "Cyber Neon City"),
        Pair("FIRE", "Inferno Boss Battle"),
        Pair("GOLD", "Luxury Heist Gold"),
        Pair("NATURE", "Emerald Forest / Minecraft"),
        Pair("VOID", "Dark Void Horror")
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(backgrounds) { (grad, name) ->
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { onSelectBackground(0xFF0F172A, grad) },
                color = GamingSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(
                                when (grad) {
                                    "FIRE" -> NeonRed
                                    "CYBER" -> NeonCyan
                                    "GOLD" -> NeonGold
                                    "NATURE" -> NeonGreen
                                    else -> CyberViolet
                                }
                            )
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = name, fontWeight = FontWeight.Bold, color = TextPrimary)
                }
            }
        }
    }
}

@Composable
fun FiltersTabContent(
    filters: FilterSettings,
    onUpdateFilters: ((FilterSettings) -> FilterSettings) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Brightness: ${(filters.brightness * 100).toInt()}%", fontSize = 13.sp, color = TextSecondary)
            Slider(
                value = filters.brightness,
                onValueChange = { v -> onUpdateFilters { it.copy(brightness = v) } },
                valueRange = 0.5f..1.5f,
                colors = SliderDefaults.colors(thumbColor = NeonCyan, activeTrackColor = NeonCyan)
            )
        }

        item {
            Text("Contrast: ${(filters.contrast * 100).toInt()}%", fontSize = 13.sp, color = TextSecondary)
            Slider(
                value = filters.contrast,
                onValueChange = { v -> onUpdateFilters { it.copy(contrast = v) } },
                valueRange = 0.5f..1.5f,
                colors = SliderDefaults.colors(thumbColor = CyberViolet, activeTrackColor = CyberViolet)
            )
        }

        item {
            Text("Saturation: ${(filters.saturation * 100).toInt()}%", fontSize = 13.sp, color = TextSecondary)
            Slider(
                value = filters.saturation,
                onValueChange = { v -> onUpdateFilters { it.copy(saturation = v) } },
                valueRange = 0.0f..2.0f,
                colors = SliderDefaults.colors(thumbColor = NeonGold, activeTrackColor = NeonGold)
            )
        }
    }
}
