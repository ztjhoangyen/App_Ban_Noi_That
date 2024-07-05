package com.example.appbannoithat.Server

import com.example.appbannoithat.Model.Account
import com.example.appbannoithat.Model.DanhMuc
import com.example.appbannoithat.Model.DonHang
import com.example.appbannoithat.Model.DonHangCT
import com.example.appbannoithat.Model.DonHangPUT
import com.example.appbannoithat.Model.DonHangReq
import com.example.appbannoithat.Model.GioHangCT
import com.example.appbannoithat.Model.LoaiNoiThat
import com.example.appbannoithat.Model.NguoiDungDK
import com.example.appbannoithat.Model.NguoiDungDN
import com.example.appbannoithat.Model.NoiThat
import com.example.appbannoithat.Model.Slideshow
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query


interface Server {
    @GET("danhMuc")
    suspend fun getdanhMuc(): Response<List<DanhMuc>>

    @GET("loaiNoiThat/{id}")
    suspend fun getLoaiNT(@Path("id") id: String): Response<List<LoaiNoiThat>>

//    @POST("gioHang")
//    suspend fun postGioHang(gioHangReq: GioHangReq): Response<GioHang>

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

    @GET("gioHangCT/{id}")
    suspend fun getgioHangCT(@Path("id") id: String): Response<List<GioHangCT>>

    @POST("dangky")
    suspend fun postdangky(@Body NguoiDungDK : NguoiDungDK): Response<Void>

    @POST("dangnhap")
    suspend fun postdangnhap(@Body NguoiDungDN : NguoiDungDN): Response<Account>

    data class Test(
        val nguoi_dung_id: String,
        val noi_that_id: String,
        val so_luong: Int,
        val gia: Int
    )

    @POST("gioHangVaChiTiet")
    suspend fun postgioHangVaChiTiet(@Body test : Test): Response<Void>

    @POST("updateChiTietGH")
    suspend fun updateChiTietGH(@Body test : Test): Response<Void>

    @GET("slide")
    suspend fun getImage(): Response<List<Slideshow>>
//truyền id người dùng
    @POST("donhang/{id}")
    suspend fun postDH(@Path("id") id: String, @Body donHangReq : DonHangReq): Response<DonHang>
//truyền id đơn hàng
    @PUT("donhang/{id}/trangthai")
    suspend fun putDH(@Path("id") id: String, @Body donHangPUT : DonHangPUT): Response<DonHang>
//truyền id người dùng

    @GET("donhang/{userId}")
    suspend fun getDH(@Path("userId") userId: String, @Query("role") role : Boolean): Response<List<DonHang>>
//truyền id đơn hàng

    @GET("donhangchitiet/{id}")
    suspend fun getDHCT(@Path("id") id: String): Response<List<DonHangCT>>

    @DELETE("delGHCT/{id}")
    suspend fun delGHCT(@Path("id") id: String): Response<Void>

}