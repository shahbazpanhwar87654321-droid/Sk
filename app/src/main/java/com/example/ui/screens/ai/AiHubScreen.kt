package com.example.ui.screens.ai

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AdBannerView
import com.example.ui.components.NeonCard
import com.example.ui.components.SectionHeader
import com.example.ui.navigation.Screen
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.CyberViolet
import com.example.ui.theme.GamingDarkBackground
import com.example.ui.theme.GamingSurface
import com.example.ui.theme.GamingSurfaceElevated
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonGold
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonIce
import com.example.ui.theme.NeonRed
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun AiHubScreen(
    onNavigate: (String) -> Unit
) {
    val aiTools = listOf(
        AiToolItem(
            title = "Title Generator",
            description = "High-CTR, viral gaming video titles with 9 style categories",
            icon = Icons.Default.Title,
            accentColor = NeonCyan,
            route = Screen.TitleGenerator.route,
            badge = "POPULAR"
        ),
        AiToolItem(
            title = "Description Generator",
            description = "Complete SEO description with hooks, timestamps, chapters & disclaimer",
            icon = Icons.Default.Description,
            accentColor = CyberViolet,
            route = Screen.DescriptionGenerator.route,
            badge = "SEO"
        ),
        AiToolItem(
            title = "Hashtag Generator",
            description = "Trending tags for game, topic, and algorithm discoverability",
            icon = Icons.Default.Tag,
            accentColor = NeonGreen,
            route = Screen.HashtagGenerator.route,
            badge = "DISCOVERY"
        ),
        AiToolItem(
            title = "Video Idea Generator",
            description = "10 unique challenges, speedruns, hardcore and impossible runs",
            icon = Icons.Default.Lightbulb,
            accentColor = NeonGold,
            route = Screen.VideoIdeaGenerator.route,
            badge = "VIRAL"
        ),
        AiToolItem(
            title = "Thumbnail Text Generator",
            description = "Short punchy hooks like 'FINAL BOSS!', 'IMPOSSIBLE!', '30 KILLS!'",
            icon = Icons.Default.TextFields,
            accentColor = NeonRed,
            route = Screen.ThumbnailTextGenerator.route,
            badge = "CTR"
        ),
        AiToolItem(
            title = "Video Script Generator",
            description = "Gaming video script with hooks, gameplay beats, and commentary cues",
            icon = Icons.Default.Movie,
            accentColor = NeonIce,
            route = Screen.VideoScriptGenerator.route,
            badge = "NEW"
        )
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(GamingDarkBackground),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = NeonCyan,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "AI Creator Hub",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Black,
                        color = TextPrimary
                    )
                }
                Text(
                    text = "Automate your channel growth with AI-powered gaming tools",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }
        }

        item {
            SectionHeader(
                title = "Available Generators",
                subtitle = "Choose a tool to begin generating"
            )
        }

        items(aiTools.size) { index ->
            val tool = aiTools[index]
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                NeonCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("ai_tool_card_${tool.title.lowercase().replace(" ", "_")}")
                        .clickable { onNavigate(tool.route) },
                    borderColor = BorderSubtle,
                    backgroundColor = GamingSurface
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(tool.accentColor.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = tool.icon,
                                contentDescription = null,
                                tint = tool.accentColor,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = tool.title,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = tool.accentColor.copy(alpha = 0.2f)
                                ) {
                                    Text(
                                        text = tool.badge,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Black,
                                        color = tool.accentColor,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = tool.description,
                                fontSize = 12.sp,
                                color = TextSecondary,
                                maxLines = 2
                            )
                        }
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(14.dp))
            AdBannerView()
        }
    }
}

data class AiToolItem(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val accentColor: Color,
    val route: String,
    val badge: String
)
