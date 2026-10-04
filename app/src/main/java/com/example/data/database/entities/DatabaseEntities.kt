package com.example.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "thumbnail_projects")
data class ThumbnailProjectEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val game: String,
    val layersJson: String,
    val backgroundColor: Long,
    val backgroundGradient: String?,
    val filterSettingsJson: String,
    val previewUri: String?,
    val createdAt: Long,
    val updatedAt: Long
)

@Entity(tableName = "ai_results")
data class AiResultEntity(
    @PrimaryKey
    val id: String,
    val type: String,
    val game: String,
    val topic: String,
    val content: String,
    val createdAt: Long
)

@Entity(tableName = "video_plans")
data class VideoPlanEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val game: String,
    val plannedDate: Long,
    val status: String,
    val notes: String,
    val thumbnailId: String?,
    val createdAt: Long,
    val updatedAt: Long
)

@Entity(tableName = "creator_profile")
data class CreatorProfileEntity(
    @PrimaryKey
    val id: Int = 1,
    val channelName: String,
    val channelDescription: String,
    val defaultGame: String,
    val creatorGoal: String,
    val subscribersGoal: Int,
    val currentSubscribers: Int,
    val videosGoal: Int,
    val isDarkMode: Boolean,
    val notificationsEnabled: Boolean,
    val language: String,
    val exportQuality: String,
    val isPremium: Boolean
)
