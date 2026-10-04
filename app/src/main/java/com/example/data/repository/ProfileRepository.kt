package com.example.data.repository

import com.example.data.database.dao.CreatorProfileDao
import com.example.data.database.entities.CreatorProfileEntity
import com.example.domain.model.CreatorProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProfileRepository(
    private val creatorProfileDao: CreatorProfileDao
) {
    val profile: Flow<CreatorProfile> = creatorProfileDao.getProfile().map { entity ->
        if (entity != null) {
            CreatorProfile(
                channelName = entity.channelName,
                channelDescription = entity.channelDescription,
                defaultGame = entity.defaultGame,
                creatorGoal = entity.creatorGoal,
                subscribersGoal = entity.subscribersGoal,
                currentSubscribers = entity.currentSubscribers,
                videosGoal = entity.videosGoal,
                isDarkMode = entity.isDarkMode,
                notificationsEnabled = entity.notificationsEnabled,
                language = entity.language,
                exportQuality = entity.exportQuality,
                isPremium = entity.isPremium
            )
        } else {
            CreatorProfile()
        }
    }

    suspend fun saveProfile(profile: CreatorProfile) {
        val entity = CreatorProfileEntity(
            id = 1,
            channelName = profile.channelName,
            channelDescription = profile.channelDescription,
            defaultGame = profile.defaultGame,
            creatorGoal = profile.creatorGoal,
            subscribersGoal = profile.subscribersGoal,
            currentSubscribers = profile.currentSubscribers,
            videosGoal = profile.videosGoal,
            isDarkMode = profile.isDarkMode,
            notificationsEnabled = profile.notificationsEnabled,
            language = profile.language,
            exportQuality = profile.exportQuality,
            isPremium = profile.isPremium
        )
        creatorProfileDao.saveProfile(entity)
    }
}
