package com.example.export

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.example.domain.model.LayerType
import com.example.domain.model.ShapeType
import com.example.domain.model.TextEffect
import com.example.domain.model.ThumbnailDesign
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

object ImageExporter {

    enum class ExportFormat(val extension: String, val mimeType: String, val compressFormat: Bitmap.CompressFormat) {
        JPG("jpg", "image/jpeg", Bitmap.CompressFormat.JPEG),
        PNG("png", "image/png", Bitmap.CompressFormat.PNG)
    }

    suspend fun renderAndExport(
        context: Context,
        design: ThumbnailDesign,
        width: Int = 1280,
        height: Int = 720,
        format: ExportFormat = ExportFormat.PNG,
        quality: Int = 100
    ): Result<Uri> = withContext(Dispatchers.IO) {
        try {
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)

            val scaleX = width / 1280f
            val scaleY = height / 720f

            // 1. Draw Background
            val bgPaint = Paint().apply { isAntiAlias = true }
            when (design.backgroundGradient) {
                "FIRE" -> {
                    bgPaint.shader = LinearGradient(
                        0f, 0f, 0f, height.toFloat(),
                        intArrayOf(Color.parseColor("#3D0A0A"), Color.parseColor("#B22222"), Color.parseColor("#FF4500")),
                        floatArrayOf(0f, 0.6f, 1f),
                        Shader.TileMode.CLAMP
                    )
                }
                "CYBER" -> {
                    bgPaint.shader = LinearGradient(
                        0f, 0f, width.toFloat(), height.toFloat(),
                        intArrayOf(Color.parseColor("#09101E"), Color.parseColor("#15243A"), Color.parseColor("#0B3B60")),
                        floatArrayOf(0f, 0.5f, 1f),
                        Shader.TileMode.CLAMP
                    )
                }
                "GOLD" -> {
                    bgPaint.shader = LinearGradient(
                        0f, 0f, width.toFloat(), 0f,
                        intArrayOf(Color.parseColor("#1F1600"), Color.parseColor("#4A3600"), Color.parseColor("#1A1200")),
                        floatArrayOf(0f, 0.5f, 1f),
                        Shader.TileMode.CLAMP
                    )
                }
                "NATURE" -> {
                    bgPaint.shader = LinearGradient(
                        0f, 0f, 0f, height.toFloat(),
                        intArrayOf(Color.parseColor("#05190D"), Color.parseColor("#0F3D24"), Color.parseColor("#03130A")),
                        floatArrayOf(0f, 0.7f, 1f),
                        Shader.TileMode.CLAMP
                    )
                }
                else -> {
                    bgPaint.color = design.backgroundColor.toInt()
                }
            }
            canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)

            // 2. Draw Layers sorted by zIndex
            val sortedLayers = design.layers.filter { it.isVisible }.sortedBy { it.zIndex }

            for (layer in sortedLayers) {
                canvas.save()
                val lx = layer.x * scaleX
                val ly = layer.y * scaleY

                canvas.translate(lx, ly)
                canvas.rotate(layer.rotation)
                canvas.scale(layer.scale, layer.scale)

                when (layer.type) {
                    LayerType.TEXT -> {
                        drawTextLayer(canvas, layer, scaleX)
                    }
                    LayerType.SHAPE -> {
                        drawShapeLayer(canvas, layer, scaleX)
                    }
                    LayerType.STICKER -> {
                        drawStickerLayer(canvas, layer, scaleX)
                    }
                    LayerType.IMAGE -> {
                        drawImageLayer(context, canvas, layer, scaleX)
                    }
                }

                canvas.restore()
            }

            // 3. Apply Filters
            val filters = design.filterSettings
            if (filters.brightness != 1f || filters.contrast != 1f || filters.saturation != 1f) {
                applyColorFilter(bitmap, filters.brightness, filters.contrast, filters.saturation)
            }

            // 4. Save to MediaStore
            val fileName = "GamingThumbnail_${System.currentTimeMillis()}.${format.extension}"
            val savedUri = saveBitmapToGallery(context, bitmap, fileName, format, quality)
            Result.success(savedUri)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    private fun drawTextLayer(canvas: Canvas, layer: com.example.domain.model.ThumbnailLayer, scale: Float) {
        val fontSize = layer.fontSize * scale
        val typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)

        val fillPaint = Paint().apply {
            isAntiAlias = true
            textSize = fontSize
            this.typeface = typeface
            textAlign = Paint.Align.CENTER
            color = layer.textColor.toInt()
            alpha = (layer.opacity * 255).toInt()
        }

        // Apply text effects
        when (layer.effect) {
            TextEffect.FIRE -> {
                fillPaint.color = Color.parseColor("#FFD600")
                fillPaint.setShadowLayer(16f * scale, 0f, 4f * scale, Color.parseColor("#FF3300"))
            }
            TextEffect.NEON -> {
                fillPaint.color = Color.parseColor("#00F0FF")
                fillPaint.setShadowLayer(20f * scale, 0f, 0f, Color.parseColor("#00F0FF"))
            }
            TextEffect.GOLD -> {
                fillPaint.color = Color.parseColor("#FFE066")
                fillPaint.setShadowLayer(14f * scale, 0f, 4f * scale, Color.parseColor("#FF9900"))
            }
            TextEffect.ELECTRIC -> {
                fillPaint.color = Color.parseColor("#00FF88")
                fillPaint.setShadowLayer(18f * scale, 0f, 0f, Color.parseColor("#00F0FF"))
            }
            TextEffect.HORROR -> {
                fillPaint.color = Color.parseColor("#FF1144")
                fillPaint.setShadowLayer(22f * scale, 0f, 4f * scale, Color.parseColor("#660011"))
            }
            else -> {
                if (layer.shadowColor != null && layer.shadowRadius > 0) {
                    fillPaint.setShadowLayer(layer.shadowRadius * scale, 2f * scale, 4f * scale, layer.shadowColor.toInt())
                }
            }
        }

