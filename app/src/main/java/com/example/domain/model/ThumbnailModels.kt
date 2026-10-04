package com.example.domain.model

enum class LayerType {
    IMAGE,
    TEXT,
    SHAPE,
    STICKER
}

enum class ShapeType {
    RECTANGLE,
    ROUNDED_RECTANGLE,
    CIRCLE,
    STAR,
    BANNER,
    BADGE
}

enum class TextEffect {
    NONE,
    FIRE,
    ICE,
    ELECTRIC,
    GOLD,
    NEON,
    RED_GLOW,
    HORROR,
    EXPLOSION,
    METALLIC
}

data class FilterSettings(
    val brightness: Float = 1.0f, // 0.5 - 1.5
    val contrast: Float = 1.0f,   // 0.5 - 1.5
    val saturation: Float = 1.0f, // 0.0 - 2.0
    val blur: Float = 0.0f,       // 0 - 25
    val vignette: Float = 0.0f    // 0.0 - 1.0
)

data class ThumbnailLayer(
    val id: String,
    val type: LayerType,
    val name: String,
    val x: Float = 640f, // center of 1280
    val y: Float = 360f, // center of 720
    val width: Float = 200f,
    val height: Float = 100f,
    val scale: Float = 1.0f,
    val rotation: Float = 0.0f,
    val opacity: Float = 1.0f,
    val isLocked: Boolean = false,
    val isVisible: Boolean = true,
    val zIndex: Int = 0,

    // Image properties
    val imageUri: String? = null,
    val isCutout: Boolean = false,

    // Text properties
    val text: String = "",
    val fontSize: Float = 48f,
    val fontFamily: String = "Impact",
    val textColor: Long = 0xFFFFFFFF,
    val outlineColor: Long? = 0xFF000000,
    val outlineWidth: Float = 4f,
    val shadowColor: Long? = 0xAA000000,
    val shadowRadius: Float = 8f,
    val glowColor: Long? = null,
    val glowRadius: Float = 0f,
    val effect: TextEffect = TextEffect.NONE,

    // Shape properties
    val shapeType: ShapeType = ShapeType.ROUNDED_RECTANGLE,
    val fillColor: Long = 0xFF00E5FF,
    val strokeColor: Long = 0xFFFFFFFF,
    val strokeWidth: Float = 2f,

    // Sticker / Badge properties
    val stickerText: String = "",
    val stickerIcon: String = ""
)

data class ThumbnailDesign(
    val id: String,
    val title: String,
    val game: String = "General Gaming",
    val layers: List<ThumbnailLayer> = emptyList(),
    val backgroundColor: Long = 0xFF0F172A,
    val backgroundGradient: String? = "CYBER",
    val filterSettings: FilterSettings = FilterSettings(),
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val previewUri: String? = null
)
