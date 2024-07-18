package com.example.appbannoithat.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.shadow
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
import com.example.appbannoithat.component.textDefault

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThongTinDonHang(navController: NavController, viewModel: ViewModel) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

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
                            fontSize = 17.sp,
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
fun inforDCCT(it: PaddingValues, viewModel: ViewModel) {
    val getDHCT = viewModel.getDHCT.observeAsState()
    val DHCTs = getDHCT.value ?: emptyList()

    val acc = viewModel.acc.observeAsState()
    val user = acc.value

    val listHD = viewModel.thongtindh.observeAsState()

    Column(
        modifier = Modifier
            .padding(it)
    ) {
        Column(
            modifier = Modifier
                .padding(start = 20.dp)
        ) {
            textDefault("Họ tên: ${user?.ho_ten}")
            textDefault("Địa chỉ giao hàng: ${listHD.value?.dia_chi_giao_hang}")
            textDefault("Phương thức thanh toán: ${listHD.value?.phuong_thuc_thanh_toan}")
            textDefault( "Ghi chú: ${listHD.value?.ghi_chu}")
        }

        Spacer(
            modifier = Modifier
                .height(1.dp)
                .background(color = Color.LightGray),
        )

        LazyColumn {
            items(DHCTs.size) {
                DHCTItem(DHCTs[it])
            }
        }
    }
}


@Composable
fun DHCTItem(obj: DonHangCT) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(8.dp))
            .background(color = Color.White, shape = RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ){
            Spacer(
                modifier = Modifier
                    .width(20.dp)
            )
            Image(
                painter = rememberImagePainter(obj.img[0]),
                contentDescription = null,
                modifier = Modifier
                    .weight(1f)
                    .height(150.dp),
                contentScale = ContentScale.Fit
            )
            Column(
                modifier = Modifier
                    .padding(20.dp, 0.dp)
                    .weight(1.5f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center
            ) {
                textDefault("Tên nội thất: ${obj.ten_noi_that}")
                textDefault("Tổng tiền: ${obj.tong_tien}")
                textDefault("Tổng số lượng: ${obj.so_luong}")
            }
        }
    }
}