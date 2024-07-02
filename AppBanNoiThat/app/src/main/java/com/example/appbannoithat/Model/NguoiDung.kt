package com.example.appbannoithat.Model

import com.google.gson.annotations.SerializedName


data class Account (
    val _id: String,
    val ten_tai_khoan: String,
    val email: String,
    val mat_khau: String,
    val ho_ten: String,
    val dia_chi: String,
    val so_dien_thoai: String,
    val role: Boolean
)

data class NguoiDungDK (
    val ten_tai_khoan: String,
    val email: String,
    val mat_khau: String,
    val ho_ten: String,
    val role: Boolean
)

data class NguoiDungDN (
    val ten_tai_khoan: String,
    val mat_khau: String
)