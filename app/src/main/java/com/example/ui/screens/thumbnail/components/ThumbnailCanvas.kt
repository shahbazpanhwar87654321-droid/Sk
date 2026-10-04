package com.example.ui.screens.thumbnail.components

import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.domain.model.LayerType
import com.example.domain.model.ShapeType
import com.example.domain.model.TextEffect
import com.example.domain.model.ThumbnailDesign
import com.example.domain.model.ThumbnailLayer
import com.example.ui.theme.BorderNeon
import com.example.ui.theme.NeonCyan
import kotlin.math.roundToInt

@Composable
fun ThumbnailCanvas(
    design: ThumbnailDesign,
    selectedLayerId: String?,
    onSelectLayer: (String?) -> Unit,
    onMoveLayer: (Float, Float) -> Unit,
    onScaleLayer: (Float) -> Unit,
    onRotateLayer: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
            .clip(RoundedCornerShape(8.dp))
            .border(1.5.dp, BorderNeon, RoundedCornerShape(8.dp))
            .testTag("thumbnail_canvas")
    ) {
        val canvasWidthPx = constraints.maxWidth.toFloat()
        val canvasHeightPx = constraints.maxHeight.toFloat()

        // Scale factors relative to native 1280 x 720
        val scaleX = canvasWidthPx / 1280f
        val scaleY = canvasHeightPx / 720f

        val sortedLayers = remember(design.layers) {
            design.layers.filter { it.isVisible }.sortedBy { it.zIndex }
        }

        val backgroundBrush = remember(design.backgroundGradient, design.backgroundColor) {
            when (design.backgroundGradient) {
                "FIRE" -> Brush.verticalGradient(
                    listOf(Color(0xFF3D0A0A), Color(0xFFB22222), Color(0xFFFF4500))
                )
                "CYBER" -> Brush.linearGradient(
                    listOf(Color(0xFF09101E), Color(0xFF15243A), Color(0xFF0B3B60))
                )
                "GOLD" -> Brush.linearGradient(
                    listOf(Color(0xFF1F1600), Color(0xFF4A3600), Color(0xFF1A1200))
                )
                "NATURE" -> Brush.verticalGradient(
                    listOf(Color(0xFF05190D), Color(0xFF0F3D24), Color(0xFF03130A))
                )
                else -> Brush.verticalGradient(
                    listOf(Color(design.backgroundColor), Color(0xFF070B12))
                )
            }
        }

        // Custom Canvas drawing
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .clipToBounds()
                .background(backgroundBrush)
                .pointerInput(selectedLayerId) {
                    detectTransformGestures { _, pan, zoom, rotation ->
                        if (selectedLayerId != null) {
                            if (pan != Offset.Zero) {
                                onMoveLayer(pan.x / scaleX, pan.y / scaleY)
                            }
                            if (zoom != 1f) {
                                onScaleLayer(zoom)
                            }
                            if (rotation != 0f) {
                                onRotateLayer(rotation)
                            }
                        }
                    }
                }
                .pointerInput(sortedLayers) {
                    detectTapGestures { tapOffset ->
                        val tapNativeX = tapOffset.x / scaleX
                        val tapNativeY = tapOffset.y / scaleY

                        // Find topmost tapped layer
                        val tappedLayer = sortedLayers.reversed().firstOrNull { layer ->
                            val dx = kotlin.math.abs(tapNativeX - layer.x)
                            val dy = kotlin.math.abs(tapNativeY - layer.y)
                            val halfW = (layer.width * layer.scale) / 2f
                            val halfH = (layer.height * layer.scale) / 2f
                            dx <= halfW + 40f && dy <= halfH + 40f
                        }
                        onSelectLayer(tappedLayer?.id)
                    }
                }
        ) {
            // Draw all layers
            for (layer in sortedLayers) {
                val lx = layer.x * scaleX
                val ly = layer.y * scaleY

                translate(left = lx, top = ly) {
                    rotate(degrees = layer.rotation) {
                        scale(scale = layer.scale) {
                            when (layer.type) {
                                LayerType.TEXT -> drawComposeText(layer, scaleX)
                                LayerType.SHAPE -> drawComposeShape(layer, scaleX)
                                LayerType.STICKER -> drawComposeSticker(layer, scaleX)
                                LayerType.IMAGE -> {
                                    // Image placeholder / bounding frame drawn in Canvas
                                    drawComposeImageFrame(layer, scaleX)
                                }
                            }

                            // Draw Selection Bounding Box if active
                            if (layer.id == selectedLayerId) {
                                drawSelectionBounds(layer, scaleX)
                            }
                        }
                    }
                }
            }
        }

        // Overlay for Image layers using Coil AsyncImage
        for (layer in sortedLayers) {
            if (layer.type == LayerType.IMAGE && layer.imageUri != null) {
                val imgW = (layer.width * layer.scale * scaleX).dp
                val imgH = (layer.height * layer.scale * scaleY).dp
                val offsetX = (layer.x * scaleX - (layer.width * layer.scale * scaleX) / 2f).roundToInt()
                val offsetY = (layer.y * scaleY - (layer.height * layer.scale * scaleY) / 2f).roundToInt()

                Box(
                    modifier = Modifier
                        .offset { IntOffset(offsetX, offsetY) }
                        .size(imgW, imgH)
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(Uri.parse(layer.imageUri))
                            .crossfade(true)
                            .build(),
                        contentDescription = "Layer image",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(8.dp))
                    )
                }
            }
        }
    }
}

