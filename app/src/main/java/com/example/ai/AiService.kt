package com.example.ai

import com.example.domain.model.DescriptionGenRequest
import com.example.domain.model.GeneratedScript
import com.example.domain.model.HashtagGenRequest
import com.example.domain.model.ScriptGenRequest
import com.example.domain.model.ThumbnailTextGenRequest
import com.example.domain.model.TitleGenRequest
import com.example.domain.model.VideoIdeaGenRequest

interface AiService {
    suspend fun generateTitles(request: TitleGenRequest): Result<List<String>>
    suspend fun generateDescription(request: DescriptionGenRequest): Result<String>
    suspend fun generateHashtags(request: HashtagGenRequest): Result<List<String>>
    suspend fun generateVideoIdeas(request: VideoIdeaGenRequest): Result<List<String>>
    suspend fun generateThumbnailText(request: ThumbnailTextGenRequest): Result<List<String>>
    suspend fun generateVideoScript(request: ScriptGenRequest): Result<GeneratedScript>

    /**
     * AI Background Removal Hook.
     * When external service is not configured, provides a transparent cutout placeholder.
     */
    suspend fun removeBackground(imageUri: String): Result<String>
}
