package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.database.dao.AiResultDao
import com.example.data.database.dao.CreatorProfileDao
import com.example.data.database.dao.ThumbnailDao
import com.example.data.database.dao.VideoPlanDao
import com.example.data.database.entities.AiResultEntity
import com.example.data.database.entities.CreatorProfileEntity
import com.example.data.database.entities.ThumbnailProjectEntity
import com.example.data.database.entities.VideoPlanEntity
import com.example.domain.model.TemplateCatalog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        ThumbnailProjectEntity::class,
        AiResultEntity::class,
        VideoPlanEntity::class,
        CreatorProfileEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(ThumbnailConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun thumbnailDao(): ThumbnailDao
    abstract fun aiResultDao(): AiResultDao
    abstract fun videoPlanDao(): VideoPlanDao
    abstract fun creatorProfileDao(): CreatorProfileDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "gaming_creator_studio.db"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Preload initial profile and starter sample projects & planner items
                            CoroutineScope(Dispatchers.IO).launch {
                                val database = getInstance(context)
                                database.creatorProfileDao().saveProfile(
                                    CreatorProfileEntity(
                                        id = 1,
                                        channelName = "Gaming Videos Creator",
                                        channelDescription = "Epic gaming challenges, boss fights and clutch moments!",
                                        defaultGame = "Stick War Saga",
                                        creatorGoal = "Reach 10,000 Subscribers & 100 Videos",
                                        subscribersGoal = 10000,
                                        currentSubscribers = 1420,
                                        videosGoal = 100,
                                        isDarkMode = true,
                                        notificationsEnabled = true,
                                        language = "English",
                                        exportQuality = "1080p Full HD",
                                        isPremium = false
                                    )
                                )

                                val converter = ThumbnailConverters()

                                // Preload starter project from catalog
                                val tpl = TemplateCatalog.templates.first()
                                database.thumbnailDao().insertProject(
                                    ThumbnailProjectEntity(
                                        id = "proj_starter_final_boss",
                                        title = tpl.title,
                                        game = tpl.gameTag,
                                        layersJson = converter.fromLayersList(tpl.layers),
                                        backgroundColor = tpl.backgroundColor,
                                        backgroundGradient = tpl.backgroundGradient,
                                        filterSettingsJson = converter.fromFilterSettings(null),
                                        previewUri = null,
                                        createdAt = System.currentTimeMillis() - 86400000L,
                                        updatedAt = System.currentTimeMillis() - 3600000L
                                    )
                                )

                                val tpl2 = TemplateCatalog.templates[1]
                                database.thumbnailDao().insertProject(
                                    ThumbnailProjectEntity(
                                        id = "proj_starter_impossible_victory",
                                        title = tpl2.title,
                                        game = tpl2.gameTag,
                                        layersJson = converter.fromLayersList(tpl2.layers),
                                        backgroundColor = tpl2.backgroundColor,
                                        backgroundGradient = tpl2.backgroundGradient,
                                        filterSettingsJson = converter.fromFilterSettings(null),
                                        previewUri = null,
                                        createdAt = System.currentTimeMillis() - 172800000L,
                                        updatedAt = System.currentTimeMillis() - 7200000L
                                    )
                                )

                                // Preload starter planner items
                                database.videoPlanDao().insertPlan(
                                    VideoPlanEntity(
                                        id = "plan_1",
                                        title = "Defeating the Impossible Final Boss in Stick War",
                                        game = "Stick War Saga",
                                        plannedDate = System.currentTimeMillis() + 86400000L * 2,
                                        status = "THUMBNAIL_READY",
                                        notes = "Thumbnail design is ready! Record opening hook first 10 seconds.",
                                        thumbnailId = "proj_starter_final_boss",
                                        createdAt = System.currentTimeMillis(),
                                        updatedAt = System.currentTimeMillis()
                                    )
                                )
                                database.videoPlanDao().insertPlan(
                                    VideoPlanEntity(
                                        id = "plan_2",
                                        title = "PUBG Mobile 30 Kills Solo vs Squad Clutch",
                                        game = "PUBG Mobile",
                                        plannedDate = System.currentTimeMillis() + 86400000L * 4,
                                        status = "RECORDING",
                                        notes = "Need intense background music for the 1v4 circle fight.",
                                        thumbnailId = null,
                                        createdAt = System.currentTimeMillis(),
                                        updatedAt = System.currentTimeMillis()
                                    )
                                )
                                database.videoPlanDao().insertPlan(
                                    VideoPlanEntity(
                                        id = "plan_3",
                                        title = "Minecraft 100 Days Hardcore Survival Episode 1",
                                        game = "Minecraft",
                                        plannedDate = System.currentTimeMillis() + 86400000L * 7,
                                        status = "PLANNING",
                                        notes = "Build castle defense, gather diamond armor, fight wither.",
                                        thumbnailId = null,
                                        createdAt = System.currentTimeMillis(),
                                        updatedAt = System.currentTimeMillis()
                                    )
                                )

                                // Preload starter AI results
                                database.aiResultDao().insertResult(
                                    AiResultEntity(
                                        id = "ai_starter_1",
                                        type = "TITLE",
                                        game = "Stick War Saga",
                                        topic = "Final Boss Challenge",
                                        content = "Stick War Saga FINAL LUNARCLOPS BOSS 😱🔥",
                                        createdAt = System.currentTimeMillis() - 3600000L
                                    )
                                )
                            }
                        }
                    })
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
