package com.example.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ui.screens.ai.AiHubScreen
import com.example.ui.screens.ai.AiViewModel
import com.example.ui.screens.ai.DescriptionGeneratorScreen
import com.example.ui.screens.ai.HashtagGeneratorScreen
import com.example.ui.screens.ai.ThumbnailTextGeneratorScreen
import com.example.ui.screens.ai.TitleGeneratorScreen
import com.example.ui.screens.ai.VideoIdeaGeneratorScreen
import com.example.ui.screens.ai.VideoScriptGeneratorScreen
import com.example.ui.screens.home.HomeScreen
import com.example.ui.screens.home.HomeViewModel
import com.example.ui.screens.onboarding.OnboardingScreen
import com.example.ui.screens.onboarding.OnboardingViewModel
import com.example.ui.screens.planner.PlannerScreen
import com.example.ui.screens.planner.PlannerViewModel
import com.example.ui.screens.profile.ProfileScreen
import com.example.ui.screens.profile.ProfileViewModel
import com.example.ui.screens.projects.ProjectsScreen
import com.example.ui.screens.projects.ProjectsViewModel
import com.example.ui.screens.thumbnail.TemplatesScreen
import com.example.ui.screens.thumbnail.ThumbnailEditorScreen
import com.example.ui.screens.thumbnail.ThumbnailViewModel
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.GamingDarkBackground
import com.example.ui.theme.GamingSurface
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector,
    val testTag: String
) {
    data object Home : BottomNavItem(Screen.Home.route, "Home", Icons.Default.Home, "nav_home")
    data object Create : BottomNavItem(Screen.Create.route, "Create", Icons.Default.AddBox, "nav_create")
    data object AiTools : BottomNavItem(Screen.AiTools.route, "AI Tools", Icons.Default.AutoAwesome, "nav_ai")
    data object Planner : BottomNavItem(Screen.Planner.route, "Planner", Icons.Default.CalendarToday, "nav_planner")
    data object Profile : BottomNavItem(Screen.Profile.route, "Profile", Icons.Default.Person, "nav_profile")
}

@Composable
fun AppNavHost(
    startDestination: String = Screen.Home.route
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomNavItems = listOf(
        BottomNavItem.Home,
        BottomNavItem.Create,
        BottomNavItem.AiTools,
        BottomNavItem.Planner,
        BottomNavItem.Profile
    )

    // Only show bottom navigation on top-level tabs
    val isBottomBarVisible = bottomNavItems.any { it.route == currentRoute }

    Scaffold(
        bottomBar = {
            if (isBottomBarVisible) {
                NavigationBar(
                    containerColor = GamingSurface,
                    contentColor = TextPrimary,
                    tonalElevation = 8.dp
                ) {
                    bottomNavItems.forEach { item ->
                        val isSelected = currentRoute == item.route
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                if (currentRoute != item.route) {
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.title,
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = item.title,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = NeonCyan,
                                selectedTextColor = NeonCyan,
                                unselectedIconColor = TextMuted,
                                unselectedTextColor = TextSecondary,
                                indicatorColor = NeonCyan.copy(alpha = 0.15f)
                            ),
                            modifier = Modifier.testTag(item.testTag)
                        )
                    }
                }
            }
        },
        containerColor = GamingDarkBackground
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Onboarding
            composable(Screen.Onboarding.route) {
                val onboardingVm: OnboardingViewModel = viewModel()
                OnboardingScreen(
                    viewModel = onboardingVm,
                    onComplete = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Onboarding.route) { inclusive = true }
                        }
                    }
                )
            }

            // 1. Home
            composable(Screen.Home.route) {
                val homeVm: HomeViewModel = viewModel()
                HomeScreen(
                    viewModel = homeVm,
                    onNavigate = { route -> navController.navigate(route) }
                )
            }

            // 2. Create (Templates / Thumbnail maker entry)
            composable(Screen.Create.route) {
                TemplatesScreen(
                    onNavigate = { route -> navController.navigate(route) }
                )
            }

            // Thumbnail Editor
            composable(
                route = Screen.ThumbnailEditor.route,
                arguments = listOf(
                    navArgument("projectId") {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = ""
                    },
                    navArgument("templateId") {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = ""
                    }
                )
            ) { backStackEntry ->
                val projectId = backStackEntry.arguments?.getString("projectId")
                val templateId = backStackEntry.arguments?.getString("templateId")
                val thumbnailVm: ThumbnailViewModel = viewModel()
                ThumbnailEditorScreen(
                    viewModel = thumbnailVm,
                    projectId = projectId,
                    templateId = templateId,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // 3. AI Hub & AI sub-tools
            composable(Screen.AiTools.route) {
                AiHubScreen(
                    onNavigate = { route -> navController.navigate(route) }
                )
            }

            composable(Screen.TitleGenerator.route) {
                val aiVm: AiViewModel = viewModel()
                TitleGeneratorScreen(
                    viewModel = aiVm,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(Screen.DescriptionGenerator.route) {
                val aiVm: AiViewModel = viewModel()
                DescriptionGeneratorScreen(
                    viewModel = aiVm,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(Screen.HashtagGenerator.route) {
                val aiVm: AiViewModel = viewModel()
                HashtagGeneratorScreen(
                    viewModel = aiVm,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(Screen.VideoIdeaGenerator.route) {
                val aiVm: AiViewModel = viewModel()
                VideoIdeaGeneratorScreen(
                    viewModel = aiVm,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(Screen.ThumbnailTextGenerator.route) {
                val aiVm: AiViewModel = viewModel()
                ThumbnailTextGeneratorScreen(
                    viewModel = aiVm,
                    onNavigateBack = { navController.popBackStack() },
                    onOpenInEditor = { hookText ->
                        navController.navigate(Screen.ThumbnailEditor.createRoute())
                    }
                )
            }

            composable(Screen.VideoScriptGenerator.route) {
                val aiVm: AiViewModel = viewModel()
                VideoScriptGeneratorScreen(
                    viewModel = aiVm,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // 4. Planner
            composable(Screen.Planner.route) {
                val plannerVm: PlannerViewModel = viewModel()
                PlannerScreen(
                    viewModel = plannerVm,
                    onNavigate = { route -> navController.navigate(route) }
                )
            }

            // 5. Projects
            composable(Screen.Projects.route) {
                val projectsVm: ProjectsViewModel = viewModel()
                ProjectsScreen(
                    viewModel = projectsVm,
                    onNavigate = { route -> navController.navigate(route) }
                )
            }

            // 6. Profile & Settings
            composable(Screen.Profile.route) {
                val profileVm: ProfileViewModel = viewModel()
                ProfileScreen(viewModel = profileVm)
            }
        }
    }
}
