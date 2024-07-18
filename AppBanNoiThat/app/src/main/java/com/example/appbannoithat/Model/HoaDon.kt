package com.example.appbannoithat.Model

data class HoaDon (
    val don_hang_id: String
)

data class HoaDonRes (
    val _id: String,
    val don_hang_id: DonHang
)

data class HoaDonChiTietRes (
    val _id: String,
    val hoa_don_id: String,
    val don_hang_ct_id: DonHangCT
)