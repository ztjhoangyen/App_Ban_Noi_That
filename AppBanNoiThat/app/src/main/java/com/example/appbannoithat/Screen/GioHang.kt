package com.example.appbannoithat.Screen


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
fun GioHang(viewModel: ViewModel, navController: NavController) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val acc = viewModel.acc.observeAsState()
    val id = acc.value?._id
    if (id != null) {
        viewModel.getGHCT(id)
    }
    val listGHUser = viewModel.GioHangCT.observeAsState()
    val GHCTs = listGHUser.value ?: emptyList()
    var tongtien by remember { mutableStateOf(0) }

    LaunchedEffect(GHCTs) {
        tongtien = GHCTs.sumOf {
            it.gia * it.so_luong
        }
    }

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
                modifier = Modifier
                    .background(Color.White)
                    .height(200.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Tổng",
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                        Text(
                            text = "${tongtien}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                    }
                    Column(
                        modifier = Modifier
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() },
                                onClick = {
                                    navController.navigate("xacNhan/${tongtien}")
                                }
                            )
                            .fillMaxWidth()
                            .height(60.dp)
                            .padding(10.dp)
                            .background(Color(0xFF02DC66), shape = RoundedCornerShape(5.dp)),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Xác nhận",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                }
            }
        }
    ) {
        ListGH(navController, viewModel, it, GHCTs, id.toString())
    }
}

@Composable
fun ListGH(
    navController: NavController,
    viewModel: ViewModel,
    it: PaddingValues,
    GHCTs: List<GioHangCT>,
    id: String,
) {
    LazyColumn(
        modifier = Modifier.padding(it),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        GHCTs?.let {
            items(it.size) { index ->
                GHItem(it[index], viewModel, onDelete = {
                    viewModel.getdel(it[index]._id, it.toString())
                })
            }
        }
    }
}

@Composable
fun GHItem(hiohangCT: GioHangCT, viewModel: ViewModel, onDelete: () -> Unit) {
    var quantity by remember { mutableStateOf(hiohangCT.so_luong) }
    val maxQuantity = hiohangCT.noi_that_id?.so_luong ?: 0
    var isVisi by remember { mutableStateOf(false) }
//derivedStateOf giúp tối ưu hóa hiệu suất bằng cách chỉ tính toán lại giá trị của nó khi các trạng thái mà nó phụ thuộc thay đổi.
    val plus by remember { derivedStateOf { if (isVisi) Color.LightGray else Color.Black } }

    val user by viewModel.acc.observeAsState()
    val id = user?._id

    val coroutineScope = rememberCoroutineScope()

    Row(
        modifier = Modifier
            .height(100.dp)
    ) {
        Image(
            painter = rememberImagePainter(hiohangCT.noi_that_id.hinh_anh[0]),
            contentDescription = null,
            modifier = Modifier
                .size(80.dp)
                .padding(start = 8.dp),
            contentScale = ContentScale.Crop
        )


        Column(
            modifier = Modifier
                .weight(1f)
                .padding(
                    start = 8.dp
                ),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "${hiohangCT.noi_that_id.ten_noi_that}",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = "${hiohangCT.gia} VNĐ",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(
                    text = "-",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickable {
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
                                }
                            }
                        }
                )
                Text(
                    text = "$quantity",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "+",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = plus,
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
                )
            }
            LaunchedEffect(Unit) {
                snapshotFlow { quantity }
                    .collect { newQuantity ->
                        viewModel.getGHCT(id.toString())
                    }
            }
        }
        Image(
            painter = painterResource(R.drawable.delete),
            contentDescription = null,
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = onDelete
                )
                .size(20.dp)
                .weight(1f),
            contentScale = ContentScale.Fit
        )
    }
}