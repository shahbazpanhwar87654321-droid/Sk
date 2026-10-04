package com.example.ai

import android.util.Log
import com.example.BuildConfig
import com.example.domain.model.DescriptionGenRequest
import com.example.domain.model.GeneratedScript
import com.example.domain.model.HashtagGenRequest
import com.example.domain.model.ScriptGenRequest
import com.example.domain.model.ThumbnailTextGenRequest
import com.example.domain.model.TitleGenRequest
import com.example.domain.model.VideoIdeaGenRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class GeminiAiService(
    private val fallbackService: AiService = MockAiService()
) : AiService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(25, TimeUnit.SECONDS)
        .build()

    private fun getApiKey(): String {
        return try {
            val field = BuildConfig::class.java.getField("GEMINI_API_KEY")
            val key = field.get(null) as? String ?: ""
            if (key == "MY_GEMINI_API_KEY" || key.isBlank()) "" else key
        } catch (e: Throwable) {
            ""
        }
    }

    private suspend fun callGeminiApi(prompt: String): String? = withContext(Dispatchers.IO) {
        val apiKey = getApiKey()
        if (apiKey.isBlank()) {
            return@withContext null
        }

        try {
            val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=$apiKey"
            val bodyJson = JSONObject().apply {
                val contents = JSONArray().apply {
                    val partObj = JSONObject().apply {
                        val parts = JSONArray().apply {
                            put(JSONObject().apply { put("text", prompt) })
                        }
                        put("parts", parts)
                    }
                    put(partObj)
                }
                put("contents", contents)
            }

            val request = Request.Builder()
                .url(url)
                .post(bodyJson.toString().toRequestBody("application/json".toMediaType()))
                .build()

            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                Log.w("GeminiAiService", "API error: ${response.code}")
                return@withContext null
            }

            val resBody = response.body?.string() ?: return@withContext null
            val jsonRes = JSONObject(resBody)
            val candidates = jsonRes.optJSONArray("candidates")
            if (candidates != null && candidates.length() > 0) {
                val candidate = candidates.getJSONObject(0)
                val content = candidate.optJSONObject("content")
                val parts = content?.optJSONArray("parts")
                if (parts != null && parts.length() > 0) {
                    return@withContext parts.getJSONObject(0).optString("text")
                }
            }
            null
        } catch (e: Exception) {
            Log.e("GeminiAiService", "Failed to call Gemini API, falling back to mock", e)
            null
        }
    }

    override suspend fun generateTitles(request: TitleGenRequest): Result<List<String>> {
        val prompt = "You are a gaming YouTube title expert. Generate 5 high-CTR, punchy YouTube titles for the game '${request.game}', topic '${request.topic}', and style '${request.style}'. Return ONLY the 5 titles, each on a new line without numbers or bullet points."
        val resultText = callGeminiApi(prompt)
        if (!resultText.isNullOrBlank()) {
            val titles = resultText.lines().map { it.trim().removePrefix("1. ").removePrefix("- ").removePrefix("* ") }.filter { it.isNotBlank() }
            if (titles.isNotEmpty()) {
                return Result.success(titles.take(5))
            }
        }
        return fallbackService.generateTitles(request)
    }

    override suspend fun generateDescription(request: DescriptionGenRequest): Result<String> {
        val prompt = "You are a professional YouTube creator assistant. Write a high-converting YouTube description for a video titled '${request.title}' in the game '${request.game}'. Topic: '${request.topic}'. Keywords: '${request.keywords}'. Include an opening hook, what happens, subscribe CTA, video chapters, disclaimer, and 5 hashtags."
        val resultText = callGeminiApi(prompt)
        if (!resultText.isNullOrBlank()) {
            return Result.success(resultText.trim())
        }
        return fallbackService.generateDescription(request)
    }

    override suspend fun generateHashtags(request: HashtagGenRequest): Result<List<String>> {
        val prompt = "Generate 12 trending YouTube gaming hashtags for the game '${request.game}' and topic '${request.topic}'. Return them separated by spaces or newlines, each starting with '#'."
        val resultText = callGeminiApi(prompt)
        if (!resultText.isNullOrBlank()) {
            val tags = resultText.split("\\s+".toRegex()).filter { it.startsWith("#") && it.length > 2 }
            if (tags.isNotEmpty()) {
                return Result.success(tags.take(12))
            }
        }
        return fallbackService.generateHashtags(request)
    }

    override suspend fun generateVideoIdeas(request: VideoIdeaGenRequest): Result<List<String>> {
        val prompt = "Generate 10 viral YouTube gaming video ideas for the game '${request.game}', category '${request.category}', difficulty '${request.difficulty}'. Return each idea on a separate line."
        val resultText = callGeminiApi(prompt)
        if (!resultText.isNullOrBlank()) {
            val ideas = resultText.lines().map { it.trim().removePrefix("1. ").removePrefix("- ").removePrefix("* ") }.filter { it.isNotBlank() }
            if (ideas.isNotEmpty()) {
                return Result.success(ideas.take(10))
            }
        }
        return fallbackService.generateVideoIdeas(request)
    }

    override suspend fun generateThumbnailText(request: ThumbnailTextGenRequest): Result<List<String>> {
        val prompt = "Generate 8 ultra-short, explosive YouTube thumbnail text hooks (1 to 4 words max, in ALL CAPS with optional emoji) for the topic '${request.topic}'. Examples: 'FINAL BOSS!', 'IMPOSSIBLE!', '30 KILLS!'. Return one per line."
        val resultText = callGeminiApi(prompt)
        if (!resultText.isNullOrBlank()) {
            val hooks = resultText.lines().map { it.trim().removePrefix("1. ").removePrefix("- ").removePrefix("* ") }.filter { it.isNotBlank() }
            if (hooks.isNotEmpty()) {
                return Result.success(hooks.take(8))
            }
        }
        return fallbackService.generateThumbnailText(request)
    }

    override suspend fun generateVideoScript(request: ScriptGenRequest): Result<GeneratedScript> {
        return fallbackService.generateVideoScript(request)
    }

    override suspend fun removeBackground(imageUri: String): Result<String> {
        return fallbackService.removeBackground(imageUri)
    }
}
