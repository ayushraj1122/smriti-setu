package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FontSizeScale

@Composable
fun AccessibleButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    emoji: String? = null,
    isPrimary: Boolean = true,
    isOutlined: Boolean = false,
    highContrast: Boolean = false,
    fontSizeScale: FontSizeScale = FontSizeScale.STANDARD,
    largeButtonMode: Boolean = true,
    enabled: Boolean = true,
    testTag: String = "accessible_button"
) {
    val minHeight = if (largeButtonMode) 58.dp else 50.dp
    val textBaseSp = if (largeButtonMode) 18f else 16f
    val computedSp = (textBaseSp * fontSizeScale.scale).sp

    val primaryBg = if (highContrast) Color(0xFFFACC15) else com.example.ui.theme.VibrantBluePrimary
    val primaryText = if (highContrast) Color.Black else Color.White
    val secondaryBg = if (highContrast) Color(0xFF1E293B) else com.example.ui.theme.VibrantOrientationBg
    val secondaryText = if (highContrast) Color(0xFF38BDF8) else com.example.ui.theme.VibrantOrientationText

    if (isOutlined) {
        OutlinedButton(
            onClick = onClick,
            enabled = enabled,
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(
                width = if (highContrast) 2.5.dp else 1.5.dp,
                color = if (highContrast) Color(0xFFFACC15) else com.example.ui.theme.VibrantBluePrimary
            ),
            modifier = modifier
                .fillMaxWidth()
                .heightIn(min = minHeight)
                .testTag(testTag)
        ) {
            ButtonContent(
                text = text,
                icon = icon,
                emoji = emoji,
                textColor = if (highContrast) Color(0xFFFACC15) else com.example.ui.theme.VibrantBluePrimary,
                fontSize = computedSp
            )
        }
    } else {
        Button(
            onClick = onClick,
            enabled = enabled,
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isPrimary) primaryBg else secondaryBg,
                contentColor = if (isPrimary) primaryText else secondaryText
            ),
            border = if (highContrast) BorderStroke(2.dp, Color.White) else null,
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
            modifier = modifier
                .fillMaxWidth()
                .heightIn(min = minHeight)
                .testTag(testTag)
        ) {
            ButtonContent(
                text = text,
                icon = icon,
                emoji = emoji,
                textColor = if (isPrimary) primaryText else secondaryText,
                fontSize = computedSp
            )
        }
    }
}

@Composable
private fun ButtonContent(
    text: String,
    icon: ImageVector?,
    emoji: String?,
    textColor: Color,
    fontSize: androidx.compose.ui.unit.TextUnit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        if (!emoji.isNullOrBlank()) {
            Text(text = emoji, fontSize = (fontSize.value + 4).sp)
            Spacer(modifier = Modifier.width(10.dp))
        } else if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
        }
        Text(
            text = text,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold,
            color = textColor,
            textAlign = TextAlign.Center
        )
    }
}
