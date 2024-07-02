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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.appbannoithat.Model.NoiThat
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
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Filled.ShoppingCart,
                            contentDescription = null,
                            modifier = Modifier
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() },
                                    onClick = {

                                    }
                                )
                                .size(16.dp),
                            tint = Color.Black
                        )}
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
                  ){
                      Row(
                          modifier = Modifier
                              .padding(10.dp)
                              .fillMaxWidth(),
                          horizontalArrangement = Arrangement.SpaceAround
                      ){
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
                            .background(Color.Green)
                            .border(
                                width = 1.dp,
                                color = Color.Green,
                                shape = MaterialTheme.shapes.extraLarge
                            )
                    ){
                        Text(
                            modifier = Modifier
                                .padding(10.dp)
                                .clickable {
                                    if (isDN) {
                                       if(DN?._id != null){
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
                                               Toast.makeText(context, "${GHErr}", Toast.LENGTH_SHORT).show()
                                               navController.navigate("gioHang")
                                           }
                                       }
                                    } else {
                                        isDialog = !isDialog
                                    }
                                }
                                .align(Alignment.Center)
                                .border(
                                    width = 1.dp,
                                    color = Color.Green,
                                    shape = MaterialTheme.shapes.extraLarge
                                ),
                            color = Color.White,
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
    if(isDialog){
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
fun ItemNoiThat(viewModel: ViewModel, it: PaddingValues, objNT: NoiThat, soluong: Int, navController: NavHostController){

    Column(
        modifier = Modifier
            .padding(it)
            .clickable {

        }
    ) {
        Image(
            painter = rememberImagePainter(objNT.hinh_anh),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Fit
        )

        Text(
            text = "${objNT.ten_noi_that}"
        )
        Text(
            text = "${objNT.gia}"
        )
        Text(
            text = "${objNT.so_luong}"
        )
    }
}