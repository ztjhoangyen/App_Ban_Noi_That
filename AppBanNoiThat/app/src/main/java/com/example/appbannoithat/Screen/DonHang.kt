package com.example.appbannoithat.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.appbannoithat.Model.DonHang
import com.example.appbannoithat.Model.DonHangPUT
import com.example.appbannoithat.ViewModel.ViewModel
import com.example.appbannoithat.component.tabHD

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DonHang(navController: NavController, viewModel: ViewModel) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    var istab by remember { mutableStateOf("Đang xử lý") }
    val acc = viewModel.acc.observeAsState()
    val user = acc.value
    if (user != null) {
        viewModel.getDH(user._id, false)
    }
    val listHD = viewModel.getDH.observeAsState()
    val hoadons = listHD.value ?: emptyList()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                modifier = Modifier.background(Color.White),
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowLeft,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = Color.Black
                        )
                    }
                },
                title = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Đơn hàng",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color.DarkGray
                        )
                        Text(
                            text = "Mô tả ngắn gọn về đơn hàng",
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        content = {
            Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(it)
            ) {
                tabHD(istab) { selectedTab ->
                    istab = selectedTab
                }
                LazyColumn {
                    items(hoadons.filter { it.trang_thai == istab }) { donHang ->
                        DonHangItemView(
                            donHang,
                            onHuy = {
                            val obj = DonHangPUT(
                                role = false,
                                trangthai = "Đã hủy"
                            )
                                if (user != null) {
                                    viewModel.putDH(user._id, donHang._id, obj)
                                }
                            },
                            onXacnhan =  {
                                val obj = DonHangPUT(
                                    role = false,
                                    trangthai = "Đã xác nhận"
                                )
                                if (user != null) {
                                    viewModel.putDH(user._id, donHang._id, obj)
                                }
                            },
                            onChiTiet ={
                                viewModel.getDHCT(donHang._id)
                                navController.navigate("thongTinDonHang")
                            })
                    }
                }
            }
        }
    )
}

@Composable
fun DonHangItemView(
    item: DonHang,
    onHuy: () -> Unit,
    onXacnhan: () -> Unit,
    onChiTiet: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable(
                onClick = onChiTiet
            )
            .fillMaxWidth()
            .padding(16.dp)
    ) {
//        đã hủy thì không có gì cả
//        đã xác nhận thì kh có gì cả, đang xử lý thì bên người dùng hiện mỗi hủy thôi, admin hiện cả 2
        Text(
            text = item._id,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color.DarkGray
        )
        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = if(item.trang_thai != "Đã hủy" && item.trang_thai != "Đã xác nhận") "Hủy đơn hàng" else "",
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier
                .clickable(
                    onClick = onHuy
                )
        )
//        Thêm quyền người dùng vào để xét đk
        if (item.trang_thai == "Đang xử lý" ) {
            Text(
                text = "Xác nhận đơn hàng",
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.clickable(onClick = onXacnhan)
            )
        }
    }
}