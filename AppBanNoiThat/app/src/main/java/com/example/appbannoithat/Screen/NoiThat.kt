package com.example.appbannoithat.Screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.appbannoithat.Images
import com.example.appbannoithat.Model.NoiThat
import com.example.appbannoithat.Model.YeuThich
import com.example.appbannoithat.R
import com.example.appbannoithat.Server.Server
import com.example.appbannoithat.ViewModel.ViewModel
import com.example.appbannoithat.component.DialogDefaul

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoiThat(navController: NavHostController, viewModel: ViewModel) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val objNT = viewModel.NoiThatCT.observeAsState()

    val context = LocalContext.current

    var soLuong by remember { mutableStateOf(1) }
    val maxQuantity = objNT.value?.so_luong

    var isVisi by remember { mutableStateOf(false) }
//derivedStateOf load lại khi isVisi nó thay đổi
    val plusColor by remember { derivedStateOf { if (isVisi) Color.LightGray else Color.Black } }

    val isDN by viewModel.isLoggedIn
    val DN by viewModel.acc.observeAsState()

    var isDialog by remember { mutableStateOf(false) }
    val GHErr by viewModel.GHErr

    val acc = viewModel.acc.observeAsState()
    val id = acc.value?._id

    if (id != null) {
        viewModel.getFavUser(id)
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                modifier = Modifier.background(Color.White),
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .width(240.dp)
                            .clickable {
                                navController.navigate("timKiem")
                            }
                            .border(
                                width = 1.dp,
                                color = Color.LightGray,
                                shape = MaterialTheme.shapes.extraLarge
                            )
                            .padding(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            modifier = Modifier.size(15.dp),
                            contentDescription = "Search",
                            tint = Color.Black,
                        )
                        Text(
                            text = "Tìm sản phẩm",
                            fontWeight = FontWeight.Normal,
                            fontSize = 13.sp,
                            color = Color.DarkGray,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
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
                actions = {

                    val listfaByUser = viewModel.favUser.observeAsState()
                    val isFavorited = listfaByUser.value?.any { it.noi_that_id == objNT.value?._id } ?: false
                   Row {
                       Image(
                           painter = painterResource(id = R.drawable.bag),
                           contentDescription = null,
                           modifier = Modifier
                               .clickable(
                                   indication = null,
                                   interactionSource = remember { MutableInteractionSource() },
                                   onClick = {
                                      navController.navigate("gioHang")
                                   }
                               )
                               .padding(start = 15.dp)
                               .size(20.dp)
                       )

                       Image(
                           painter = painterResource(id = if (isFavorited) R.drawable.red else R.drawable.heart),
                           contentDescription = null,
                           modifier = Modifier
                               .clickable(
                                   indication = null,
                                   interactionSource = remember { MutableInteractionSource() },
                                   onClick = {
                                       if (isDN) {
                                           val objFav = id?.let {
                                               YeuThich(
                                                   nguoi_dung_id = it,
                                                   noi_that_id = objNT.value?._id ?: ""
                                               )
                                           }
                                           if (objFav != null) {
                                               viewModel.postFav(id, objFav)
                                           }
                                       } else {
                                           isDialog = !isDialog
                                       }
                                   }
                               )
                               .padding(start = 15.dp)
                               .size(20.dp)
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
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(10.dp)
                            .border(
                                width = 1.dp,
                                color = Color.Black,
                                shape = MaterialTheme.shapes.large
                            )
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(10.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Text(
                                text = "-",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .clickable {
                                        if (soLuong > 1) {
                                            soLuong--
                                        }
                                    }
                            )
                            Text(
                                fontWeight = FontWeight.Bold,
                                text = "${soLuong}"
                            )
                            Text(
                                text = "+",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .clickable {
                                        if (soLuong < maxQuantity!!) {
                                            soLuong++
                                            isVisi = false
                                        } else {
                                            isVisi = true
                                        }
                                    },
                                color = plusColor
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .weight(2f)
                            .padding(10.dp)
                            .background(Color.Green, MaterialTheme.shapes.extraLarge)
                            .border(
                                width = 1.dp,
                                color = Color.Green,
                                shape = MaterialTheme.shapes.extraLarge
                            )
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(10.dp)
                                .clickable (
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() },
                                    onClick = {
                                        if (isDN) {
                                            if (DN?._id != null) {
                                                val objTest = objNT.value?.let {
                                                    Server.Test(
                                                        nguoi_dung_id = DN?._id!!,
                                                        noi_that_id = it._id,
                                                        so_luong = soLuong,
                                                        gia = it.gia
                                                    )
                                                }

                                                if (objTest != null) {
                                                    viewModel.Test(objTest)
                                                    Toast
                                                        .makeText(
                                                            context,
                                                            "${GHErr}",
                                                            Toast.LENGTH_SHORT
                                                        )
                                                        .show()
                                                    navController.navigate("gioHang")
                                                }
                                            }
                                        } else {
                                            isDialog = !isDialog
                                        }
                                    }
                                    )
                                .align(Alignment.Center)
                                .border(
                                    width = 1.dp,
                                    color = Color.Green,
                                    shape = MaterialTheme.shapes.extraLarge
                                ),
                            color = Color.White,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            text = "Thêm vào giỏ hàng"
                        )
                    }
                }
            }
        }
    ) {
        objNT.value?.let { noiThat ->
            ItemNoiThat(viewModel, it, noiThat, soLuong, navController)
        } ?: run {
            Text(
                text = "Không có dữ liệu để hiển thị",
                modifier = Modifier.padding(16.dp)
            )
        }
    }
    if (isDialog) {
        DialogDefaul(
            onClickClose = {
                isDialog = false
            },
            onNav = {
                navController.navigate("login")
            },
            title = "Hãy đăng nhập để thêm vào giỏ hàng"
        )
    }
}

@Composable
fun ItemNoiThat(
    viewModel: ViewModel,
    it: PaddingValues,
    objNT: NoiThat,
    soluong: Int,
    navController: NavHostController,
) {

    Column(
        modifier = Modifier
            .padding(it)
            .clickable {

            }
    ) {

        Images(viewModel)

        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Text(
                text = "${objNT.ten_noi_that}",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Text(
                text = "Giá: ${objNT.gia} đ",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Text(
                text = "Số lượng: ${objNT.so_luong}",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        Spacer(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(Color.DarkGray)
        )

        Text(
            text = "Mô tả: ${objNT.mo_ta}",
            fontSize = 16.sp,
            fontStyle = FontStyle.Normal,
            modifier = Modifier
                .padding(vertical = 4.dp)
                .align(Alignment.CenterHorizontally)
                .padding(8.dp)
        )
        Spacer(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(Color.DarkGray)
        )
    }
}