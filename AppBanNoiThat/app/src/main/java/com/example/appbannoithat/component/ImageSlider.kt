package com.example.appbannoithat.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .padding(top = 195.dp)
                .align(Alignment.Center),
            indicatorShape = RoundedCornerShape(50)
        )
    }
}