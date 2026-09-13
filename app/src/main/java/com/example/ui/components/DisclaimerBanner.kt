package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FontSizeScale
import com.example.i18n.StringsProvider

@Composable
fun DisclaimerBanner(
    currentLanguage: String,
    highContrast: Boolean = false,
    fontSizeScale: FontSizeScale = FontSizeScale.STANDARD,
    modifier: Modifier = Modifier
) {
    val bg = if (highContrast) Color(0xFF1E293B) else com.example.ui.theme.VibrantSurfaceVariant
    val border = if (highContrast) Color(0xFF38BDF8) else com.example.ui.theme.VibrantBorder
    val textColor = if (highContrast) Color(0xFFF8FAFC) else com.example.ui.theme.VibrantTextPrimary
    val iconTint = if (highContrast) Color(0xFF38BDF8) else com.example.ui.theme.VibrantBluePrimary

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = bg,
        border = BorderStroke(1.dp, border)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Medical Disclaimer Note",
                tint = iconTint,
                modifier = Modifier
                    .size(22.dp)
                    .padding(top = 2.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = "Cognitive Activity Notice",
                    fontWeight = FontWeight.Bold,
                    fontSize = (13f * fontSizeScale.scale).sp,
                    color = textColor
                )
                Text(
                    text = StringsProvider.get("app_disclaimer", currentLanguage),
                    fontSize = (12f * fontSizeScale.scale).sp,
                    color = textColor.copy(alpha = 0.9f),
                    lineHeight = 16.sp
                )
            }
        }
    }
}
