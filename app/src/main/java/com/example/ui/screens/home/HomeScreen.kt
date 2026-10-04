package com.example.ui.screens.home

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.filled.Title
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.ThumbnailDesign
import com.example.ui.components.AdBannerView
import com.example.ui.components.GamingButton
import com.example.ui.components.NeonCard
import com.example.ui.components.SectionHeader
import com.example.ui.components.StatCard
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
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonRed
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigate: (String) -> Unit
) {
    val recentProjects by viewModel.recentProjects.collectAsState()
    val totalThumbnails by viewModel.totalThumbnails.collectAsState()
    val totalPlans by viewModel.totalPlans.collectAsState()
    val totalAiResults by viewModel.totalAiResults.collectAsState()
    val profile by viewModel.profile.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(GamingDarkBackground),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // 1. Header & Hero Bar
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                GamingSurfaceVariant,
                                GamingDarkBackground
                            )
                        )
                    )
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Gaming Creator Studio",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Black,
                                color = TextPrimary,
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = "Create. Design. Grow.",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = NeonCyan
                            )
                        }

                        // Profile badge / Quick Link
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { onNavigate(Screen.Profile.route) },
                            color = GamingSurfaceElevated,
                            border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(NeonGreen)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = profile.channelName.take(12),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Creator Goal Card
                    NeonCard(
                        modifier = Modifier.fillMaxWidth(),
                        borderColor = NeonCyan.copy(alpha = 0.35f),
                        backgroundColor = GamingSurface
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.TrendingUp,
                                        contentDescription = null,
                                        tint = NeonCyan,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Creator Goal Progress",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = TextPrimary
                                    )
                                }
                                Text(
                                    text = "${profile.currentSubscribers} / ${profile.subscribersGoal} Subs",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NeonCyan
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            val progress = (profile.currentSubscribers.toFloat() / profile.subscribersGoal.coerceAtLeast(1).toFloat()).coerceIn(0f, 1f)
                            LinearProgressIndicator(
                                progress = { progress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = NeonCyan,
                                trackColor = GamingSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = profile.creatorGoal,
                                fontSize = 12.sp,
                                color = TextSecondary,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }

        // 2. Statistics Grid
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    title = "Saved Thumbnails",
                    value = "$totalThumbnails",
                    icon = Icons.Default.PhotoLibrary,
                    accentColor = NeonCyan,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Video Plans",
                    value = "$totalPlans",
                    icon = Icons.Default.PlayArrow,
                    accentColor = CyberViolet,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "AI Results",
                    value = "$totalAiResults",
                    icon = Icons.Default.AutoAwesome,
                    accentColor = NeonGold,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // 3. Quick Action Cards (Prompt requirement)
        item {
            SectionHeader(
                title = "Creator Tools",
                subtitle = "Fast track your video production"
            )
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Primary Action: Create Thumbnail
                NeonCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("action_create_thumbnail"),
                    borderColor = NeonCyan,
                    backgroundColor = GamingSurfaceElevated,
                    onClick = { onNavigate(Screen.Create.route) }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    Brush.linearGradient(
                                        listOf(NeonCyan, CyberViolet)
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                tint = GamingDarkBackground,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Create Thumbnail",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 17.sp,
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = NeonCyan.copy(alpha = 0.2f)
                                ) {
                                    Text(
                                        text = "1280x720",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = NeonCyan,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Text(
                                text = "Design a professional gaming thumbnail with templates & layers",
                                fontSize = 12.sp,
                                color = TextSecondary,
                                maxLines = 2
                            )
                        }
                    }
                }

                // Grid of 5 secondary tools
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    HomeToolCard(
                        title = "AI Title Generator",
                        desc = "Generate engaging YouTube titles",
                        icon = Icons.Default.Title,
                        accentColor = NeonCyan,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(Screen.TitleGenerator.route) }
                    )
                    HomeToolCard(
                        title = "Description Generator",
                        desc = "Create YouTube descriptions",
                        icon = Icons.Default.Description,
                        accentColor = CyberViolet,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(Screen.DescriptionGenerator.route) }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    HomeToolCard(
                        title = "Hashtag Generator",
                        desc = "Find relevant hashtags",
                        icon = Icons.Default.Tag,
                        accentColor = NeonGreen,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(Screen.HashtagGenerator.route) }
                    )
                    HomeToolCard(
                        title = "Video Ideas",
                        desc = "Generate new gaming video ideas",
                        icon = Icons.Default.Lightbulb,
                        accentColor = NeonGold,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigate(Screen.VideoIdeaGenerator.route) }
                    )
                }

                // My Projects Quick Card
                NeonCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = BorderSubtle,
                    backgroundColor = GamingSurface,
                    onClick = { onNavigate(Screen.Projects.route) }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(GamingSurfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Folder,
                                contentDescription = null,
                                tint = NeonCyan,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "My Projects",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = TextPrimary
                            )
                            Text(
                                text = "Continue your saved designs & generated content",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }

        // 4. Recent Projects Section
        item {
            SectionHeader(
                title = "Recent Projects",
                subtitle = "Pick up where you left off",
                actionLabel = "View All",
                onActionClick = { onNavigate(Screen.Projects.route) }
            )
        }

        if (recentProjects.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(GamingSurface)
                        .border(1.dp, BorderSubtle, RoundedCornerShape(14.dp))
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.PhotoLibrary,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No saved projects yet",
                            fontWeight = FontWeight.Bold,
                            color = TextSecondary,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Tap Create Thumbnail to start your first project",
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                    }
                }
            }
        } else {
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(recentProjects.take(6)) { project ->
                        RecentProjectCard(
                            project = project,
                            onClick = {
                                onNavigate(Screen.ThumbnailEditor.createRoute(projectId = project.id))
                            }
                        )
                    }
                }
            }
        }

        // 5. Test Ad Banner
        item {
            Spacer(modifier = Modifier.height(16.dp))
            AdBannerView()
        }
    }
}

