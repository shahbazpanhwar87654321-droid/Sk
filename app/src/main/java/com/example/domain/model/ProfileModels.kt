package com.example.domain.model

data class CreatorProfile(
    val channelName: String = "Gaming Videos Creator",
    val channelDescription: String = "Daily gaming videos, epic boss fights, walkthroughs & challenges!",
    val defaultGame: String = "Stick War Saga",
    val creatorGoal: String = "Reach 10,000 Subscribers & 100 Uploads",
    val subscribersGoal: Int = 10000,
    val currentSubscribers: Int = 1250,
    val videosGoal: Int = 100,
    val isDarkMode: Boolean = true,
    val notificationsEnabled: Boolean = true,
    val language: String = "English",
    val exportQuality: String = "1080p Full HD",
    val isPremium: Boolean = false
)
