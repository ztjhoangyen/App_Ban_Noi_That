package com.example.appbannoithat.Screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.appbannoithat.ShowHI
import com.example.appbannoithat.ViewModel.ViewModel

@Composable
fun Home(it: PaddingValues, viewModel: ViewModel, navController: NavController) {
    Column (
        modifier = Modifier.padding(it)
    ){
        ShowHI(viewModel)
    }
}