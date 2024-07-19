package com.example.appbannoithat.Server

import com.example.appbannoithat.Model.Account
import com.example.appbannoithat.Model.ChatReq
import com.example.appbannoithat.Model.ChatRes
import com.example.appbannoithat.Model.DanhGia
import com.example.appbannoithat.Model.DanhGiaReq
import com.example.appbannoithat.Model.DanhMuc
import com.example.appbannoithat.Model.DonHang
import com.example.appbannoithat.Model.DonHangCT
import com.example.appbannoithat.Model.DonHangPUT
import com.example.appbannoithat.Model.DonHangReq
import com.example.appbannoithat.Model.GioHangCT
import com.example.appbannoithat.Model.HoaDon
import com.example.appbannoithat.Model.HoaDonChiTietRes
import com.example.appbannoithat.Model.HoaDonRes
import com.example.appbannoithat.Model.LoaiNoiThat
import com.example.appbannoithat.Model.Message
import com.example.appbannoithat.Model.MessageR
import com.example.appbannoithat.Model.NguoiDungDK
import com.example.appbannoithat.Model.NguoiDungDN
import com.example.appbannoithat.Model.NoiThat
import com.example.appbannoithat.Model.PhanHoi
import com.example.appbannoithat.Model.PhanHoiReq
import com.example.appbannoithat.Model.Slideshow
import com.example.appbannoithat.Model.TotalFav
import com.example.appbannoithat.Model.YeuThich
import com.example.appbannoithat.Model.textChatReq
import com.example.appbannoithat.Model.updateSocket
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

    @POST("yeuthich")
    suspend fun postFav(@Body yeuThich: YeuThich): Response<Void>

    @GET("totalFav")
    suspend fun gettotalFav(): Response<List<TotalFav>>

    @GET("yeuthich")
    suspend fun getFav(): Response<Void>

    @GET("yeuthich/{id}")
    suspend fun getFavUser(@Path("id") id: String): Response<List<YeuThich>>

    @GET("timKiem")
    suspend fun gettimKiem(@Query("query") query: String): Response<List<NoiThat>>

    @POST("UpdateSocketId")
    suspend fun updateSocket(@Body updateSocket: updateSocket): Response<Void>

//    danh sách và ai nhận thì text ở bên đó còn ai gửi thì text bên gửi

//    @POST("textChat")
//    suspend fun posttextChat(@Body textChatReq: textChatReq): Response<List<Message>>
//
//    @GET("messages")
//    suspend fun getmessages(@Query("idSender") idSender: String,  @Query("idReceiver") idReceiver: String): Response<List<Message>>

    @GET("nguoidung")
    suspend fun getnguoidung(@Query("id") id: String): Response<List<Account>>

    @GET("AllloaiNoiThat")
    suspend fun getAllloaiNoiThat(): Response<List<LoaiNoiThat>>

    @POST("hdAndHdct")
    suspend fun posthdAndHdct(@Body hoaDon: HoaDon): Response<Void>

    @GET("hoadon")
    suspend fun gethoadon(): Response<List<HoaDonRes>>

    @GET("thongtindonhang/{id}")
    suspend fun getthongtindonhang(@Path("id") id: String): Response<DonHang>

    @GET("hoadonchitiet/{id}")
    suspend fun gethoadonchitiet(@Path("id") id: String): Response<List<HoaDonChiTietRes>>

    @POST("danhgia")
    suspend fun postdanhgia(@Body danhGiaReq : DanhGiaReq): Response<Void>

    @GET("danhGias")
    suspend fun getdanhgia(): Response<List<DanhGia>>

    @POST("phanhoi")
    suspend fun postphanhoi(@Body phanHoiReq: PhanHoiReq): Response<Void>
//id đánh giá
    @GET("phanHois")
    suspend fun getphanhoi(): Response<List<PhanHoi>>

    @GET("itemhoadon/{id}")
    suspend fun getitemhoadon(@Path("id") id: String): Response<HoaDonRes>




}


//=========== r sẽ test lại thôi

//@POST("chat")
//suspend fun postchat(@Body chatReq: ChatReq): Response<ChatRes>
