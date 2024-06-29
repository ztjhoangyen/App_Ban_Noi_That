package com.example.appbannoithat.Model

data class GioHang(
    val _id : String,
    val nguoi_dung_id : String,
    val trang_thai : Number
)

data class GioHangReq(
    val nguoi_dung_id : String,
    val trang_thai : Number
)