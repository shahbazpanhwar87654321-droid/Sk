package com.example.data.repository

import com.example.data.database.dao.VideoPlanDao
import com.example.data.database.entities.VideoPlanEntity
import com.example.domain.model.VideoPlan
import com.example.domain.model.VideoStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlannerRepository(
    private val videoPlanDao: VideoPlanDao
) {
    val allPlans: Flow<List<VideoPlan>> = videoPlanDao.getAllPlans().map { list ->
        list.map { entity ->
            VideoPlan(
                id = entity.id,
                title = entity.title,
                game = entity.game,
                plannedDate = entity.plannedDate,
                status = VideoStatus.fromString(entity.status),
                notes = entity.notes,
                thumbnailId = entity.thumbnailId,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt
            )
        }
    }

    val plansCount: Flow<Int> = videoPlanDao.getPlansCount()

    suspend fun getPlanById(id: String): VideoPlan? {
        val entity = videoPlanDao.getPlanById(id) ?: return null
        return VideoPlan(
            id = entity.id,
            title = entity.title,
            game = entity.game,
            plannedDate = entity.plannedDate,
            status = VideoStatus.fromString(entity.status),
            notes = entity.notes,
            thumbnailId = entity.thumbnailId,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }

    suspend fun savePlan(plan: VideoPlan) {
        val entity = VideoPlanEntity(
            id = plan.id,
            title = plan.title,
            game = plan.game,
            plannedDate = plan.plannedDate,
            status = plan.status.name,
            notes = plan.notes,
            thumbnailId = plan.thumbnailId,
            createdAt = plan.createdAt,
            updatedAt = System.currentTimeMillis()
        )
        videoPlanDao.insertPlan(entity)
    }

    suspend fun deletePlan(id: String) {
        videoPlanDao.deletePlanById(id)
    }
}
