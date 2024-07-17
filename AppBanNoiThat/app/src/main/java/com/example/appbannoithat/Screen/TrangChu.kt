package com.example.appbannoithat.Screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.appbannoithat.Model.ChatReq
import com.example.appbannoithat.R
import com.example.appbannoithat.ViewModel.ViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrangChu (navController: NavHostController, viewModel: ViewModel, ) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    var istab by remember { mutableStateOf("Home") }

    val acc = viewModel.acc.observeAsState()
    val id = acc.value?._id

    val idngdung = ""
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
                actions = {
                    Image(
                        painter = painterResource(id = R.drawable.chat),
                        contentDescription = null,
                        modifier = Modifier
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() },
                                onClick = {

                                    navController.navigate("chat/${idngdung}")
                                }
                            )
                            .padding(start = 15.dp)
                            .size(20.dp)
                    )
//                    IconButton(onClick = {}) {
//                        Icon(
//                            imageVector = Icons.Filled.Notifications,
//                            contentDescription = null,
//                            modifier = Modifier
//                                .clickable(
//                                    indication = null,
//                                    interactionSource = remember { MutableInteractionSource() },
//                                    onClick = {
//
//                                    }
//                                )
//                                .size(16.dp),
//                            tint = Color.Black
//                        )}
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Filled.Notifications,
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
                Row {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = null,
                        tint = if(istab == "Home") Color.Black else Color.Gray,
                        modifier = Modifier
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() },
                                onClick = {
                                    istab = "Home"
                                })
                            .weight(1f)
                    )
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = if(istab == "Thư mục") Color.Black else Color.Gray,
                        modifier = Modifier
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() },
                                onClick = {
                                    istab = "Thư mục"
                                })
                          .weight(1f)
                    )
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = if(istab == "Giỏ hàng") Color.Black else Color.Gray,
                        modifier = Modifier
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() },
                                onClick = {
                                    istab = "Giỏ hàng"
                                })
                          .weight(1f)
                    )

                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription =  null,
                        tint = if(istab == "Tôi") Color.Black else Color.Gray,
                        modifier = Modifier
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() },
                                onClick = {
                                    istab = "Tôi"
                                })
                          .weight(1f)
                    )
                }
            }
        }
    ) {
        when(istab){
            "Home" -> Home(it, viewModel, navController)
            "Thư mục" -> ThuMuc(it, viewModel, navController)
            "Giỏ hàng" -> UITrangChu(it, viewModel, navController)
            "Tôi" -> DonHang(navController, viewModel)
        }
    }
}



//val objChat = id?.let {
//    ChatReq(
//        idSender = it,
//        idRecever = "667dbfcbe8426273772ec45d"
//    )
//}
//if (objChat != null) {
//    viewModel.postchat(objChat)
//    viewModel.getDanhSachTextChat(id, "667dbfcbe8426273772ec45d")
//    viewModel.listenForMessage()
////                                        viewModel.getmessages(id, "667dbfcbe8426273772ec45d")
//    Log.d("objChat", "${objChat}")
//}