package com.example.ui.navigation

sealed class Screen(val route: String) {
    data object Onboarding : Screen("onboarding")
    data object Home : Screen("home")
    data object Create : Screen("create")
    data object ThumbnailEditor : Screen("thumbnail_editor?projectId={projectId}&templateId={templateId}") {
        fun createRoute(projectId: String? = null, templateId: String? = null): String {
            return "thumbnail_editor?projectId=${projectId ?: ""}&templateId=${templateId ?: ""}"
        }
    }
    data object AiTools : Screen("ai_tools")
    data object TitleGenerator : Screen("ai_title")
    data object DescriptionGenerator : Screen("ai_description")
    data object HashtagGenerator : Screen("ai_hashtags")
    data object VideoIdeaGenerator : Screen("ai_ideas")
    data object ThumbnailTextGenerator : Screen("ai_thumbnail_text")
    data object VideoScriptGenerator : Screen("ai_script")
    data object Planner : Screen("planner")
    data object Projects : Screen("projects")
    data object Profile : Screen("profile")
}
