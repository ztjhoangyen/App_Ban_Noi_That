package com.example.appbannoithat.Screen


import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.appbannoithat.Model.GioHangCT
import com.example.appbannoithat.R
import com.example.appbannoithat.Server.Server
import com.example.appbannoithat.ViewModel.ViewModel
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun GioHang(navController: NavController, viewModel: ViewModel) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val acc = viewModel.acc.observeAsState()
    val id = acc.value?._id
    if (id != null) {
        viewModel.getGHCT(id)
    }
    val listGHUser = viewModel.GioHangCT.observeAsState()
    val GHCTs = listGHUser.value ?: emptyList()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                modifier = Modifier.background(Color.White),
                title = {
                    Text(
                        text = "Giỏ hàng của bạn"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowLeft,
                            contentDescription = null,
                            modifier = Modifier
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() },
                                    onClick = {
                                        navController.navigateUp()
                                    }
                                )
                                .size(16.dp),
                            tint = Color.Black
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = Color.White,
                modifier = Modifier.background(Color.White),
            ) {
                Column {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Tổng"
                        )
                        Text(
                            text = "98"
                        )
                    }
                    Row {
                        Text(
                            text = "Xác nhận",
                            color = Color.White,
                            modifier = Modifier
                                .padding(10.dp)
                                .fillMaxWidth()
                        )
                    }
                }
            }
        }
    ) {
        ListGH(navController, viewModel, it, GHCTs)
    }
}

@Composable
fun ListGH(navController: NavController, viewModel: ViewModel, it: PaddingValues, GHCTs: List<GioHangCT>){
    LazyColumn(
        modifier = Modifier.padding(it),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        GHCTs?.let {
            items(it.size) { index ->
                GHItem(it[index], viewModel)
            }
        }
    }
}

@Composable
fun GHItem(hiohangCT : GioHangCT, viewModel: ViewModel){
    var quantity by remember { mutableStateOf(hiohangCT.so_luong) }
    val maxQuantity = hiohangCT.noi_that_id?.so_luong ?: 0
    val context = LocalContext.current
// tự động cập nhật lại các Composable liên quan, giúp giao diện luôn đồng bộ với trạng thái dữ liệu hiện tại.
    var isVisi by remember { mutableStateOf(false) }
//derivedStateOf giúp tối ưu hóa hiệu suất bằng cách chỉ tính toán lại giá trị của nó khi các trạng thái mà nó phụ thuộc thay đổi.
    val plus by remember { derivedStateOf { if(isVisi) Color.LightGray else Color.Black } }

//observable cho phép bạn lắng nghe thay đổi giá trị của một thuộc tính
    val user by viewModel.acc.observeAsState()
    val id = user?._id

    Row(
        modifier = Modifier
            .height(100.dp)
    ) {
        Image(
            painter = rememberImagePainter(hiohangCT.noi_that_id.hinh_anh),
            contentDescription = null,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentScale = ContentScale.Fit
        )
        val coroutineScope = rememberCoroutineScope()

        Column(
            modifier = Modifier
                .weight(1f),
        ) {
            Text(
                text = "${hiohangCT.noi_that_id.ten_noi_that}"
            )
            Text(
                text = "${hiohangCT.gia}"
            )
            Row {
                Text(
                    text = "-",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickable {
                            Log.d("KKKK", "${id.toString()} và ${hiohangCT.noi_that_id._id} và ${quantity} và ${hiohangCT.gia}")
                            coroutineScope.launch {
                                if (quantity > 1) {
                                    quantity--
                                    val objre = Server.Test(
                                        nguoi_dung_id = id.toString(),
                                        noi_that_id = hiohangCT.noi_that_id._id,
                                        so_luong = quantity,
                                        gia = hiohangCT.gia
                                    )
                                    isVisi = false
                                    viewModel.PutGHCT(id.toString(), objre)
                                    Log.d(
                                        "KKKK",
                                        "${id.toString()} và ${hiohangCT.noi_that_id.toString()} và ${quantity} và ${hiohangCT.gia}"
                                    )
                                }
                            }
                        }
                )

                Text(
                    text = "${hiohangCT.so_luong}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Text(
                    text = "+",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickable {
                            coroutineScope.launch {
                                if (quantity < maxQuantity) {
                                    quantity++
                                    isVisi = false
                                    val objreq = Server.Test(
                                        nguoi_dung_id = id.toString(),
                                        noi_that_id = hiohangCT.noi_that_id._id,
                                        so_luong = quantity,
                                        gia = hiohangCT.gia
                                    )
                                    viewModel.PutGHCT(id.toString(), objreq)

                                } else {
                                    isVisi = true
                                }
                            }
                        },
                    color = plus
                )

                LaunchedEffect(Unit) {
                    snapshotFlow { quantity }
                        .collect { newQuantity ->
                            viewModel.getGHCT(id.toString())
                        }
                }
            }
        }
        Image(
            painter = painterResource(R.drawable.delete),
            contentDescription = null,
            modifier = Modifier
                .size(20.dp)
                .weight(1f),
            contentScale = ContentScale.Fit
        )
    }
}