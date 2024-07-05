package com.example.appbannoithat.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun tabHD (selectedTab: String, onTabSelected: (String) -> Unit){
    Row(
        modifier = Modifier.fillMaxWidth()
    ){
        listOf("Đang xử lý", "Đã xác nhận", "Đã hủy").forEach{
            Text(
                text = it,
                fontWeight = FontWeight.Bold,
                color = if (selectedTab == it) Color.Black else Color.Gray,
                modifier = Modifier
                    .clickable(
                        onClick = { onTabSelected(it) },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    )
                    .weight(1f)
                    .padding(8.dp)
            )
        }
    }
}