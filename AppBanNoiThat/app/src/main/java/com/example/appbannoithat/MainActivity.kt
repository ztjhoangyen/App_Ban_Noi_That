package com.example.appbannoithat

import android.content.Context
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.appbannoithat.Model.DonHangReq
import com.example.appbannoithat.ViewModel.ViewModel
import com.example.appbannoithat.component.CustomButton
import com.example.appbannoithat.component.ImageSlider
import com.example.appbannoithat.nav.navController
import com.example.bai.Api.CreateOrder
import com.example.bai.Constant.AppInfo
import kotlinx.coroutines.launch
import vn.zalopay.sdk.Environment
import vn.zalopay.sdk.ZaloPayError
import vn.zalopay.sdk.ZaloPaySDK
import vn.zalopay.sdk.listeners.PayOrderListener

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        setContent {
            val viewModel: ViewModel = viewModel()

            val navController = rememberNavController()
            navController(viewModel, navController, this@MainActivity)
        }
        try {
            ZaloPaySDK.init(AppInfo.APP_ID, Environment.SANDBOX)
            Log.d("ZaloPayInit", "ZaloPay SDK initialized successfully")
        } catch (e: Exception) {
            Log.e("ZaloPayInitError", "Error initializing ZaloPay SDK: ${e.message}")
            e.printStackTrace()
        }
    }

    //    Dialog Zalo Pay
    @Composable
    fun DialogZaloPay(onClickClose: () -> Unit, title: String, tongtien: Int?, viewModel: ViewModel, selectedOption: String, diachi: String, ghichu: String) {
//     val viewModel : ViewModel = viewModel()
        val DN = viewModel.acc.observeAsState()
        val idUser = DN.value
        Dialog(
            onDismissRequest = {
                onClickClose()
            },
            content = {
                Box(
                    modifier = Modifier
                        .background(
                            color = Color.White,
                            shape = MaterialTheme.shapes.medium
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = title,
                            fontSize = 13.sp,
                            color = Color.DarkGray.copy(alpha = 0.7f),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 10.dp),
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = Color.Black,
                                    shape = MaterialTheme.shapes.medium
                                )
                        ) {
                            CustomButton(
                                onClick = {
                                    onClickClose()
                                },
                                buttonText = "Cancel"
                            )
                            CustomButton(
                                onClick = {
                                    val orderApi = CreateOrder()
                                    lifecycleScope.launch {
                                        try {
                                            val data = orderApi.createOrder(tongtien.toString())
                                            Log.d("Amount", tongtien.toString())

                                            Log.d("ZaloPay", "Order created successfully: $data")
                                            val code = data.getString("return_code")
                                            Log.d("code", "Order created successfully: $code")

                                            if (code == "1") {
                                                val token = data.getString("zp_trans_token")
                                                val dh = DonHangReq(
                                                    phuong_thuc_thanh_toan = selectedOption,
                                                    dia_chi_giao_hang = diachi,
                                                    ghi_chu = ghichu,
                                                    tinh_trang = "Đã thanh toán"
                                                )
                                                if (idUser != null) {
                                                    viewModel.postDH(idUser._id, dh)
                                                }
                                                ZaloPaySDK.getInstance().payOrder(
                                                    this@MainActivity,
                                                    token,
                                                    "appnoithatzpdk://app",
                                                    object : PayOrderListener {
                                                        override fun onPaymentSucceeded(
                                                            payUrl: String?,
                                                            transToken: String?,
                                                            appTransID: String?,
                                                        ) {
                                                            Toast.makeText(
                                                                this@MainActivity,
                                                                "Thanh toán thành công",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
//                                                            paymentResult = "Thanh toán thành công"
                                                            Log.d(
                                                                "ZaloPay",
                                                                "Payment succeeded: payUrl=$payUrl, transToken=$transToken, appTransID=$appTransID"
                                                            )
                                                        }

                                                        override fun onPaymentCanceled(
                                                            payUrl: String?,
                                                            transToken: String?,
                                                        ) {
//                                                            paymentResult = "Hủy thanh toán"
                                                            Toast.makeText(
                                                                this@MainActivity,
                                                                "Hủy thanh toán",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                            Log.d(
                                                                "ZaloPay",
                                                                "Payment canceled: payUrl=$payUrl, transToken=$transToken"
                                                            )
                                                        }

                                                        override fun onPaymentError(
                                                            error: ZaloPayError?,
                                                            payUrl: String?,
                                                            transToken: String?,
                                                        ) {
//                                                            paymentResult = "Lỗi thanh toán"
                                                            Toast.makeText(
                                                                this@MainActivity,
                                                                "Lỗi thanh toán",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                            Log.e(
                                                                "ZaloPayError",
                                                                "Payment error: payUrl=$payUrl, transToken=$transToken"
                                                            )
                                                        }
                                                    })

//                                                paymentResult = "Đã tạo đơn hàng thành công và đang đợi đơn hàng được thanh toán"
                                            } else {
                                                Toast.makeText(
                                                    this@MainActivity,
                                                    "Failed to create order",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }

                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                            Log.e("ZaloPayError", "Exception: ${e.message}")
                                        }
                                    }
                                },
                                buttonText = "Tôi đồng ý"
                            )
                        }
                    }
                }
            }
        )
    }

    fun showDialogZaloPay(
        context: Context,
        onClickClose: () -> Unit,
        title: String,
        tongtien: Int?,
        viewModel: ViewModel,
        selectedOption: String,
        diachi: String,
        ghichu: String
    ) {
        setContent {
            DialogZaloPay(onClickClose, title, tongtien, viewModel, selectedOption, diachi, ghichu)
        }
    }
}


@Composable
fun ShowHI(viewModel: ViewModel) {
    val imageUrlsState by viewModel.slide.collectAsState()
    val imageUrls = imageUrlsState?.map { it.url } ?: emptyList() // Convert to list of URLs

    LaunchedEffect(Unit) {
        viewModel.getSlideshow()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        ImageSlider(imageUrls = imageUrls)
    }
}
