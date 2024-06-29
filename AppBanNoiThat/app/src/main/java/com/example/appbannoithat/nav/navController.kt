package com.example.appbannoithat.nav

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.appbannoithat.Screen.ListNoiThat
import com.example.appbannoithat.Screen.TrangChu
import com.example.appbannoithat.ViewModel.ViewModel

@Composable
fun navController (navController: NavHostController) {
    val viewModel: ViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.TrangChu.route
    ){
        composable(Screen.TrangChu.route){ TrangChu(navController, viewModel) }
//        composable(Screen.ListNoiThat.route){ ListNoiThat(navController, viewModel) }
        composable("listNoiThat/{id}"){ backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            if (id != null) {
                ListNoiThat(navController, viewModel, id)
            }
        }
//        composable(Screen.ForYou.route){ ForYou(navController, viewModel) }

    }
}