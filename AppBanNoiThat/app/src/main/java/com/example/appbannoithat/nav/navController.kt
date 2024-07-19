package com.example.appbannoithat.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.appbannoithat.MainActivity
import com.example.appbannoithat.Screen.Chat
import com.example.appbannoithat.Screen.DanhGia
import com.example.appbannoithat.Screen.DonHang
import com.example.appbannoithat.Screen.GioHang
import com.example.appbannoithat.Screen.HoaDon
import com.example.appbannoithat.Screen.ListNoiThat
import com.example.appbannoithat.Screen.Login
import com.example.appbannoithat.Screen.ManHinh
import com.example.appbannoithat.Screen.NguoiDungs
import com.example.appbannoithat.Screen.NoiThat
import com.example.appbannoithat.Screen.Register
import com.example.appbannoithat.Screen.ThongTinDonHang
import com.example.appbannoithat.Screen.TimKiem
import com.example.appbannoithat.Screen.TrangChu
import com.example.appbannoithat.Screen.XacNhan
import com.example.appbannoithat.ViewModel.ViewModel

@Composable
fun navController(viewModel: ViewModel, navController: NavHostController, mainActivity: MainActivity) {

    NavHost(
        navController = navController,
        startDestination = Screen.ManHinh.route
    ) {
        composable(Screen.TrangChu.route) { TrangChu(navController, viewModel) }
        composable("listNoiThat/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            if (id != null) {
                ListNoiThat(navController, viewModel, id)
            }
        }
        composable("chat/{taikhN}/{idNhan}") { backStackEntry ->
            val taikhN = backStackEntry.arguments?.getString("taikhN")
            val idNhan = backStackEntry.arguments?.getString("idNhan")
            if (taikhN != null && idNhan != null) {
                Chat(navController, viewModel, taikhN, idNhan)
            }
        }
        composable(Screen.NguoiDungs.route) { NguoiDungs(navController, viewModel) }
        composable(Screen.ManHinh.route) { ManHinh(navController, viewModel) }
        composable(Screen.DanhGia.route) { DanhGia(navController, viewModel) }
        composable(Screen.NoiThat.route) { NoiThat(navController, viewModel) }
        composable(Screen.TimKiem.route) { TimKiem(navController, viewModel) }
        composable(Screen.Register.route) { Register(navController, viewModel) }
        composable(Screen.Login.route) { Login(navController, viewModel) }
        composable(Screen.GioHang.route) { GioHang(viewModel, navController) }
        composable(Screen.DonHang.route) { DonHang(navController, viewModel) }
        composable(Screen.HoaDon.route) { HoaDon(navController, viewModel) }
        composable(Screen.ThongTinDonHang.route) { ThongTinDonHang(navController, viewModel) }
        composable("xacNhan/{tongtien}") { backStackEntry ->
            val tongtienString = backStackEntry.arguments?.getString("tongtien")
            val tongtien = tongtienString?.toIntOrNull() ?: 0
            XacNhan(navController, viewModel, tongtien, mainActivity)
        }
    }
}