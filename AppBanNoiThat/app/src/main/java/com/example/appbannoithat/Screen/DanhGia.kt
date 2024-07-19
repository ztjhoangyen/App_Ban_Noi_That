package com.example.appbannoithat.Screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.appbannoithat.Model.DanhGiaReq
import com.example.appbannoithat.Model.PhanHoiReq
import com.example.appbannoithat.ViewModel.ViewModel
import com.example.appbannoithat.component.RatingBar
import com.example.appbannoithat.component.hasRatingBar
import com.example.appbannoithat.component.textDefault
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DanhGia(navController: NavController, viewModel: ViewModel) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    LaunchedEffect(Unit) {
        viewModel.getphanhoi()
    }

    LaunchedEffect(Unit) {
        viewModel.getdanhgia()
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                modifier = Modifier.background(Color.White),
                title = {
                    Text(
                        text = "Đánh giá"
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
        }
    ) {
        UiDanhGia(navController, viewModel, it)
    }
}

@Composable
fun UiDanhGia(navController: NavController, viewModel: ViewModel, it: PaddingValues) {
    val hd = viewModel.itemhd.observeAsState()
    val idhd = hd.value?._id

    val acc = viewModel.acc.observeAsState()
    val idacc = acc.value?._id

    var rating by remember { mutableStateOf(0) }
    var text by remember { mutableStateOf("") }
    var phanhoiText by remember { mutableStateOf("") }

    val dgia = viewModel.dgia.observeAsState()
    val danhgia = dgia.value ?: emptyList()

    val pHoi = viewModel.pHoi.observeAsState()
    val phanhoi = pHoi.value ?: emptyList()

//tìm xem tồn tại hay không tồn tại để mà hiện if else

    val hasRated = danhgia.any { it.hoa_don_id == idhd }

//tìm dữ liệu item để hiển thị
    val foundDanhgia = danhgia.find { it.hoa_don_id == idhd }
    val diemDgia = foundDanhgia?.diem

    val hasphanhoi = phanhoi.any { it.danh_gia_id == foundDanhgia?._id }

    val foundphanhoi = phanhoi.find { it.danh_gia_id == foundDanhgia?._id }
    val noi_dungPhanhoi  = foundphanhoi?.noi_dung

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 15.dp, end = 15.dp)
            .padding(it),
        verticalArrangement = Arrangement.Center
    ) {
//        true hiện item
        if(hasRated && diemDgia != null){
            textDefault("${acc.value?.ho_ten}")
            Spacer(modifier = Modifier.height(20.dp))
            textDefault("Đánh giá sao")
            hasRatingBar(diemDgia)
            Spacer(modifier = Modifier.height(20.dp))
            textDefault("Mã hóa đơn: ${hd.value?._id}")
            textDefault("Đánh giá: ${foundDanhgia.binh_luan}")
            textDefault("Tổng tiền: ${hd.value?.don_hang_id?.tong_gia}")
            textDefault("Số lượng: ${hd.value?.don_hang_id?.so_luong}")
            textDefault("Phương thức thanh toán: ${hd.value?.don_hang_id?.phuong_thuc_thanh_toan}")
            Spacer(modifier = Modifier.height(20.dp))

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(color = Color.LightGray)
            )
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Phản hồi",
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.End),
                color = Color.Red
            )
//            true hiện item nội dung phản hồi
            Spacer(modifier = Modifier.height(20.dp))

            if(hasphanhoi && noi_dungPhanhoi != null){
                OutlinedTextField(
                    value = noi_dungPhanhoi,
                    onValueChange = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    maxLines = Int.MAX_VALUE,
                    minLines = 5,
                    enabled = false
                )
            }else{
//Admin phản hồi nếu false
                OutlinedTextField(
                    value = phanhoiText,
                    onValueChange = { phanhoiText = it },
                    placeholder = { Text("Phản hồi lại.") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    maxLines = Int.MAX_VALUE,
                    minLines = 5
                )

                Button(
                    onClick = {
                        val objPhanHoi = PhanHoiReq(
                            danh_gia_id = foundDanhgia._id,
                            nguoi_dung_id = idacc.toString(),
                            noi_dung = phanhoiText
                        )
                        viewModel.postphanhoi(objPhanHoi)
                        coroutineScope.launch {
                            Toast.makeText(context, "Gửi phản hồi thành công", Toast.LENGTH_SHORT).show()
                        }
                        navController.navigateUp()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFB2FF59),
                        contentColor = Color.Black
                    ),
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ){
                    Text(
                        text = "Gửi phản hồi"
                    )
                }
            }
        }else{
//không tồn tại hóa đơn trong danh sách đấy rồi, thì cho nó hiện
            Column(
                modifier = Modifier
                    .padding(10.dp),
                verticalArrangement = Arrangement.Center
            ) {
                textDefault("Mã hóa đơn: ${hd.value?._id}")
                textDefault("Tổng tiền: ${hd.value?.don_hang_id?.tong_gia}")
                textDefault("Số lượng: ${hd.value?.don_hang_id?.so_luong}")
                textDefault("Phương thức thanh toán: ${hd.value?.don_hang_id?.phuong_thuc_thanh_toan}")
            }
            Spacer(modifier = Modifier.height(10.dp))
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(color = Color.LightGray)
            )
            Spacer(modifier = Modifier.height(15.dp))

            RatingBar(
                rating = rating,
                onRatingChange = { newRating -> rating = newRating }
            )
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                placeholder = { Text("Nhận xét của bạn giúp chúng tôi cải thiện chất lượng sản phẩm.") }, // Sử dụng placeholder như một composable
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                maxLines = Int.MAX_VALUE,
                minLines = 5
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    val obj = DanhGiaReq(
                        nguoi_dung_id = idacc.toString(),
                        hoa_don_id = idhd.toString(),
                        diem = rating,
                        binh_luan = text,
                    )
                    viewModel.postdanhgia(obj)
                    coroutineScope.launch {
                        Toast.makeText(context, "Gửi đánh giá thành công", Toast.LENGTH_SHORT).show()
                    }
                    navController.navigateUp()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFB2FF59),
                    contentColor = Color.Black
                ),
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Gửi đánh giá")
            }
        }
    }
}