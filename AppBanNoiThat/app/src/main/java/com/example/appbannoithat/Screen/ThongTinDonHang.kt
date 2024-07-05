package com.example.appbannoithat.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.appbannoithat.Model.DonHangCT
import com.example.appbannoithat.ViewModel.ViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThongTinDonHang(navController: NavController, viewModel: ViewModel) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
//CHÚ Ý: PHẢI ĐĂNG NHẬP THÌ MỚI HIỆN THÔNG TIN ĐƠN HÀNG CUA TÔI
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
                            modifier = Modifier
                                .size(16.dp)
                                .clickable {
                                    navController.navigateUp()
                                },
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
                            text = "Thông tin đơn hàng",
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
        ) {
        inforDCCT(it, viewModel)
    }
}

    @Composable
    fun inforDCCT(it: PaddingValues, viewModel: ViewModel){
        val getDHCT = viewModel.getDHCT.observeAsState()
        val DHCTs = getDHCT.value ?: emptyList()

        val acc = viewModel.acc.observeAsState()
        val user = acc.value

        Column(
            modifier = Modifier
                .padding(it)
        ) {
            Text(
                text = "${user?.ho_ten}"
            )
            Text(
                text = "${user?.dia_chi}"
            )
            Text(
                text = "${user?.dia_chi}"
            )

            LazyColumn{
                items(DHCTs.size){
                    DHCTItem(DHCTs[it])
                }
            }
        }
    }


@Composable
fun DHCTItem(obj: DonHangCT) {
    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {

        Image(
            painter = rememberImagePainter(obj.img),
            contentDescription = null,
            modifier = Modifier
                .weight(1f)
                .height(200.dp),
            contentScale = ContentScale.Fit
        )
        Column(
            modifier = Modifier
                .padding(20.dp, 0.dp)
                .weight(1f)
                .fillMaxWidth(),
        ) {
            Text(text = "${obj.ten_noi_that}", color = Color.DarkGray)
            Text(text = "${obj.tong_tien}", color = Color.DarkGray, fontWeight = FontWeight.Bold)
            Text(text = "${obj.so_luong}", color = Color.DarkGray, fontWeight = FontWeight.Bold)
        }
    }
}