private fun DrawScope.drawComposeText(layer: ThumbnailLayer, scale: Float) {
    val nativeCanvas = drawContext.canvas.nativeCanvas
    val paint = android.graphics.Paint().apply {
        isAntiAlias = true
        textSize = layer.fontSize * scale
        typeface = android.graphics.Typeface.create(android.graphics.Typeface.DEFAULT, android.graphics.Typeface.BOLD)
        textAlign = android.graphics.Paint.Align.CENTER
        color = layer.textColor.toInt()
        alpha = (layer.opacity * 255).toInt()
    }

    // Effect styling
    when (layer.effect) {
        TextEffect.FIRE -> {
            paint.color = android.graphics.Color.parseColor("#FFD600")
            paint.setShadowLayer(16f * scale, 0f, 4f * scale, android.graphics.Color.parseColor("#FF3300"))
        }
        TextEffect.NEON -> {
            paint.color = android.graphics.Color.parseColor("#00F0FF")
            paint.setShadowLayer(20f * scale, 0f, 0f, android.graphics.Color.parseColor("#00F0FF"))
        }
        TextEffect.GOLD -> {
            paint.color = android.graphics.Color.parseColor("#FFE066")
            paint.setShadowLayer(14f * scale, 0f, 4f * scale, android.graphics.Color.parseColor("#FF9900"))
        }
        TextEffect.ELECTRIC -> {
            paint.color = android.graphics.Color.parseColor("#00FF88")
            paint.setShadowLayer(18f * scale, 0f, 0f, android.graphics.Color.parseColor("#00F0FF"))
        }
        TextEffect.HORROR -> {
            paint.color = android.graphics.Color.parseColor("#FF1144")
            paint.setShadowLayer(22f * scale, 0f, 4f * scale, android.graphics.Color.parseColor("#660011"))
        }
        else -> {
            if (layer.shadowColor != null && layer.shadowRadius > 0) {
                paint.setShadowLayer(layer.shadowRadius * scale, 2f * scale, 4f * scale, layer.shadowColor.toInt())
            }
        }
    }

    // Stroke Outline
    if (layer.outlineColor != null && layer.outlineWidth > 0) {
        val strokePaint = android.graphics.Paint().apply {
            isAntiAlias = true
            textSize = layer.fontSize * scale
            typeface = android.graphics.Typeface.create(android.graphics.Typeface.DEFAULT, android.graphics.Typeface.BOLD)
            textAlign = android.graphics.Paint.Align.CENTER
            style = android.graphics.Paint.Style.STROKE
            strokeWidth = layer.outlineWidth * 2f * scale
            color = layer.outlineColor.toInt()
            alpha = (layer.opacity * 255).toInt()
        }
        nativeCanvas.drawText(layer.text, 0f, (layer.fontSize * scale) * 0.35f, strokePaint)
    }

    nativeCanvas.drawText(layer.text, 0f, (layer.fontSize * scale) * 0.35f, paint)
}

