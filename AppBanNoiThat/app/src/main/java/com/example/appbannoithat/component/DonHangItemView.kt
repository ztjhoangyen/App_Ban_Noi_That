package com.example.appbannoithat.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appbannoithat.Model.DonHang
import com.example.appbannoithat.Model.HoaDon
import com.example.appbannoithat.ViewModel.ViewModel

@Composable
fun DonHangItemView(
    viewModel: ViewModel,
    item: DonHang,
    onHuy: () -> Unit,
    onXacnhan: () -> Unit,
    onChiTiet: () -> Unit,
    onDaXacNhan: () -> Unit,
) {
    val acc = viewModel.acc.observeAsState()
    val user = acc.value
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(8.dp))
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
            .clickable(onClick = onChiTiet)
    ) {
        Column(
            modifier = Modifier
                .clickable(
                    onClick = onChiTiet
                )
                .padding(16.dp)
        ) {

            Text(
                text = "Mã đơn hàng: ${item._id}",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Color.DarkGray
            )

            textDefault("Tổng giá: ${item.tong_gia} đ")
            textDefault("Số lượng: ${item.so_luong}")
            textDefault("Phương thức thanh toán: ${item.phuong_thuc_thanh_toan}")
            textDefault("Địa chỉ giao hàng: ${item.dia_chi_giao_hang}")
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (item.trang_thai != "Đã hủy" && item.trang_thai != "Đã xác nhận") "Hủy đơn hàng" else "",
                fontWeight = FontWeight.Normal,
                fontSize = 15.sp,
                color = Color.Gray,
                modifier = Modifier
                    .clickable(
                        onClick = onHuy
                    )
            )

            if (user?.role == true) {
                if (item.trang_thai == "Đang xử lý") {
                    Log.d("user?.role", "${user?.role}")
                    Text(
                        text = "Xác nhận đơn hàng",
                        fontWeight = FontWeight.Normal,
                        fontSize = 15.sp,
                        color = Color.Gray,
                        modifier = Modifier.clickable {
                            onXacnhan()
                        }
                    )
                } else if (item.trang_thai == "Đã xác nhận") {
                    Text(
                        text = "Xác nhận đơn hàng",
                        fontWeight = FontWeight.Normal,
                        fontSize = 15.sp,
                        color = Color.Gray,
                        modifier = Modifier.clickable {
//                        xác nhận xuất hóa đơn thì tạo ra hóa đơn luôn
                            onDaXacNhan()
                            val objHD = HoaDon(
                                don_hang_id = item._id
                            )
                            viewModel.posthdAndHdct(objHD)
                        }
                    )
                }
            }
        }
    }
}