package com.example.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.database.entities.AiResultEntity
import com.example.data.database.entities.CreatorProfileEntity
import com.example.data.database.entities.ThumbnailProjectEntity
import com.example.data.database.entities.VideoPlanEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ThumbnailDao {
    @Query("SELECT * FROM thumbnail_projects ORDER BY updatedAt DESC")
    fun getAllProjects(): Flow<List<ThumbnailProjectEntity>>

    @Query("SELECT * FROM thumbnail_projects WHERE id = :id LIMIT 1")
    suspend fun getProjectById(id: String): ThumbnailProjectEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: ThumbnailProjectEntity)

    @Update
    suspend fun updateProject(project: ThumbnailProjectEntity)

    @Query("DELETE FROM thumbnail_projects WHERE id = :id")
    suspend fun deleteProjectById(id: String)

    @Query("SELECT COUNT(*) FROM thumbnail_projects")
    fun getProjectCount(): Flow<Int>
}

@Dao
interface AiResultDao {
    @Query("SELECT * FROM ai_results ORDER BY createdAt DESC")
    fun getAllResults(): Flow<List<AiResultEntity>>

    @Query("SELECT * FROM ai_results WHERE type = :type ORDER BY createdAt DESC")
    fun getResultsByType(type: String): Flow<List<AiResultEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResult(result: AiResultEntity)

    @Query("DELETE FROM ai_results WHERE id = :id")
    suspend fun deleteResultById(id: String)

    @Query("SELECT COUNT(*) FROM ai_results")
    fun getResultsCount(): Flow<Int>
}

@Dao
interface VideoPlanDao {
    @Query("SELECT * FROM video_plans ORDER BY plannedDate ASC")
    fun getAllPlans(): Flow<List<VideoPlanEntity>>

    @Query("SELECT * FROM video_plans WHERE id = :id LIMIT 1")
    suspend fun getPlanById(id: String): VideoPlanEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlan(plan: VideoPlanEntity)

    @Update
    suspend fun updatePlan(plan: VideoPlanEntity)

    @Query("DELETE FROM video_plans WHERE id = :id")
    suspend fun deletePlanById(id: String)

    @Query("SELECT COUNT(*) FROM video_plans")
    fun getPlansCount(): Flow<Int>
}

@Dao
interface CreatorProfileDao {
    @Query("SELECT * FROM creator_profile WHERE id = 1 LIMIT 1")
    fun getProfile(): Flow<CreatorProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProfile(profile: CreatorProfileEntity)
}