@Composable
fun HomeToolCard(
    title: String,
    desc: String,
    icon: ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    NeonCard(
        modifier = modifier.height(115.dp),
        borderColor = BorderSubtle,
        backgroundColor = GamingSurface,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(accentColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            Column {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = desc,
                    fontSize = 11.sp,
                    color = TextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun RecentProjectCard(
    project: ThumbnailDesign,
    onClick: () -> Unit
) {
    NeonCard(
        modifier = Modifier
            .width(220.dp)
            .clickable(onClick = onClick),
        borderColor = BorderSubtle,
        backgroundColor = GamingSurfaceElevated
    ) {
        Column {
            // 16:9 Thumbnail representation
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .background(
                        when (project.backgroundGradient) {
                            "FIRE" -> Brush.verticalGradient(listOf(Color(0xFF3D0A0A), Color(0xFFFF4500)))
                            "CYBER" -> Brush.linearGradient(listOf(Color(0xFF09101E), Color(0xFF00F0FF).copy(alpha = 0.3f)))
                            "GOLD" -> Brush.linearGradient(listOf(Color(0xFF261D03), Color(0xFFFFD700).copy(alpha = 0.4f)))
                            else -> Brush.verticalGradient(listOf(Color(project.backgroundColor), Color(0xFF0F172A)))
                        }
                    )
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                val topTextLayer = project.layers.firstOrNull { it.type == com.example.domain.model.LayerType.TEXT }
                Text(
                    text = topTextLayer?.text ?: project.title,
                    fontWeight = FontWeight.Black,
                    fontSize = 15.sp,
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = project.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = project.game,
                        fontSize = 11.sp,
                        color = NeonCyan
                    )
                    Text(
                        text = "${project.layers.size} layers",
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                }
            }
        }
    }
}
