package com.example.appbannoithat.Server

import com.example.appbannoithat.Model.DanhMuc
import com.example.appbannoithat.Model.GioHang
import com.example.appbannoithat.Model.GioHangReq
import com.example.appbannoithat.Model.LoaiNoiThat
import com.example.appbannoithat.Model.NoiThat
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface Server {
    @GET("danhMuc")
    suspend fun getdanhMuc(): Response<List<DanhMuc>>

    @GET("loaiNoiThat/{id}")
    suspend fun getLoaiNT(@Path("id") id: String): Response<List<LoaiNoiThat>>

    @POST("gioHang")
    suspend fun postGioHang(gioHangReq: GioHangReq): Response<List<GioHang>>

    @GET("noiThat/{id}")
    suspend fun getNoiThats(@Path("id") id: String): Response<List<NoiThat>>

    @GET("noiThatCT/{id}")
    suspend fun getnoiThatCT(@Path("id") id: String): Response<NoiThat>

    @GET("sanPhamMoi/{id}")
    suspend fun getspnew(@Path("id") id: String): Response<List<NoiThat>>

    @GET("tang/{id}")
    suspend fun getTang(@Path("id") id: String): Response<List<NoiThat>>

    @GET("giam/{id}")
    suspend fun getgiam(@Path("id") id: String): Response<List<NoiThat>>


}