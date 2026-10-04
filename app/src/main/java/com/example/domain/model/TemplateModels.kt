package com.example.domain.model

import java.util.UUID

data class TemplateCategory(
    val id: String,
    val name: String,
    val iconName: String
)

data class ThumbnailTemplate(
    val id: String,
    val title: String,
    val category: String,
    val gameTag: String,
    val description: String,
    val badge: String = "POPULAR",
    val backgroundColor: Long,
    val backgroundGradient: String,
    val layers: List<ThumbnailLayer>
)

object TemplateCatalog {
    val categories = listOf(
        TemplateCategory("all", "All", "Star"),
        TemplateCategory("stick_war", "Stick War", "SportsMartialArts"),
        TemplateCategory("pubg", "PUBG", "GpsFixed"),
        TemplateCategory("gta", "GTA", "DirectionsCar"),
        TemplateCategory("minecraft", "Minecraft", "CropSquare"),
        TemplateCategory("roblox", "Roblox", "VideogameAsset"),
        TemplateCategory("horror", "Horror", "VisibilityOff"),
        TemplateCategory("action", "Action", "FlashOn"),
        TemplateCategory("racing", "Racing", "Speed"),
        TemplateCategory("adventure", "Adventure", "Explore"),
        TemplateCategory("general", "General Gaming", "SportsEsports")
    )

