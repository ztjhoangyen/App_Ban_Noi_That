package com.example.appbannoithat.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.appbannoithat.Model.Account
import com.example.appbannoithat.ViewModel.ViewModel
import com.example.appbannoithat.component.textDefault

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NguoiDungs(navController: NavHostController, viewModel: ViewModel) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

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
                title = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Danh sách người dùng",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color.DarkGray
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) {
        nguoidungColumn(it, viewModel, navController)
    }
}


@Composable
fun nguoidungColumn(it: PaddingValues, viewModel: ViewModel, navController: NavController) {
    val acc = viewModel.acc.observeAsState()
    val id = acc.value?._id

    if (id != null) {
        viewModel.getnguoidung(id)
    }

    val ngdungs = viewModel.ngdungs.observeAsState()
    val lstngdung = ngdungs.value ?: emptyList()


    LazyColumn(
        modifier = Modifier.padding(it)
    ) {
        items(lstngdung) { ngdung ->
            nguoiDung(
                ngdung = ngdung,
                onClick = {
                    if (id != null) {
                        viewModel.requestMessages(id, ngdung._id)
                    }
                    viewModel.listenForMessages()
                    navController.navigate("chat/${ngdung.ten_tai_khoan}/${ngdung._id}")
                }
            )
        }
    }
}

@Composable
fun nguoiDung(ngdung: Account, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .clickable(
                onClick = onClick
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        textDefault("${ngdung.ho_ten}")
    }
}


//=== cmt r test lại thôi

//val objChat = id?.let {
//    ChatReq(
//        idSender = it,
//        idRecever = ngdung._id
//    )
//}
//if (objChat != null) {
//    viewModel.postchat(objChat)
//    Log.d("objChat", "$objChat")
//}