        // Outline / Stroke
        if (layer.outlineColor != null && layer.outlineWidth > 0) {
            val strokePaint = Paint().apply {
                isAntiAlias = true
                textSize = fontSize
                this.typeface = typeface
                textAlign = Paint.Align.CENTER
                style = Paint.Style.STROKE
                strokeWidth = layer.outlineWidth * 2f * scale
                color = layer.outlineColor.toInt()
                alpha = (layer.opacity * 255).toInt()
            }
            canvas.drawText(layer.text, 0f, fontSize * 0.35f, strokePaint)
        }

        canvas.drawText(layer.text, 0f, fontSize * 0.35f, fillPaint)
    }

    private fun drawShapeLayer(canvas: Canvas, layer: com.example.domain.model.ThumbnailLayer, scale: Float) {
        val w = layer.width * scale
        val h = layer.height * scale
        val rect = RectF(-w / 2f, -h / 2f, w / 2f, h / 2f)

        val fillPaint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = layer.fillColor.toInt()
            alpha = (layer.opacity * 255).toInt()
        }

        val strokePaint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = layer.strokeWidth * scale
            color = layer.strokeColor.toInt()
            alpha = (layer.opacity * 255).toInt()
        }

        when (layer.shapeType) {
            ShapeType.ROUNDED_RECTANGLE -> {
                val radius = 16f * scale
                canvas.drawRoundRect(rect, radius, radius, fillPaint)
                canvas.drawRoundRect(rect, radius, radius, strokePaint)
            }
            ShapeType.CIRCLE -> {
                val radius = minOf(w, h) / 2f
                canvas.drawCircle(0f, 0f, radius, fillPaint)
                canvas.drawCircle(0f, 0f, radius, strokePaint)
            }
            else -> {
                canvas.drawRect(rect, fillPaint)
                canvas.drawRect(rect, strokePaint)
            }
        }
    }

    private fun drawStickerLayer(canvas: Canvas, layer: com.example.domain.model.ThumbnailLayer, scale: Float) {
        val w = layer.width * scale
        val h = layer.height * scale
        val rect = RectF(-w / 2f, -h / 2f, w / 2f, h / 2f)

        val bgPaint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = layer.fillColor.toInt()
            alpha = (layer.opacity * 255).toInt()
        }
        val borderPaint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = 3f * scale
            color = layer.strokeColor.toInt()
        }
        canvas.drawRoundRect(rect, 12f * scale, 12f * scale, bgPaint)
        canvas.drawRoundRect(rect, 12f * scale, 12f * scale, borderPaint)

        val textPaint = Paint().apply {
            isAntiAlias = true
            textSize = 24f * scale
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
            color = layer.textColor.toInt()
        }
        canvas.drawText(layer.stickerText, 0f, 8f * scale, textPaint)
    }

    private fun drawImageLayer(context: Context, canvas: Canvas, layer: com.example.domain.model.ThumbnailLayer, scale: Float) {
        val uriStr = layer.imageUri ?: return
        try {
            val uri = Uri.parse(uriStr)
            val inputStream = context.contentResolver.openInputStream(uri)
            val srcBitmap = android.graphics.BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            if (srcBitmap != null) {
                val targetW = layer.width * scale
                val targetH = layer.height * scale
                val dstRect = RectF(-targetW / 2f, -targetH / 2f, targetW / 2f, targetH / 2f)
                val paint = Paint().apply {
                    isAntiAlias = true
                    alpha = (layer.opacity * 255).toInt()
                }
                canvas.drawBitmap(srcBitmap, null, dstRect, paint)
                srcBitmap.recycle()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun applyColorFilter(bitmap: Bitmap, brightness: Float, contrast: Float, saturation: Float) {
        val canvas = Canvas(bitmap)
        val paint = Paint().apply { isAntiAlias = true }

        val satMatrix = ColorMatrix().apply { setSaturation(saturation) }
        val scale = contrast
        val translate = (1f - scale) * 128f + (brightness - 1f) * 255f

        val contrastMatrix = ColorMatrix(
            floatArrayOf(
                scale, 0f, 0f, 0f, translate,
                0f, scale, 0f, 0f, translate,
                0f, 0f, scale, 0f, translate,
                0f, 0f, 0f, 1f, 0f
            )
        )

        contrastMatrix.preConcat(satMatrix)
        paint.colorFilter = ColorMatrixColorFilter(contrastMatrix)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
    }

    private fun saveBitmapToGallery(
        context: Context,
        bitmap: Bitmap,
        fileName: String,
        format: ExportFormat,
        quality: Int
    ): Uri {
        val resolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, format.mimeType)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, "${Environment.DIRECTORY_PICTURES}/GamingCreatorStudio")
                put(MediaStore.MediaColumns.IS_PENDING, 1)
            }
        }

        val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            ?: throw IllegalStateException("Failed to create MediaStore entry")

        resolver.openOutputStream(imageUri)?.use { out: OutputStream ->
            bitmap.compress(format.compressFormat, quality, out)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            contentValues.clear()
            contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
            resolver.update(imageUri, contentValues, null, null)
        }

        return imageUri
    }
}
