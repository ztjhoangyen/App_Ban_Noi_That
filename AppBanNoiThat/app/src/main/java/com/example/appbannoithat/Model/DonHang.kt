package com.example.appbannoithat.Model

data class DonHang(
    val _id : String,
    val nguoi_dung_id : String,
    val tong_gia : Number,
    val so_luong : Number,
    val trang_thai : String,
    val phuong_thuc_thanh_toan : String,
    val dia_chi_giao_hang : String,
    val ghi_chu : String,
    val tinh_trang : String
)

//id truyền vào khi post là id người dùng
data class DonHangReq(
    val phuong_thuc_thanh_toan : String,
    val dia_chi_giao_hang : String,
    val ghi_chu : String,
    val tinh_trang : String
)

data class DonHangPUT(
    val role : Boolean,
    val trangthai : String
)

data class DonHangCT(
    val _id : String,
    val don_hang_id : String,
    val ten_noi_that : String,
    val mo_ta : String,
    val img : String,
    val so_luong : Int,
    val tong_tien : Int
)
