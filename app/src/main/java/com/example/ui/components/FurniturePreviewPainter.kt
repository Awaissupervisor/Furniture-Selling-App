package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun FurniturePreview(
    stylePattern: Int,
    tintColorHex: String,
    modifier: Modifier = Modifier
) {
    // Convert hex safely
    val parsedColor = try {
        Color(android.graphics.Color.parseColor(tintColorHex))
    } catch (e: Exception) {
        Color(0xFF8B5A2B) // Default walnut brown
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .padding(12.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val centerX = width / 2
            val centerY = height / 2

            // Base ground shadow line
            drawOval(
                color = Color.Black.copy(alpha = 0.1f),
                topLeft = Offset(width * 0.15f, height * 0.82f),
                size = Size(width * 0.7f, height * 0.08f)
            )

            when (stylePattern) {
                1 -> { // Nordic Dining Bench
                    // Plinth thickness
                    val topHeight = height * 0.12f
                    val topY = centerY - topHeight / 2

                    // Draw 4 splayed legs
                    val legWidth = width * 0.05f
                    val legHeight = height * 0.45f
                    // Front Left Leg
                    drawLine(
                        color = parsedColor,
                        start = Offset(width * 0.25f, topY + topHeight),
                        end = Offset(width * 0.20f, topY + topHeight + legHeight),
                        strokeWidth = legWidth
                    )
                    // Back Left Leg
                    drawLine(
                        color = parsedColor.copy(alpha = 0.7f),
                        start = Offset(width * 0.29f, topY + topHeight),
                        end = Offset(width * 0.27f, topY + topHeight + legHeight),
                        strokeWidth = legWidth
                    )
                    // Front Right Leg
                    drawLine(
                        color = parsedColor,
                        start = Offset(width * 0.75f, topY + topHeight),
                        end = Offset(width * 0.80f, topY + topHeight + legHeight),
                        strokeWidth = legWidth
                    )
                    // Back Right Leg
                    drawLine(
                        color = parsedColor.copy(alpha = 0.7f),
                        start = Offset(width * 0.71f, topY + topHeight),
                        end = Offset(width * 0.73f, topY + topHeight + legHeight),
                        strokeWidth = legWidth
                    )

                    // Draw bench seat (rectangular plank with rounded corners)
                    drawRoundRect(
                        color = parsedColor,
                        topLeft = Offset(width * 0.15f, topY),
                        size = Size(width * 0.7f, topHeight),
                        cornerRadius = CornerRadius(8f, 8f)
                    )

                    // Subtle wood grain accent lines
                    drawLine(
                        color = Color.White.copy(alpha = 0.25f),
                        start = Offset(width * 0.18f, topY + topHeight * 0.3f),
                        end = Offset(width * 0.82f, topY + topHeight * 0.3f),
                        strokeWidth = 3f
                    )
                }
                2 -> { // Mid-Century Walnut Lounge Chair
                    val bodyColor = parsedColor
                    val cushionColor = Color(0xFFD4A373) // Warm leather brown

                    // Draw angled chair base/frame
                    val framePath = Path().apply {
                        moveTo(width * 0.25f, height * 0.65f)
                        lineTo(width * 0.40f, height * 0.78f)
                        lineTo(width * 0.65f, height * 0.65f)
                        lineTo(width * 0.75f, height * 0.35f)
                    }
                    drawPath(
                        path = framePath,
                        color = bodyColor,
                        style = Stroke(width = 12f)
                    )

                    // Front splayed leg
                    drawLine(
                        color = bodyColor,
                        start = Offset(width * 0.40f, height * 0.78f),
                        end = Offset(width * 0.35f, height * 0.90f),
                        strokeWidth = 14f
                    )
                    // Back splayed leg
                    drawLine(
                        color = bodyColor,
                        start = Offset(width * 0.55f, height * 0.71f),
                        end = Offset(width * 0.65f, height * 0.90f),
                        strokeWidth = 14f
                    )

                    // Bottom main cushion (rounded seat)
                    drawRoundRect(
                        color = cushionColor,
                        topLeft = Offset(width * 0.25f, height * 0.58f),
                        size = Size(width * 0.42f, height * 0.12f),
                        cornerRadius = CornerRadius(14f, 14f)
                    )

                    // Backrest cushion (tilted block)
                    val backPath = Path().apply {
                        moveTo(width * 0.60f, height * 0.62f)
                        lineTo(width * 0.72f, height * 0.36f)
                        lineTo(width * 0.62f, height * 0.32f)
                        lineTo(width * 0.50f, height * 0.58f)
                        close()
                    }
                    drawPath(
                        path = backPath,
                        color = cushionColor
                    )
                }
                3 -> { // Japanese Coffee Table
                    val slabColor = parsedColor // Slate / Dark stone
                    val baseColor = Color(0xFF1E1E1E) // Slate base cylindrical plate

                    // Cylindrical monolithic pedestal columns
                    drawRect(
                        color = baseColor,
                        topLeft = Offset(width * 0.32f, height * 0.55f),
                        size = Size(width * 0.14f, height * 0.30f)
                    )
                    drawRect(
                        color = baseColor,
                        topLeft = Offset(width * 0.54f, height * 0.55f),
                        size = Size(width * 0.14f, height * 0.30f)
                    )

                    // Thick beautiful stone countertop slab
                    drawRoundRect(
                        color = slabColor,
                        topLeft = Offset(width * 0.15f, height * 0.40f),
                        size = Size(width * 0.70f, height * 0.15f),
                        cornerRadius = CornerRadius(12f, 12f)
                    )

                    // Surface texturing (slate lines)
                    drawLine(
                        color = Color.Black.copy(alpha = 0.2f),
                        start = Offset(width * 0.18f, height * 0.47f),
                        end = Offset(width * 0.82f, height * 0.47f),
                        strokeWidth = 4f
                    )
                }
                4 -> { // Velvet Bed Frame
                    val velvetColor = parsedColor
                    val linenColor = Color(0xFFF0EBE5) // Clean beige / white sheets

                    // Tall beautiful headboard behind bed
                    drawRoundRect(
                        color = velvetColor,
                        topLeft = Offset(width * 0.20f, height * 0.20f),
                        size = Size(width * 0.60f, height * 0.65f),
                        cornerRadius = CornerRadius(24f, 24f)
                    )

                    // Bed base platform
                    drawRoundRect(
                        color = velvetColor.copy(alpha = 0.9f),
                        topLeft = Offset(width * 0.16f, height * 0.62f),
                        size = Size(width * 0.68f, height * 0.20f),
                        cornerRadius = CornerRadius(12f, 12f)
                    )

                    // Soft bedding sheets layout
                    drawRoundRect(
                        color = linenColor,
                        topLeft = Offset(width * 0.22f, height * 0.50f),
                        size = Size(width * 0.56f, height * 0.15f),
                        cornerRadius = CornerRadius(6f, 6f)
                    )

                    // Fluffy dual pillows
                    drawRoundRect(
                        color = Color.White,
                        topLeft = Offset(width * 0.27f, height * 0.40f),
                        size = Size(width * 0.21f, height * 0.11f),
                        cornerRadius = CornerRadius(8f, 8f)
                    )
                    drawRoundRect(
                        color = Color.White,
                        topLeft = Offset(width * 0.52f, height * 0.40f),
                        size = Size(width * 0.21f, height * 0.11f),
                        cornerRadius = CornerRadius(8f, 8f)
                    )

                    // Accent blanket drape
                    drawRoundRect(
                        color = velvetColor.copy(alpha = 0.5f),
                        topLeft = Offset(width * 0.30f, height * 0.57f),
                        size = Size(width * 0.40f, height * 0.10f),
                        cornerRadius = CornerRadius(4f, 4f)
                    )
                }
                5 -> { // Bauhaus Cantilever Desk Chair
                    val leatherColor = parsedColor
                    val chromeColor = Color(0xFFCCCCCC) // Mirror steel silver

                    // Continuous tubular chrome base cantilever line
                    val tubePath = Path().apply {
                        moveTo(width * 0.35f, height * 0.84f)
                        lineTo(width * 0.65f, height * 0.84f) // Ground track
                        lineTo(width * 0.62f, height * 0.58f) // Forward cant
                        lineTo(width * 0.32f, height * 0.58f) // Under seat plate
                        lineTo(width * 0.30f, height * 0.28f) // Backrest stick
                    }
                    drawPath(
                        path = tubePath,
                        color = chromeColor,
                        style = Stroke(width = 14f)
                    )

                    // Hanging Back leather sling
                    drawRoundRect(
                        color = leatherColor,
                        topLeft = Offset(width * 0.29f, height * 0.32f),
                        size = Size(width * 0.08f, height * 0.26f),
                        cornerRadius = CornerRadius(8f, 8f)
                    )

                    // Hanging Seat leather flap
                    drawRoundRect(
                        color = leatherColor,
                        topLeft = Offset(width * 0.31f, height * 0.54f),
                        size = Size(width * 0.31f, height * 0.08f),
                        cornerRadius = CornerRadius(8f, 8f)
                    )
                }
                6 -> { // Cedar Outdoor Sofa Set
                    val woodColor = parsedColor
                    val pillowColor = Color(0xFFFAFBF9) // Cloud white sand cushions

                    // Redwood slat frame sides
                    drawRoundRect(
                        color = woodColor,
                        topLeft = Offset(width * 0.14f, height * 0.50f),
                        size = Size(width * 0.72f, height * 0.32f),
                        cornerRadius = CornerRadius(10f, 10f)
                    )

                    // Cozy wide cotton cushions
                    drawRoundRect(
                        color = pillowColor,
                        topLeft = Offset(width * 0.18f, height * 0.54f),
                        size = Size(width * 0.29f, height * 0.22f),
                        cornerRadius = CornerRadius(12f, 12f)
                    )
                    drawRoundRect(
                        color = pillowColor,
                        topLeft = Offset(width * 0.53f, height * 0.54f),
                        size = Size(width * 0.29f, height * 0.22f),
                        cornerRadius = CornerRadius(12f, 12f)
                    )

                    // Wood arm slats
                    drawRoundRect(
                        color = woodColor,
                        topLeft = Offset(width * 0.10f, height * 0.46f),
                        size = Size(width * 0.10f, height * 0.36f),
                        cornerRadius = CornerRadius(6f, 6f)
                    )
                    drawRoundRect(
                        color = woodColor,
                        topLeft = Offset(width * 0.80f, height * 0.46f),
                        size = Size(width * 0.10f, height * 0.36f),
                        cornerRadius = CornerRadius(6f, 6f)
                    )
                }
                else -> { // Default stylish cabinet console (listed by user)
                    val primaryWood = parsedColor
                    val goldBeadColor = Color(0xFFD4AF37) // Golden knobs/brass legs

                    // Legs
                    drawRect(
                        color = primaryWood.copy(alpha = 0.8f),
                        topLeft = Offset(width * 0.30f, height * 0.70f),
                        size = Size(width * 0.06f, height * 0.15f)
                    )
                    drawRect(
                        color = primaryWood.copy(alpha = 0.8f),
                        topLeft = Offset(width * 0.64f, height * 0.70f),
                        size = Size(width * 0.06f, height * 0.15f)
                    )

                    // Cabinet Main Carcass
                    drawRoundRect(
                        color = primaryWood,
                        topLeft = Offset(width * 0.22f, height * 0.30f),
                        size = Size(width * 0.56f, height * 0.42f),
                        cornerRadius = CornerRadius(12f, 12f)
                    )

                    // Dual drawer separators
                    drawLine(
                        color = Color.Black.copy(alpha = 0.15f),
                        start = Offset(width * 0.50f, height * 0.32f),
                        end = Offset(width * 0.50f, height * 0.68f),
                        strokeWidth = 4f
                    )

                    // Gilded handles (gold circles)
                    drawCircle(
                        color = goldBeadColor,
                        radius = 8f,
                        center = Offset(width * 0.44f, height * 0.50f)
                    )
                    drawCircle(
                        color = goldBeadColor,
                        radius = 8f,
                        center = Offset(width * 0.56f, height * 0.50f)
                    )
                }
            }
        }
    }
}
