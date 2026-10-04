package com.example.domain.model

enum class VideoStatus(val displayName: String, val colorHex: Long) {
    IDEA("Idea", 0xFF9E9E9E),
    PLANNING("Planning", 0xFF2196F3),
    RECORDING("Recording", 0xFFFF9800),
    EDITING("Editing", 0xFF9C27B0),
    THUMBNAIL_READY("Thumbnail Ready", 0xFF00BCD4),
    SCHEDULED("Scheduled", 0xFFFF5722),
    PUBLISHED("Published", 0xFF4CAF50);

    companion object {
        fun fromString(value: String): VideoStatus {
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) || it.displayName.equals(value, ignoreCase = true) }
                ?: IDEA
        }
    }
}

data class VideoPlan(
    val id: String,
    val title: String,
    val game: String,
    val plannedDate: Long,
    val status: VideoStatus = VideoStatus.IDEA,
    val notes: String = "",
    val thumbnailId: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
