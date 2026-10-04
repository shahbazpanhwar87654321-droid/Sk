package com.example.domain.model

enum class AiContentType {
    TITLE,
    DESCRIPTION,
    HASHTAGS,
    VIDEO_IDEAS,
    THUMBNAIL_TEXT,
    VIDEO_SCRIPT
}

data class SavedAiResult(
    val id: String,
    val type: AiContentType,
    val game: String,
    val topic: String,
    val content: String,
    val createdAt: Long = System.currentTimeMillis()
)

data class TitleGenRequest(
    val game: String,
    val topic: String,
    val style: String
)

data class DescriptionGenRequest(
    val game: String,
    val title: String,
    val topic: String,
    val keywords: String = ""
)

data class HashtagGenRequest(
    val game: String,
    val topic: String,
    val title: String
)

data class VideoIdeaGenRequest(
    val game: String,
    val category: String,
    val difficulty: String
)

data class ThumbnailTextGenRequest(
    val topic: String
)

data class ScriptGenRequest(
    val game: String,
    val topic: String,
    val length: String
)

data class GeneratedScript(
    val title: String,
    val hook: String,
    val intro: String,
    val gameplayBeats: List<String>,
    val commentaryNotes: List<String>,
    val outro: String,
    val callToAction: String
) {
    fun toFormattedText(): String {
        return buildString {
            appendLine("=== VIDEO SCRIPT: $title ===")
            appendLine()
            appendLine("[HOOK - FIRST 5 SECONDS]")
            appendLine(hook)
            appendLine()
            appendLine("[INTRO & TITLE CARD]")
            appendLine(intro)
            appendLine()
            appendLine("[GAMEPLAY BEATS]")
            gameplayBeats.forEachIndexed { i, beat ->
                appendLine("${i + 1}. $beat")
            }
            appendLine()
            appendLine("[COMMENTARY CUES & PRO TIPS]")
            commentaryNotes.forEachIndexed { i, note ->
                appendLine("• $note")
            }
            appendLine()
            appendLine("[OUTRO]")
            appendLine(outro)
            appendLine()
            appendLine("[CALL TO ACTION]")
            appendLine(callToAction)
        }
    }
}
