package com.example.appbannoithat.Screen


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.appbannoithat.ViewModel.ViewModel
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.appbannoithat.MainActivity
import com.example.appbannoithat.Model.DonHangReq

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun XacNhan(navController: NavHostController, viewModel: ViewModel, tongtien: Int?, mainActivity: MainActivity) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
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
        }
    ) {
        thanhToan(it, tongtien, mainActivity, viewModel)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun thanhToan(it: PaddingValues, tongtien: Int?, mainActivity: MainActivity, viewModel: ViewModel){
    var diachi by remember { mutableStateOf("") }
    var diachiErr by remember { mutableStateOf("") }

    var sdt by remember { mutableStateOf("") }
    var sdtErr by remember { mutableStateOf("") }

    var ghichu by remember { mutableStateOf("") }
    var ghichuErr by remember { mutableStateOf("") }

    var isDialogZaloPay by remember { mutableStateOf(false) }

    var selectedOption by remember { mutableStateOf("") }

    val DN = viewModel.acc.observeAsState()
    val idUser = DN.value

    Column(
        modifier = Modifier
            .padding(it)
            .padding(20.dp)
    ) {
        OutlinedTextField(
            value = diachi,
            onValueChange = {
                diachi = it
                diachiErr = ""
            },
            placeholder = {
                Text(
                    "Nhập địa chỉ của bạn",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.DarkGray.copy(alpha = 0.4f)
                    )
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = Color.LightGray,
                cursorColor = Color.Black
            ),
            isError = diachiErr.isNotEmpty()
        )

        if (diachiErr.isNotEmpty()) {
            TextAcc(diachiErr)
        }
        Spacer(modifier = Modifier.height(10.dp))

        if (diachiErr != null) {
            Text(
                text = diachiErr ?: "",
                fontSize = 13.sp,
                color = Color.Red.copy(alpha = 0.7f),
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(start = 10.dp),
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = sdt,
            onValueChange = {
                sdt = it
                sdtErr = ""
            },
            placeholder = {
                Text(
                    "Nhập số điện thoại của bạn",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.DarkGray.copy(alpha = 0.4f)
                    )
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = Color.LightGray,
                cursorColor = Color.Black
            ),
            isError = sdt.isNotEmpty()
        )

        if (sdtErr.isNotEmpty()) {
            TextAcc(sdtErr)
        }
        Spacer(modifier = Modifier.height(10.dp))

        if (sdtErr != null) {
            Text(
                text = sdtErr ?: "",
                fontSize = 13.sp,
                color = Color.Red.copy(alpha = 0.7f),
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(start = 10.dp),
            )
        }

        Spacer(modifier = Modifier.height(10.dp))


        OutlinedTextField(
            value = ghichu,
            onValueChange = {
                ghichu = it
                ghichuErr = ""
            },
            placeholder = {
                Text(
                    "Ghi chú",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.DarkGray.copy(alpha = 0.4f)
                    )
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = Color.LightGray,
                cursorColor = Color.Black
            ),
            isError = ghichu.isNotEmpty()
        )

        if (ghichuErr.isNotEmpty()) {
            TextAcc(ghichuErr)
        }
        Spacer(modifier = Modifier.height(10.dp))

        if (ghichuErr != null) {
            Text(
                text = ghichuErr ?: "",
                fontSize = 13.sp,
                color = Color.Red.copy(alpha = 0.7f),
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(start = 10.dp),
            )
        }

        Spacer(modifier = Modifier.height(10.dp))
//    =====================


        Text(
            text = "Chọn phương thức thanh toán",
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp
        )
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Zalo Pay",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 8.dp)
                )
                RadioButton(
                    selected = selectedOption == "Zalo Pay",
                    onClick = {
                        selectedOption = "Zalo Pay"
                    },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color.Red,
                        unselectedColor = Color.Gray,
                    )
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "COD",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 8.dp)
                )
                RadioButton(
                    selected = selectedOption == "COD",
                    onClick = {
                        selectedOption = "COD"
                    },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color.Red,
                        unselectedColor = Color.Gray,
                    )
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Green)
                .border(width = 1.dp, color = Color.Green, shape = MaterialTheme.shapes.medium)
        ){
            Text(
                text = "Xác nhận",
                modifier = Modifier
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = {
                            if (diachi.isEmpty()) {
                                diachiErr = "Bắt buộc"
                            }
                            if (selectedOption == "Zalo Pay") {

                                isDialogZaloPay = !isDialogZaloPay
                            }else{
//                                if (diachiErr.isEmpty()) {
//                                    diachiErr = "Bắt buộc"
//                                }
                                val dh = DonHangReq(
                                    phuong_thuc_thanh_toan = selectedOption,
                                    dia_chi_giao_hang = diachi,
                                    ghi_chu = ghichu,
                                    tinh_trang = "Chưa thanh toán"
                                )
                                if (idUser != null) {
                                    viewModel.postDH(idUser._id, dh)
                                }
                            }
                        }
                    )
                    .padding(10.dp)
                    .align(Alignment.Center)
                    .background(Color.Green),
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
    if(isDialogZaloPay){
        mainActivity.showDialogZaloPay(
            mainActivity,
            onClickClose = {

            },
            title = "Tiêu đề",
            tongtien,
            viewModel,
            selectedOption,
            diachi,
            ghichu
        )
    }
}