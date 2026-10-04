package com.example.data.database

import androidx.room.TypeConverter
import com.example.domain.model.FilterSettings
import com.example.domain.model.LayerType
import com.example.domain.model.ShapeType
import com.example.domain.model.TextEffect
import com.example.domain.model.ThumbnailLayer
import org.json.JSONArray
import org.json.JSONObject

class ThumbnailConverters {

    @TypeConverter
    fun fromLayersList(layers: List<ThumbnailLayer>?): String {
        if (layers == null) return "[]"
        val array = JSONArray()
        for (layer in layers) {
            val obj = JSONObject().apply {
                put("id", layer.id)
                put("type", layer.type.name)
                put("name", layer.name)
                put("x", layer.x.toDouble())
                put("y", layer.y.toDouble())
                put("width", layer.width.toDouble())
                put("height", layer.height.toDouble())
                put("scale", layer.scale.toDouble())
                put("rotation", layer.rotation.toDouble())
                put("opacity", layer.opacity.toDouble())
                put("isLocked", layer.isLocked)
                put("isVisible", layer.isVisible)
                put("zIndex", layer.zIndex)
                put("imageUri", layer.imageUri ?: "")
                put("isCutout", layer.isCutout)
                put("text", layer.text)
                put("fontSize", layer.fontSize.toDouble())
                put("fontFamily", layer.fontFamily)
                put("textColor", layer.textColor)
                put("outlineColor", layer.outlineColor ?: -1L)
                put("outlineWidth", layer.outlineWidth.toDouble())
                put("shadowColor", layer.shadowColor ?: -1L)
                put("shadowRadius", layer.shadowRadius.toDouble())
                put("glowColor", layer.glowColor ?: -1L)
                put("glowRadius", layer.glowRadius.toDouble())
                put("effect", layer.effect.name)
                put("shapeType", layer.shapeType.name)
                put("fillColor", layer.fillColor)
                put("strokeColor", layer.strokeColor)
                put("strokeWidth", layer.strokeWidth.toDouble())
                put("stickerText", layer.stickerText)
                put("stickerIcon", layer.stickerIcon)
            }
            array.put(obj)
        }
        return array.toString()
    }

    @TypeConverter
    fun toLayersList(json: String?): List<ThumbnailLayer> {
        if (json.isNullOrBlank()) return emptyList()
        val list = mutableListOf<ThumbnailLayer>()
        try {
            val array = JSONArray(json)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                val outlineCol = obj.optLong("outlineColor", -1L)
                val shadowCol = obj.optLong("shadowColor", -1L)
                val glowCol = obj.optLong("glowColor", -1L)
                list.add(
                    ThumbnailLayer(
                        id = obj.getString("id"),
                        type = LayerType.valueOf(obj.optString("type", LayerType.TEXT.name)),
                        name = obj.optString("name", "Layer"),
                        x = obj.optDouble("x", 640.0).toFloat(),
                        y = obj.optDouble("y", 360.0).toFloat(),
                        width = obj.optDouble("width", 200.0).toFloat(),
                        height = obj.optDouble("height", 100.0).toFloat(),
                        scale = obj.optDouble("scale", 1.0).toFloat(),
                        rotation = obj.optDouble("rotation", 0.0).toFloat(),
                        opacity = obj.optDouble("opacity", 1.0).toFloat(),
                        isLocked = obj.optBoolean("isLocked", false),
                        isVisible = obj.optBoolean("isVisible", true),
                        zIndex = obj.optInt("zIndex", 0),
                        imageUri = obj.optString("imageUri").takeIf { it.isNotBlank() },
                        isCutout = obj.optBoolean("isCutout", false),
                        text = obj.optString("text", ""),
                        fontSize = obj.optDouble("fontSize", 48.0).toFloat(),
                        fontFamily = obj.optString("fontFamily", "Impact"),
                        textColor = obj.optLong("textColor", 0xFFFFFFFF),
                        outlineColor = if (outlineCol != -1L) outlineCol else null,
                        outlineWidth = obj.optDouble("outlineWidth", 4.0).toFloat(),
                        shadowColor = if (shadowCol != -1L) shadowCol else null,
                        shadowRadius = obj.optDouble("shadowRadius", 8.0).toFloat(),
                        glowColor = if (glowCol != -1L) glowCol else null,
                        glowRadius = obj.optDouble("glowRadius", 0.0).toFloat(),
                        effect = try { TextEffect.valueOf(obj.optString("effect", TextEffect.NONE.name)) } catch (e: Exception) { TextEffect.NONE },
                        shapeType = try { ShapeType.valueOf(obj.optString("shapeType", ShapeType.ROUNDED_RECTANGLE.name)) } catch (e: Exception) { ShapeType.ROUNDED_RECTANGLE },
                        fillColor = obj.optLong("fillColor", 0xFF00E5FF),
                        strokeColor = obj.optLong("strokeColor", 0xFFFFFFFF),
                        strokeWidth = obj.optDouble("strokeWidth", 2.0).toFloat(),
                        stickerText = obj.optString("stickerText", ""),
                        stickerIcon = obj.optString("stickerIcon", "")
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    @TypeConverter
    fun fromFilterSettings(filter: FilterSettings?): String {
        val f = filter ?: FilterSettings()
        return JSONObject().apply {
            put("brightness", f.brightness.toDouble())
            put("contrast", f.contrast.toDouble())
            put("saturation", f.saturation.toDouble())
            put("blur", f.blur.toDouble())
            put("vignette", f.vignette.toDouble())
        }.toString()
    }

    @TypeConverter
    fun toFilterSettings(json: String?): FilterSettings {
        if (json.isNullOrBlank()) return FilterSettings()
        return try {
            val obj = JSONObject(json)
            FilterSettings(
                brightness = obj.optDouble("brightness", 1.0).toFloat(),
                contrast = obj.optDouble("contrast", 1.0).toFloat(),
                saturation = obj.optDouble("saturation", 1.0).toFloat(),
                blur = obj.optDouble("blur", 0.0).toFloat(),
                vignette = obj.optDouble("vignette", 0.0).toFloat()
            )
        } catch (e: Exception) {
            FilterSettings()
        }
    }
}
