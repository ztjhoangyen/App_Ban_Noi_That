package com.example.appbannoithat.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.pager.*
import kotlinx.coroutines.delay

@Composable
fun ImageSlider(imageUrls: List<String>) {
    val pagerState = rememberPagerState()

    LaunchedEffect(imageUrls) {
        while (true) {
            delay(3000)
            if (imageUrls.isNotEmpty()) {
                pagerState.animateScrollToPage((pagerState.currentPage + 1) % imageUrls.size)
            }
        }
    }

    Box(
        modifier = Modifier
            .height(200.dp)
    ) {
        HorizontalPager(
            state = pagerState,
            count = imageUrls.size
        ) { page ->
            Box(
                modifier = Modifier
                    .height(200.dp), contentAlignment = Alignment.Center
            ) {
                val painter: Painter = rememberAsyncImagePainter(
                    model = imageUrls[page],
                )

                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }
        CustomHorizontalPagerIndicator(pagerState)
    }
}

@Composable
fun CustomHorizontalPagerIndicator(pagerState: PagerState) {
    Row(
        modifier = Modifier
            .padding(top = 175.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pagerState.pageCount) { page ->
            val isSelected = pagerState.currentPage == page
            val indicatorHeight = 10.dp
            val width = if (isSelected) 30.dp else 10.dp
            val color = if (isSelected) Color.Black else Color.Gray

            Canvas(
                modifier = Modifier
                    .size(width, indicatorHeight)
                    .padding(horizontal = 1.dp)
            ) {
                drawRoundRect(
                    color = color,
                    cornerRadius = CornerRadius(50f),
                    topLeft = Offset(0f, 0f),
                    size = Size(width.toPx(), indicatorHeight.toPx()),
                )
            }
            if (page < pagerState.pageCount - 1) {
                Spacer(modifier = Modifier.width(10.dp))
            }
        }
    }
}