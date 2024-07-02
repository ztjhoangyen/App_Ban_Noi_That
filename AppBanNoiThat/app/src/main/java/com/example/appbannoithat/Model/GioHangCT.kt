package com.example.appbannoithat.Model

data class GioHangCT(
    val _id : String,
    val gio_hang_id : String,
    val noi_that_id : NoiThat,
    val so_luong: Int,
    val gia: Int
)

data class GioHangCTReq(
    val gio_hang_id : String,
    val noi_that_id : String,
    val so_luong: Number,
    val gia: Number
)