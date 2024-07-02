package com.example.appbannoithat.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun CustomButton(
    onClick: () -> Unit,
    buttonText: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(170.dp)
            .height(50.dp)
            .background(color = Color.Black)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black)
        )
        Text(
            text = buttonText,
            color = Color.White,
            modifier = Modifier
                .padding(12.dp)
                .align(Alignment.Center),
            fontWeight = FontWeight.Bold,
        )
    }
}