package com.example.appbannoithat.Model

import java.util.Date

data class NoiThat(
    val _id : String,
    val ten_noi_that : String,
    val mo_ta : String,
    val gia : Int,
    val so_luong : Int,
    val hinh_anh : List<String>,
    val loai_noi_that_id : String,
    val ngay_nhap_kho : Date
)