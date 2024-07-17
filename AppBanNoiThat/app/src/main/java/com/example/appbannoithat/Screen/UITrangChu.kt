package com.example.appbannoithat.Screen

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.appbannoithat.Model.Account
import com.example.appbannoithat.ViewModel.ViewModel

@Composable
fun UITrangChu(it: PaddingValues, viewModel: ViewModel, navController: NavController) {
    val acc = viewModel.acc.observeAsState()
    val id = acc.value?._id

    if (id != null) {
        viewModel.getnguoidung(id)
    }

    val ngdungs = viewModel.ngdungs.observeAsState()
    val lstngdung = ngdungs.value ?: emptyList()


    Column(
        modifier = Modifier
            .padding(it)
    ) {

        LazyColumn {
            items(lstngdung) { ngdung ->
                itemNgdung(
                    ngdung = ngdung,
                    onClick = {
                        if (id != null) {
                            Log.d("Gửi ", "Goi lần 3")

                            viewModel.requestMessages(id, ngdung._id)
                        }
                        viewModel.listenForMessages()
                        navController.navigate("chat/${ngdung.ten_tai_khoan}/${ngdung._id}")
                    }
                )
            }
        }
    }
}

@Composable
fun itemNgdung(ngdung: Account, onClick : () -> Unit){
   Column(
       modifier = Modifier
           .clickable (
               onClick = onClick
           )
   ) {
       Text(
           text = "${ngdung._id}"
       )
       Text(
           text = "${ngdung.ten_tai_khoan}"
       )
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