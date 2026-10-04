package com.example.data.repository

import com.example.data.database.dao.AiResultDao
import com.example.data.database.entities.AiResultEntity
import com.example.domain.model.AiContentType
import com.example.domain.model.SavedAiResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AiRepository(
    private val aiResultDao: AiResultDao
) {
    val allResults: Flow<List<SavedAiResult>> = aiResultDao.getAllResults().map { list ->
        list.map { entity ->
            SavedAiResult(
                id = entity.id,
                type = try { AiContentType.valueOf(entity.type) } catch (e: Exception) { AiContentType.TITLE },
                game = entity.game,
                topic = entity.topic,
                content = entity.content,
                createdAt = entity.createdAt
            )
        }
    }

    val resultsCount: Flow<Int> = aiResultDao.getResultsCount()

    fun getResultsByType(type: AiContentType): Flow<List<SavedAiResult>> {
        return aiResultDao.getResultsByType(type.name).map { list ->
            list.map { entity ->
                SavedAiResult(
                    id = entity.id,
                    type = type,
                    game = entity.game,
                    topic = entity.topic,
                    content = entity.content,
                    createdAt = entity.createdAt
                )
            }
        }
    }

    suspend fun saveResult(result: SavedAiResult) {
        aiResultDao.insertResult(
            AiResultEntity(
                id = result.id,
                type = result.type.name,
                game = result.game,
                topic = result.topic,
                content = result.content,
                createdAt = result.createdAt
            )
        )
    }

    suspend fun deleteResult(id: String) {
        aiResultDao.deleteResultById(id)
    }
}
