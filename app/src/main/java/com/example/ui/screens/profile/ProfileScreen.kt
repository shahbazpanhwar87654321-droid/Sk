package com.example.ui.screens.profile

import android.widget.Toast
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.HighQuality
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.billing.PlanType
import com.example.ui.components.GamingButton
import com.example.ui.components.NeonCard
import com.example.ui.components.SectionHeader
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
fun ProfileScreen(
    viewModel: ProfileViewModel
) {
    val context = LocalContext.current
    val profile by viewModel.profile.collectAsState()
    val subState by viewModel.subscriptionState.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    var showClearConfirm by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.toastMessage) {
        uiState.toastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(GamingDarkBackground),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // 1. Channel Profile Hero Card
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Creator Profile & Settings",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Black,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(14.dp))

                NeonCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = NeonCyan.copy(alpha = 0.5f),
                    backgroundColor = GamingSurfaceElevated
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(54.dp)
                                        .clip(CircleShape)
                                        .background(
                                            Brush.linearGradient(
                                                listOf(NeonCyan, CyberViolet)
                                            )
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.SportsEsports,
                                        contentDescription = null,
                                        tint = GamingDarkBackground,
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(14.dp))
                                Column {
                                    Text(
                                        text = profile.channelName,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 17.sp,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = profile.defaultGame,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = NeonCyan
                                    )
                                }
                            }

                            IconButton(
                                onClick = { viewModel.setEditingProfile(true) },
                                modifier = Modifier.testTag("btn_edit_profile")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit Profile",
                                    tint = NeonCyan
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = profile.channelDescription,
                            fontSize = 12.sp,
                            color = TextSecondary,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Stats & Goal row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(GamingSurfaceVariant)
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${profile.currentSubscribers}",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 15.sp,
                                    color = TextPrimary
                                )
                                Text("Subscribers", fontSize = 11.sp, color = TextMuted)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${profile.subscribersGoal}",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 15.sp,
                                    color = NeonCyan
                                )
                                Text("Goal", fontSize = 11.sp, color = TextMuted)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = if (subState.isPremium) "PRO" else "FREE",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 15.sp,
                                    color = if (subState.isPremium) NeonGold else TextSecondary
                                )
                                Text("Plan", fontSize = 11.sp, color = TextMuted)
                            }
                        }
                    }
                }
            }
        }

        // 2. Subscription / Upgrade Card
        item {
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                NeonCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = NeonGold.copy(alpha = 0.5f),
                    backgroundColor = GamingSurface
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(NeonGold.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Star, contentDescription = null, tint = NeonGold, modifier = Modifier.size(22.dp))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = if (subState.isPremium) "Pro Creator Active" else "Upgrade to Pro",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = TextPrimary
                                )
                                Text(
                                    text = if (subState.isPremium) "All features unlocked • No ads" else "4K Export • Unlimited AI • Pro Templates",
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                            }
                        }

                        if (!subState.isPremium) {
                            Button(
                                onClick = { viewModel.setShowUpgradeDialog(true) },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = NeonGold, contentColor = Color.Black)
                            ) {
                                Text("PRO", fontWeight = FontWeight.Black, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }

        // 3. Settings List
        item {
            SectionHeader(title = "App Settings")
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SettingToggleItem(
                    icon = Icons.Default.DarkMode,
                    title = "Dark Gaming Theme",
                    subtitle = "Neon accents & OLED dark styling",
                    checked = profile.isDarkMode,
                    onCheckedChange = { viewModel.toggleDarkMode(it) }
                )

                SettingToggleItem(
                    icon = Icons.Default.Notifications,
                    title = "Channel Upload Notifications",
                    subtitle = "Reminders for scheduled video uploads",
                    checked = profile.notificationsEnabled,
                    onCheckedChange = { viewModel.toggleNotifications(it) }
                )

                SettingActionItem(
                    icon = Icons.Default.HighQuality,
                    title = "Default Export Quality",
                    subtitle = profile.exportQuality,
                    onClick = {
                        val newQuality = if (profile.exportQuality.contains("1080")) "720p Standard HD" else "1080p Full HD"
                        viewModel.setExportQuality(newQuality)
                    }
                )

                SettingActionItem(
                    icon = Icons.Default.DeleteForever,
                    title = "Clear Local Data",
                    subtitle = "Reset database and clear local cache",
                    onClick = { showClearConfirm = true },
                    accentColor = NeonRed
                )
            }
        }

        // 4. About & Legal
        item {
            SectionHeader(title = "About & Legal")
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SettingActionItem(
                    icon = Icons.Default.Info,
                    title = "About Gaming Creator Studio",
                    subtitle = "Version 1.0.0 (Production Ready)",
                    onClick = { viewModel.setShowAboutDialog(true) }
                )

                SettingActionItem(
                    icon = Icons.Default.PrivacyTip,
                    title = "Privacy Policy",
                    subtitle = "Local offline storage & secure zero-key client",
                    onClick = { viewModel.setShowPrivacyDialog(true) }
                )

                SettingActionItem(
                    icon = Icons.Default.Description,
                    title = "Terms of Service",
                    subtitle = "Content creation & commercial YouTube usage",
                    onClick = { viewModel.setShowTermsDialog(true) }
                )
            }
        }
    }

    // Edit Profile Dialog
    if (uiState.isEditingProfile) {
        var name by remember { mutableStateOf(profile.channelName) }
        var desc by remember { mutableStateOf(profile.channelDescription) }
        var game by remember { mutableStateOf(profile.defaultGame) }
        var goal by remember { mutableStateOf(profile.creatorGoal) }
        var subGoal by remember { mutableIntStateOf(profile.subscribersGoal) }
        var curSubs by remember { mutableIntStateOf(profile.currentSubscribers) }

        AlertDialog(
            onDismissRequest = { viewModel.setEditingProfile(false) },
            title = { Text("Edit Channel Profile", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Channel Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = desc,
                        onValueChange = { desc = it },
                        label = { Text("Channel Description") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = game,
                        onValueChange = { game = it },
                        label = { Text("Default Game") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = goal,
                        onValueChange = { goal = it },
                        label = { Text("Creator Goal Title") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = "$curSubs",
                        onValueChange = { curSubs = it.toIntOrNull() ?: curSubs },
                        label = { Text("Current Subscribers") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = "$subGoal",
                        onValueChange = { subGoal = it.toIntOrNull() ?: subGoal },
                        label = { Text("Subscriber Goal") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updateProfile(name, desc, game, goal, subGoal, curSubs)
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.setEditingProfile(false) }) {
                    Text("Cancel")
                }
            },
            containerColor = GamingSurfaceElevated,
            titleContentColor = TextPrimary,
            textContentColor = TextPrimary
        )
    }

    // Upgrade Dialog
    if (uiState.showUpgradeDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.setShowUpgradeDialog(false) },
            title = { Text("Upgrade to Pro Creator", fontWeight = FontWeight.Bold, color = NeonGold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Unlock the full studio for gaming content creators:", fontSize = 13.sp)
                    Text("• 1080p and 4K Ultra HD Export", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    Text("• Unlimited AI Title & Script Generation", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    Text("• All Pro Gaming Templates Unlocked", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    Text("• Ad-free Experience", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    Text("• Advanced Cutout & Glow Text Effects", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("(Test Mode: Billing simulated via SubscriptionManager)", fontSize = 11.sp, color = TextMuted)
                }
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.upgradePlan(PlanType.PREMIUM_MONTHLY) },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonGold, contentColor = Color.Black)
                ) {
                    Text("Activate Pro ($4.99/mo)")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.setShowUpgradeDialog(false) }) {
                    Text("Cancel")
                }
            },
            containerColor = GamingSurfaceElevated,
            titleContentColor = TextPrimary,
            textContentColor = TextPrimary
        )
    }

    // Clear confirmation dialog
    if (showClearConfirm) {
        AlertDialog(
            onDismissRequest = { showClearConfirm = false },
            title = { Text("Clear Local Data?", fontWeight = FontWeight.Bold) },
            text = { Text("This will reset all saved thumbnails, video plans, and AI results.") },
            confirmButton = {
                Button(
                    onClick = {
                        showClearConfirm = false
                        viewModel.clearLocalData()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonRed)
                ) {
                    Text("Clear All")
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearConfirm = false }) {
                    Text("Cancel")
                }
            },
            containerColor = GamingSurfaceElevated,
            titleContentColor = TextPrimary,
            textContentColor = TextPrimary
        )
    }

    // About Dialog
    if (uiState.showAboutDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.setShowAboutDialog(false) },
            title = { Text("Gaming Creator Studio", fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    "Gaming Creator Studio is a production-ready YouTube thumbnail maker and creator assistant designed specifically for mobile gaming creators. Built with Kotlin, Jetpack Compose, Material 3, Room, and AI-powered generator tools.",
                    fontSize = 13.sp
                )
            },
            confirmButton = {
                TextButton(onClick = { viewModel.setShowAboutDialog(false) }) {
                    Text("Close")
                }
            },
            containerColor = GamingSurfaceElevated,
            titleContentColor = TextPrimary,
            textContentColor = TextPrimary
        )
    }

    // Privacy Dialog
    if (uiState.showPrivacyDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.setShowPrivacyDialog(false) },
            title = { Text("Privacy Policy", fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    "Your privacy is our priority. Gaming Creator Studio stores all your thumbnail designs, planner schedules, and creator settings locally on your device via Room Database. No personal files or photos are uploaded to any external server without your consent.",
                    fontSize = 13.sp
                )
            },
            confirmButton = {
                TextButton(onClick = { viewModel.setShowPrivacyDialog(false) }) {
                    Text("Close")
                }
            },
            containerColor = GamingSurfaceElevated,
            titleContentColor = TextPrimary,
            textContentColor = TextPrimary
        )
    }

    // Terms Dialog
    if (uiState.showTermsDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.setShowTermsDialog(false) },
            title = { Text("Terms of Service", fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    "All thumbnails, scripts, descriptions, and titles generated with Gaming Creator Studio are 100% royalty-free for your YouTube, Twitch, TikTok, and social media channels. You maintain full ownership of all content exported.",
                    fontSize = 13.sp
                )
            },
            confirmButton = {
                TextButton(onClick = { viewModel.setShowTermsDialog(false) }) {
                    Text("Close")
                }
            },
            containerColor = GamingSurfaceElevated,
            titleContentColor = TextPrimary,
            textContentColor = TextPrimary
        )
    }
}

@Composable
fun SettingToggleItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    NeonCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = BorderSubtle,
        backgroundColor = GamingSurface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Icon(icon, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(22.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextPrimary)
                    Text(subtitle, fontSize = 11.sp, color = TextSecondary)
                }
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = GamingDarkBackground,
                    checkedTrackColor = NeonCyan
                )
            )
        }
    }
}

@Composable
fun SettingActionItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    accentColor: Color = NeonCyan
) {
    NeonCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        borderColor = BorderSubtle,
        backgroundColor = GamingSurface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Icon(icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(22.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextPrimary)
                    Text(subtitle, fontSize = 11.sp, color = TextSecondary)
                }
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextMuted)
        }
    }
}
