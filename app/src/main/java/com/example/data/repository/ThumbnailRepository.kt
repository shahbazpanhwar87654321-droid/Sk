package com.example.data.repository

import com.example.data.database.ThumbnailConverters
import com.example.data.database.dao.ThumbnailDao
import com.example.data.database.entities.ThumbnailProjectEntity
import com.example.domain.model.ThumbnailDesign
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ThumbnailRepository(
    private val thumbnailDao: ThumbnailDao
) {
    private val converter = ThumbnailConverters()

    val allProjects: Flow<List<ThumbnailDesign>> = thumbnailDao.getAllProjects().map { list ->
        list.map { entity ->
            ThumbnailDesign(
                id = entity.id,
                title = entity.title,
                game = entity.game,
                layers = converter.toLayersList(entity.layersJson),
                backgroundColor = entity.backgroundColor,
                backgroundGradient = entity.backgroundGradient,
                filterSettings = converter.toFilterSettings(entity.filterSettingsJson),
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt,
                previewUri = entity.previewUri
            )
        }
    }

    val projectsCount: Flow<Int> = thumbnailDao.getProjectCount()

    suspend fun getProjectById(id: String): ThumbnailDesign? {
        val entity = thumbnailDao.getProjectById(id) ?: return null
        return ThumbnailDesign(
            id = entity.id,
            title = entity.title,
            game = entity.game,
            layers = converter.toLayersList(entity.layersJson),
            backgroundColor = entity.backgroundColor,
            backgroundGradient = entity.backgroundGradient,
            filterSettings = converter.toFilterSettings(entity.filterSettingsJson),
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
            previewUri = entity.previewUri
        )
    }

    suspend fun saveProject(design: ThumbnailDesign) {
        val entity = ThumbnailProjectEntity(
            id = design.id,
            title = design.title,
            game = design.game,
            layersJson = converter.fromLayersList(design.layers),
            backgroundColor = design.backgroundColor,
            backgroundGradient = design.backgroundGradient,
            filterSettingsJson = converter.fromFilterSettings(design.filterSettings),
            previewUri = design.previewUri,
            createdAt = design.createdAt,
            updatedAt = System.currentTimeMillis()
        )
        thumbnailDao.insertProject(entity)
    }

    suspend fun deleteProject(id: String) {
        thumbnailDao.deleteProjectById(id)
    }

    suspend fun duplicateProject(id: String, newId: String, newTitle: String) {
        val existing = getProjectById(id) ?: return
        val duplicated = existing.copy(
            id = newId,
            title = newTitle,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        saveProject(duplicated)
    }
}