private fun DrawScope.drawComposeShape(layer: ThumbnailLayer, scale: Float) {
    val w = layer.width * scale
    val h = layer.height * scale
    val topLeft = Offset(-w / 2f, -h / 2f)
    val size = Size(w, h)

    when (layer.shapeType) {
        ShapeType.ROUNDED_RECTANGLE -> {
            drawRoundRect(
                color = Color(layer.fillColor).copy(alpha = layer.opacity),
                topLeft = topLeft,
                size = size,
                cornerRadius = CornerRadius(14f * scale, 14f * scale),
                style = Fill
            )
            drawRoundRect(
                color = Color(layer.strokeColor).copy(alpha = layer.opacity),
                topLeft = topLeft,
                size = size,
                cornerRadius = CornerRadius(14f * scale, 14f * scale),
                style = Stroke(width = layer.strokeWidth * scale)
            )
        }
        ShapeType.CIRCLE -> {
            val radius = minOf(w, h) / 2f
            drawCircle(
                color = Color(layer.fillColor).copy(alpha = layer.opacity),
                radius = radius,
                center = Offset.Zero,
                style = Fill
            )
            drawCircle(
                color = Color(layer.strokeColor).copy(alpha = layer.opacity),
                radius = radius,
                center = Offset.Zero,
                style = Stroke(width = layer.strokeWidth * scale)
            )
        }
        else -> {
            drawRect(
                color = Color(layer.fillColor).copy(alpha = layer.opacity),
                topLeft = topLeft,
                size = size,
                style = Fill
            )
            drawRect(
                color = Color(layer.strokeColor).copy(alpha = layer.opacity),
                topLeft = topLeft,
                size = size,
                style = Stroke(width = layer.strokeWidth * scale)
            )
        }
    }
}

private fun DrawScope.drawComposeSticker(layer: ThumbnailLayer, scale: Float) {
    val w = layer.width * scale
    val h = layer.height * scale
    val topLeft = Offset(-w / 2f, -h / 2f)

    drawRoundRect(
        color = Color(layer.fillColor).copy(alpha = layer.opacity),
        topLeft = topLeft,
        size = Size(w, h),
        cornerRadius = CornerRadius(10f * scale, 10f * scale),
        style = Fill
    )
    drawRoundRect(
        color = Color(layer.strokeColor).copy(alpha = layer.opacity),
        topLeft = topLeft,
        size = Size(w, h),
        cornerRadius = CornerRadius(10f * scale, 10f * scale),
        style = Stroke(width = 2.5f * scale)
    )

    val nativeCanvas = drawContext.canvas.nativeCanvas
    val textPaint = android.graphics.Paint().apply {
        isAntiAlias = true
        textSize = 22f * scale
        typeface = android.graphics.Typeface.create(android.graphics.Typeface.DEFAULT, android.graphics.Typeface.BOLD)
        textAlign = android.graphics.Paint.Align.CENTER
        color = layer.textColor.toInt()
    }
    nativeCanvas.drawText(layer.stickerText, 0f, 8f * scale, textPaint)
}

private fun DrawScope.drawComposeImageFrame(layer: ThumbnailLayer, scale: Float) {
    if (layer.imageUri == null) {
        val w = layer.width * scale
        val h = layer.height * scale
        drawRoundRect(
            color = Color(0x3300F0FF),
            topLeft = Offset(-w / 2f, -h / 2f),
            size = Size(w, h),
            cornerRadius = CornerRadius(8f * scale, 8f * scale),
            style = Fill
        )
    }
}

private fun DrawScope.drawSelectionBounds(layer: ThumbnailLayer, scale: Float) {
    val w = (layer.width * 1.15f) * scale
    val h = (layer.height * 1.25f) * scale
    val topLeft = Offset(-w / 2f, -h / 2f)

    // Dashed Neon cyan selection border
    drawRoundRect(
        color = NeonCyan,
        topLeft = topLeft,
        size = Size(w, h),
        cornerRadius = CornerRadius(8f, 8f),
        style = Stroke(
            width = 2.dp.toPx(),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
        )
    )

    // Corner handle points
    val cornerRadius = 5.dp.toPx()
    drawCircle(color = NeonCyan, radius = cornerRadius, center = topLeft)
    drawCircle(color = NeonCyan, radius = cornerRadius, center = Offset(w / 2f, -h / 2f))
    drawCircle(color = NeonCyan, radius = cornerRadius, center = Offset(-w / 2f, h / 2f))
    drawCircle(color = NeonCyan, radius = cornerRadius, center = Offset(w / 2f, h / 2f))
}