    val templates: List<ThumbnailTemplate> = listOf(
        ThumbnailTemplate(
            id = "tpl_final_boss",
            title = "FINAL BOSS 😱",
            category = "stick_war",
            gameTag = "Stick War Saga",
            description = "High stakes boss battle thumbnail with fiery glowing typography and warning badge.",
            badge = "HOT",
            backgroundColor = 0xFF180A0A,
            backgroundGradient = "FIRE",
            layers = listOf(
                ThumbnailLayer(
                    id = "layer_shape_banner",
                    type = LayerType.SHAPE,
                    name = "Header Banner",
                    x = 640f,
                    y = 140f,
                    width = 750f,
                    height = 90f,
                    shapeType = ShapeType.ROUNDED_RECTANGLE,
                    fillColor = 0xDDFF1744,
                    strokeColor = 0xFFFFD600,
                    strokeWidth = 4f,
                    zIndex = 1
                ),
                ThumbnailLayer(
                    id = "layer_txt_game",
                    type = LayerType.TEXT,
                    name = "Game Tag",
                    text = "STICK WAR SAGA",
                    x = 640f,
                    y = 135f,
                    fontSize = 32f,
                    textColor = 0xFFFFFFFF,
                    outlineColor = 0xFF000000,
                    outlineWidth = 3f,
                    effect = TextEffect.GOLD,
                    zIndex = 2
                ),
                ThumbnailLayer(
                    id = "layer_txt_boss",
                    type = LayerType.TEXT,
                    name = "Main Hook",
                    text = "FINAL BOSS 😱",
                    x = 640f,
                    y = 360f,
                    fontSize = 84f,
                    textColor = 0xFFFFD600,
                    outlineColor = 0xFFFF1744,
                    outlineWidth = 8f,
                    effect = TextEffect.FIRE,
                    shadowColor = 0xFFFF2200,
                    shadowRadius = 24f,
                    zIndex = 3
                ),
                ThumbnailLayer(
                    id = "layer_sticker_impossible",
                    type = LayerType.STICKER,
                    name = "Sticker",
                    stickerText = "IMPOSSIBLE MODE",
                    x = 640f,
                    y = 540f,
                    width = 440f,
                    height = 80f,
                    fillColor = 0xFF000000,
                    strokeColor = 0xFFFF0055,
                    textColor = 0xFFFF0055,
                    zIndex = 4
                )
            )
        ),
        ThumbnailTemplate(
            id = "tpl_impossible_victory",
            title = "IMPOSSIBLE VICTORY",
            category = "action",
            gameTag = "Action Gaming",
            description = "Electrifying clutch victory with cyber neon glow and electric text effects.",
            badge = "EPIC",
            backgroundColor = 0xFF06152B,
            backgroundGradient = "CYBER",
            layers = listOf(
                ThumbnailLayer(
                    id = "layer_shape_bg",
                    type = LayerType.SHAPE,
                    name = "Glow Accent",
                    x = 640f,
                    y = 360f,
                    width = 900f,
                    height = 280f,
                    shapeType = ShapeType.ROUNDED_RECTANGLE,
                    fillColor = 0x3300F0FF,
                    strokeColor = 0xFF00F0FF,
                    strokeWidth = 3f,
                    zIndex = 1
                ),
                ThumbnailLayer(
                    id = "layer_txt_clutch",
                    type = LayerType.TEXT,
                    name = "Subhead",
                    text = "1 HP CLUTCH...",
                    x = 640f,
                    y = 260f,
                    fontSize = 42f,
                    textColor = 0xFF00FF88,
                    outlineColor = 0xFF003B1F,
                    outlineWidth = 4f,
                    effect = TextEffect.ELECTRIC,
                    zIndex = 2
                ),
                ThumbnailLayer(
                    id = "layer_txt_main",
                    type = LayerType.TEXT,
                    name = "Title",
                    text = "IMPOSSIBLE VICTORY",
                    x = 640f,
                    y = 390f,
                    fontSize = 72f,
                    textColor = 0xFF00F0FF,
                    outlineColor = 0xFF041833,
                    outlineWidth = 8f,
                    effect = TextEffect.NEON,
                    shadowColor = 0xFF00F0FF,
                    shadowRadius = 20f,
                    zIndex = 3
                ),
                ThumbnailLayer(
                    id = "layer_sticker_vs",
                    type = LayerType.STICKER,
                    name = "Win Badge",
                    stickerText = "1v5 ACE!",
                    x = 640f,
                    y = 530f,
                    width = 300f,
                    height = 70f,
                    fillColor = 0xFF00F0FF,
                    textColor = 0xFF06152B,
                    zIndex = 4
                )
            )
        ),
        ThumbnailTemplate(
            id = "tpl_30_kills",
            title = "30 KILLS!",
            category = "pubg",
            gameTag = "PUBG Mobile",
            description = "High kill count showcase with military neon badges and bold impact text.",
            badge = "TRENDING",
            backgroundColor = 0xFF1E1705,
            backgroundGradient = "GOLD",
            layers = listOf(
                ThumbnailLayer(
                    id = "layer_txt_kills",
                    type = LayerType.TEXT,
                    name = "Kills Text",
                    text = "30 KILLS!",
                    x = 640f,
                    y = 320f,
                    fontSize = 96f,
                    textColor = 0xFFFFD700,
                    outlineColor = 0xFF1A1200,
                    outlineWidth = 9f,
                    effect = TextEffect.GOLD,
                    shadowColor = 0xFFFF9900,
                    shadowRadius = 18f,
                    zIndex = 2
                ),
                ThumbnailLayer(
                    id = "layer_txt_solo",
                    type = LayerType.TEXT,
                    name = "Sub",
                    text = "SOLO VS SQUAD CHICKEN DINNER",
                    x = 640f,
                    y = 450f,
                    fontSize = 36f,
                    textColor = 0xFFFFFFFF,
                    outlineColor = 0xFF000000,
                    outlineWidth = 4f,
                    effect = TextEffect.METALLIC,
                    zIndex = 3
                ),
                ThumbnailLayer(
                    id = "layer_stk_rank",
                    type = LayerType.STICKER,
                    name = "Rank Badge",
                    stickerText = "CONQUEROR LOBBY 👑",
                    x = 640f,
                    y = 560f,
                    width = 460f,
                    height = 75f,
                    fillColor = 0xFFFF9900,
                    textColor = 0xFF000000,
                    zIndex = 4
                )
            )
        ),
        ThumbnailTemplate(
            id = "tpl_new_update",
            title = "NEW UPDATE!",
            category = "general",
            gameTag = "All Games",
            description = "Clean announcement thumbnail for patch notes, DLCs and seasons.",
            badge = "NEW",
            backgroundColor = 0xFF0F1A24,
            backgroundGradient = "CYBER",
            layers = listOf(
                ThumbnailLayer(
                    id = "layer_stk_badge",
                    type = LayerType.STICKER,
                    name = "Alert",
                    stickerText = "SEASON 5 LAUNCH 🔥",
                    x = 640f,
                    y = 190f,
                    width = 420f,
                    height = 70f,
                    fillColor = 0xFFFF2A55,
                    textColor = 0xFFFFFFFF,
                    zIndex = 1
                ),
                ThumbnailLayer(
                    id = "layer_txt_update",
                    type = LayerType.TEXT,
                    name = "Main",
                    text = "NEW UPDATE!",
                    x = 640f,
                    y = 350f,
                    fontSize = 86f,
                    textColor = 0xFF00F0FF,
                    outlineColor = 0xFF052B3C,
                    outlineWidth = 7f,
                    effect = TextEffect.NEON,
                    zIndex = 2
                ),
                ThumbnailLayer(
                    id = "layer_txt_sub",
                    type = LayerType.TEXT,
                    name = "Details",
                    text = "EVERYTHING CHANGED!",
                    x = 640f,
                    y = 480f,
                    fontSize = 42f,
                    textColor = 0xFFFFE57F,
                    outlineColor = 0xFF000000,
                    outlineWidth = 4f,
                    effect = TextEffect.GOLD,
                    zIndex = 3
                )
            )
        ),
        ThumbnailTemplate(
            id = "tpl_secret_boss",
            title = "SECRET BOSS",
            category = "horror",
            gameTag = "Horror & Mystery",
            description = "Dark sinister aesthetic with eerie red typography and mystery warning.",
            badge = "SPOOKY",
            backgroundColor = 0xFF0A0005,
            backgroundGradient = "DARK_VOID",
            layers = listOf(
                ThumbnailLayer(
                    id = "layer_txt_dont",
                    type = LayerType.TEXT,
                    name = "Warning",
                    text = "DO NOT ENTER ROOM 404...",
                    x = 640f,
                    y = 230f,
                    fontSize = 36f,
                    textColor = 0xFFB0B0B0,
                    outlineColor = 0xFF000000,
                    outlineWidth = 3f,
                    zIndex = 1
                ),
                ThumbnailLayer(
                    id = "layer_txt_secret",
                    type = LayerType.TEXT,
                    name = "Title",
                    text = "SECRET BOSS",
                    x = 640f,
                    y = 380f,
                    fontSize = 84f,
                    textColor = 0xFFFF1144,
                    outlineColor = 0xFF2A0008,
                    outlineWidth = 8f,
                    effect = TextEffect.HORROR,
                    shadowColor = 0xFFFF0033,
                    shadowRadius = 25f,
                    zIndex = 2
                ),
                ThumbnailLayer(
                    id = "layer_stk_unseen",
                    type = LayerType.STICKER,
                    name = "Badge",
                    stickerText = "100% UNBEATEN 💀",
                    x = 640f,
                    y = 520f,
                    width = 400f,
                    height = 70f,
                    fillColor = 0xFF3D000E,
                    strokeColor = 0xFFFF1144,
                    textColor = 0xFFFF1144,
                    zIndex = 3
                )
            )
        ),
        ThumbnailTemplate(
            id = "tpl_insane_war",
            title = "INSANE WAR!",
            category = "stick_war",
            gameTag = "Stick War & Strategy",
            description = "Large scale battle thumbnail with exploding typography and gold highlights.",
            badge = "POPULAR",
            backgroundColor = 0xFF210F05,
            backgroundGradient = "FIRE",
            layers = listOf(
                ThumbnailLayer(
                    id = "layer_txt_army",
                    type = LayerType.TEXT,
                    name = "Army Size",
                    text = "10,000 SOLDIERS VS 1 GIANT",
                    x = 640f,
                    y = 220f,
                    fontSize = 38f,
                    textColor = 0xFFFFD600,
                    outlineColor = 0xFF000000,
                    outlineWidth = 4f,
                    zIndex = 1
                ),
                ThumbnailLayer(
                    id = "layer_txt_war",
                    type = LayerType.TEXT,
                    name = "Headline",
                    text = "INSANE WAR!",
                    x = 640f,
                    y = 370f,
                    fontSize = 92f,
                    textColor = 0xFFFF4500,
                    outlineColor = 0xFF3D1000,
                    outlineWidth = 8f,
                    effect = TextEffect.EXPLOSION,
                    shadowColor = 0xFFFF7700,
                    shadowRadius = 22f,
                    zIndex = 2
                ),
                ThumbnailLayer(
                    id = "layer_stk_noway",
                    type = LayerType.STICKER,
                    name = "Tag",
                    stickerText = "HE SURVIVED?! 🤯",
                    x = 640f,
                    y = 520f,
                    width = 380f,
                    height = 75f,
                    fillColor = 0xFFFF4500,
                    textColor = 0xFFFFFFFF,
                    zIndex = 3
                )
            )
        ),
        ThumbnailTemplate(
            id = "tpl_100_days",
            title = "100 DAYS SURVIVAL",
            category = "minecraft",
            gameTag = "Minecraft Hardcore",
            description = "Hardcore survival template with bold green emerald effects and counter badge.",
            badge = "HARDCORE",
            backgroundColor = 0xFF071F12,
            backgroundGradient = "NATURE",
            layers = listOf(
                ThumbnailLayer(
                    id = "layer_txt_hardcore",
                    type = LayerType.TEXT,
                    name = "Mode",
                    text = "HARDCORE WORLD",
                    x = 640f,
                    y = 220f,
                    fontSize = 36f,
                    textColor = 0xFFFF4444,
                    outlineColor = 0xFF000000,
                    outlineWidth = 4f,
                    zIndex = 1
                ),
                ThumbnailLayer(
                    id = "layer_txt_days",
                    type = LayerType.TEXT,
                    name = "Days",
                    text = "100 DAYS",
                    x = 640f,
                    y = 350f,
                    fontSize = 88f,
                    textColor = 0xFF00FF88,
                    outlineColor = 0xFF032B16,
                    outlineWidth = 8f,
                    effect = TextEffect.ICE,
                    zIndex = 2
                ),
                ThumbnailLayer(
                    id = "layer_txt_sub",
                    type = LayerType.TEXT,
                    name = "Goal",
                    text = "SURVIVING THE APOCALYPSE",
                    x = 640f,
                    y = 470f,
                    fontSize = 40f,
                    textColor = 0xFFFFFFFF,
                    outlineColor = 0xFF000000,
                    outlineWidth = 4f,
                    zIndex = 3
                )
            )
        ),
        ThumbnailTemplate(
            id = "tpl_gta_millionaire",
            title = "BECOMING A MILLIONAIRE",
            category = "gta",
            gameTag = "GTA Online",
            description = "Flashy money and heist theme with luxury gold typography and cash badges.",
            badge = "PRO",
            backgroundColor = 0xFF141400,
            backgroundGradient = "GOLD",
            layers = listOf(
                ThumbnailLayer(
                    id = "layer_txt_zero",
                    type = LayerType.TEXT,
                    name = "From",
                    text = "$0 TO $100,000,000",
                    x = 640f,
                    y = 240f,
                    fontSize = 44f,
                    textColor = 0xFF00FF88,
                    outlineColor = 0xFF000000,
                    outlineWidth = 4f,
                    zIndex = 1
                ),
                ThumbnailLayer(
                    id = "layer_txt_rich",
                    type = LayerType.TEXT,
                    name = "Title",
                    text = "RICHEST PLAYER!",
                    x = 640f,
                    y = 380f,
                    fontSize = 82f,
                    textColor = 0xFFFFD700,
                    outlineColor = 0xFF2D2300,
                    outlineWidth = 8f,
                    effect = TextEffect.GOLD,
                    zIndex = 2
                ),
                ThumbnailLayer(
                    id = "layer_stk_heist",
                    type = LayerType.STICKER,
                    name = "Method",
                    stickerText = "FASTEST SOLO METHOD 💰",
                    x = 640f,
                    y = 510f,
                    width = 460f,
                    height = 70f,
                    fillColor = 0xFFFFD700,
                    textColor = 0xFF000000,
                    zIndex = 3
                )
            )
        )
    )
}
