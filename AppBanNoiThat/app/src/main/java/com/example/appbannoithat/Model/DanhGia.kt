package com.example.appbannoithat.Model

data class DanhGia(
    val _id : String,
    val nguoi_dung_id : String,
    val hoa_don_id : String,
    val diem : Int,
    val binh_luan : String
)

data class PhanHoi(
    val _id : String,
    val danh_gia_id : String,
    val nguoi_dung_id : String,
    val noi_dung : String
)

data class PhanHoiReq(
    val danh_gia_id : String,
    val nguoi_dung_id : String,
    val noi_dung : String
)

data class DanhGiaReq(
    val nguoi_dung_id : String,
    val hoa_don_id : String,
    val diem : Int,
    val binh_luan : String
)