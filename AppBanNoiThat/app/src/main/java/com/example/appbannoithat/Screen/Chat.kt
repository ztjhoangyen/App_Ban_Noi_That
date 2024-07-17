package com.example.appbannoithat.Screen

import android.util.Log
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.appbannoithat.Model.Message
import com.example.appbannoithat.Model.MessageR
import com.example.appbannoithat.R
import com.example.appbannoithat.ViewModel.ViewModel
import org.json.JSONObject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Chat(
    navController: NavHostController,
    viewModel: ViewModel,
    taikhN: String,
    idNhan: String?,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    var text by remember { mutableStateOf("") }

    val acc = viewModel.acc.observeAsState()
    val id = acc.value?._id
    val nameHT = acc.value?.ten_tai_khoan
//    viewModel.listenForMessages()

//đặt if else cái nào nên load lại dữ liệu
    if(idNhan == idNhan){
        LaunchedEffect(Unit) {
            viewModel.on("message") { msg: JSONObject ->
                if (id != null) {
                    if (idNhan != null) {
                        viewModel.requestMessages(id, idNhan)
                    }
                    Log.d("Gửi ", "Gửi lần 1")

                    viewModel.listenForMessages()
                }
                Log.e("listenForMessages", "ok")

            }
        }
    }


    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                modifier = Modifier.background(Color.White),
                title = {
                    Text(
                        text = "Tên tài khoản"
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
                modifier = Modifier.background(Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .padding(15.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .border(
                                width = 1.dp,
                                color = Color.Black,
                                shape = RoundedCornerShape(24.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 12.dp)
                    ) {
                        if (text.isEmpty()) {
                            Text(
                                modifier = Modifier.padding(start = 10.dp),
                                text = "Aa",
                                color = Color.Gray
                            )
                        }
                        BasicTextField(
                            value = text,
                            onValueChange = { newText -> text = newText },
                            maxLines = 1,
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .fillMaxWidth(),
                            decorationBox = { innerTextField ->
                                Box(modifier = Modifier.fillMaxWidth()) {
                                    innerTextField()
                                }
                            }
                        )
                    }
                    Spacer(modifier = Modifier.width(15.dp))
                    Image(
                        painter = painterResource(id = R.drawable.plane),
                        contentDescription = null,
                        modifier = Modifier
                            .size(35.dp)
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() },
                                onClick = {
                                    Log.d("VV receiver", "${nameHT}-${taikhN}-${text}-${id}-${idNhan}")

                                    if (nameHT != null && id != null && idNhan != null) {
                                        viewModel.sendMessage(nameHT, taikhN, text, id, idNhan)
                                        Log.d(" receiver", "${nameHT}-${taikhN}-${text}-${id}-${idNhan}")
                                        Log.d("Gửi ", "Gửi lần 2")

                                    }
                                    text = ""
                                }
                            )
                    )
                }
            }
        }
    ) {
        CommentColumn(navController, viewModel, it)
    }
}


@Composable
fun CommentColumn(
    navController: NavHostController,
    viewModel: ViewModel,
    innerPadding: PaddingValues,
) {
    val messagesDD = viewModel.messages.observeAsState()
    val danhsach = messagesDD.value ?: emptyList()

    LazyColumn(
        modifier = Modifier.padding(innerPadding)
    ) {
//        để gọi được ra size thì cần xét đk nó không null
        items(danhsach.size) { message ->
            CommentItem(navController, danhsach[message], viewModel)
        }
    }
}
//chuyển trang thì gửi lần 2 mới được
@Composable
fun CommentItem(navController: NavHostController, it: MessageR, viewModel: ViewModel) {
    Column(
        modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "${it.content}",
            fontWeight = FontWeight.Bold
        )
//        Text(
//            text = "20-98-392",
//            color = Color.DarkGray
//        )
//        Text(
//            text = "${it.content}",
//            fontSize = 15.sp
//        )

//        OutlinedButton(onClick = {
//            viewModel.getReply(it._idComment)
//            navController.navigate("reply/${it._idComment}")
//        }) {
//            Text("Phản hồi")
//        }
//        Spacer(
//            modifier = Modifier
//                .height(1.dp)
//                .fillMaxWidth()
//                .background(Color.LightGray)
//        )
    }
}


// LaunchedEffect(Unit) {
//        if(idngdung.isNotEmpty()){
//            if (id != null) {
//                viewModel.getDanhSachTextChat(idngdung, id)
//                viewModel.listenForMessage()
//            }
//            if (id != null) {
//                Log.d("idngdung ${idngdung}", "${id} id")
//                viewModel.listenForMessage()
//                //                viewModel.getmessages(id, "667dbfcbe8426273772ec45d")
//            }
//        }else{
//            viewModel.getDanhSachTextChat(idngdung, "667dbfcbe8426273772ec45d")
//
//            if (id != null) {
//                Log.d("idngdung 667dbfcbe8426273772ec45d ${idngdung}", "${id} id")
//                viewModel.listenForMessage()
////                viewModel.getmessages(id, "667dbfcbe8426273772ec45d")
//            }
//        }
//    }


//click gửi
//   chatRess.value?.let { viewModel.sendMessage(it._id, text) }
//   viewModel.listenForMessages()