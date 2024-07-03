package com.example.appbannoithat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.appbannoithat.ViewModel.ViewModel
import com.example.appbannoithat.component.ImageSlider
import com.example.appbannoithat.nav.navController
import com.example.appbannoithat.ui.theme.AppBanNoiThatTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            navController(navController)
        }
    }
}

@Composable
fun ShowHI(viewModel: ViewModel) {
    val imageUrlsState by viewModel.slide.collectAsState()
    val imageUrls = imageUrlsState?.map { it.url } ?: emptyList() // Convert to list of URLs

    LaunchedEffect(Unit) {
        viewModel.getSlideshow()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        ImageSlider(imageUrls = imageUrls)
    }
}
