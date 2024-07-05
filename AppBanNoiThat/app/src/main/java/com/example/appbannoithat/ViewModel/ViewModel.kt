package com.example.appbannoithat.ViewModel

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appbannoithat.MainActivity
import com.example.appbannoithat.Model.Account
import com.example.appbannoithat.Model.DanhMuc
import com.example.appbannoithat.Model.DonHang
import com.example.appbannoithat.Model.DonHangCT
import com.example.appbannoithat.Model.DonHangPUT
import com.example.appbannoithat.Model.DonHangReq
import com.example.appbannoithat.Model.GioHang
import com.example.appbannoithat.Model.GioHangCT
import com.example.appbannoithat.Model.LoaiNoiThat
import com.example.appbannoithat.Model.NguoiDungDK
import com.example.appbannoithat.Model.NguoiDungDN
import com.example.appbannoithat.Model.NoiThat
import com.example.appbannoithat.Model.Slideshow
import com.example.appbannoithat.Server.RetrofitBanNoiThat
import com.example.appbannoithat.Server.Server
import com.example.appbannoithat.nav.SortState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import vn.zalopay.sdk.ZaloPayError
import vn.zalopay.sdk.ZaloPaySDK
import vn.zalopay.sdk.listeners.PayOrderListener

class ViewModel : ViewModel() {
    private val _danhMuc = MutableLiveData<List<DanhMuc>>()
    val danhmuc: LiveData<List<DanhMuc>> = _danhMuc

    private val _loaiNT = MutableLiveData<List<LoaiNoiThat>>()
    val loaiNT: LiveData<List<LoaiNoiThat>> = _loaiNT

    private val _noiThats = MutableLiveData<List<NoiThat>>()
    val noiThats: LiveData<List<NoiThat>> = _noiThats

    private val _NoiThatCT = MutableLiveData<NoiThat>()
    val NoiThatCT: LiveData<NoiThat> = _NoiThatCT

    private val _NewNoiThatCT = MutableLiveData<List<NoiThat>>()
    val NewNoiThatCT: LiveData<List<NoiThat>> = _NewNoiThatCT

    private val _TangNoiThatCT = MutableLiveData<List<NoiThat>>()
    val TangNoiThatCT: LiveData<List<NoiThat>> = _TangNoiThatCT

    private val _GiamNoiThatCT = MutableLiveData<List<NoiThat>>()
    val GiamNoiThatCT: LiveData<List<NoiThat>> = _GiamNoiThatCT

    private val _currentSortState = MutableLiveData<SortState>(SortState.DEFAULT)
    val currentSortState: LiveData<SortState> get() = _currentSortState

    private val _ghCT = MutableLiveData<GioHang>()
    val ghCT: LiveData<GioHang> = _ghCT

    private  val _acc = MutableLiveData<Account>()
    val acc : LiveData<Account> = _acc

    private val _GioHangCT = MutableLiveData<List<GioHangCT>>()
    val GioHangCT: LiveData<List<GioHangCT>> = _GioHangCT

    private val _slide = MutableStateFlow<List<Slideshow>?>(null)
    val slide: StateFlow<List<Slideshow>?> get() = _slide

    private val _getDH = MutableLiveData<List<DonHang>>()
    val getDH: LiveData<List<DonHang>> = _getDH

    private val _getDHCT = MutableLiveData<List<DonHangCT>>()
    val getDHCT: LiveData<List<DonHangCT>> = _getDHCT

    //thong bao
    private val _loaiNTErr = MutableLiveData<String>()
    val loaiNTErr: LiveData<String> = _loaiNTErr

    //    Thông báo TK
    private val _loginErr = mutableStateOf<String?>(null)
    val loginErr: State<String?> get() = _loginErr

    private val _registerErr = mutableStateOf<String?>(null)
    val registerErr: State<String?> get() = _registerErr

    private val _isLoggedIn = mutableStateOf(false)
    val isLoggedIn = _isLoggedIn

    private val _isregister = mutableStateOf(false)
    val isregister = _isregister

    private val _GHErr = mutableStateOf<String?>(null)
    val GHErr: State<String?> get() = _GHErr


    init {
        getDM()
    }

    fun setSortState(state: SortState) {
        _currentSortState.value = state
    }

    fun postRegister(account: NguoiDungDK){
        viewModelScope.launch{
            try{
                val response = RetrofitBanNoiThat().server.postdangky(account)
                if(response.isSuccessful){
                    _isregister.value = true
                    Log.d("VM_Register", "Success")
                }else{
                    _isregister.value = false
                    when(response.code()){
                        404 -> _registerErr.value = "Nhập thiếu thông tin"
                        409 -> _registerErr.value = "Tài khoản này đã tồn tại"
                        else -> _registerErr.value = "Đăng ký thất bại"
                    }
                    Log.d("VM_Register", "Not Success")
                }
            }catch(e: Exception){
                _isregister.value = false
                _registerErr.value = "Đăng ký thất bại"
                Log.d("VM_Register", "Not Success ${e.message}")
            }
        }
    }

    //    Dùng để quay lại màn hình cũ
    fun registerDH() {
        _isregister.value = false
    }

    fun logout() {
        _isLoggedIn.value = false
        println("${_isLoggedIn.value}")
    }

    fun postLogin(acc: NguoiDungDN) {
        viewModelScope.launch {
            try {
                val response = RetrofitBanNoiThat().server.postdangnhap(acc)
                if (response.isSuccessful) {
                    _acc.value = response.body()
                    _isLoggedIn.value = true
                    _loginErr.value = ""
                    Log.d("VM_Login", "Success ${_acc.value}")
                } else {
                    _isLoggedIn.value = false
                    when (response.code()) {
                        404 -> _loginErr.value = "Sai tên đăng nhập hoặc mật khẩu"
                        else ->  _loginErr.value = "Đăng nhập thất bại"
                    }
                }
            } catch (e: Exception) {
                _isLoggedIn.value = false
                _loginErr.value = "Đăng nhập thất bại"
                Log.d("VM_Login", "Not Success: ${e.message}")
            }
        }
    }

    fun getDM() {
        viewModelScope.launch {
            try {
                val response = RetrofitBanNoiThat().server.getdanhMuc()
                if (response.isSuccessful) {
                    _danhMuc.value = response.body()
                    Log.d("VM_dmuc", "Success")
                } else {
                    Log.d(
                        "VM_dmuc",
                        "Not Success: ${response.code()} - ${response.message()}"
                    )
                }
            } catch (e: Exception) {
                Log.e("VM_dmuc", "Not Success ${e.message}")
            }
        }
    }

    fun getLoaiNT(id: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitBanNoiThat().server.getLoaiNT(id)
                if (response.isSuccessful) {
                    _loaiNT.value = response.body()
                    Log.d("VM_loaiNT", "Success")
                } else {
                    Log.d(
                        "VM_loaiNT",
                        "Not Success: ${response.code()} - ${response.message()}"
                    )
                    when (response.code()) {
                        404 -> _loaiNTErr.value = "Rất tiếc, hiện không có sản phẩm nào"
                    }
                    _loaiNT.value = response.body()
                }
            } catch (e: Exception) {
                Log.e("VM_loaiNT", "Not Success ${e.message}")
            }
        }
    }

    // CHU Ý   Nhớ text list trông UI
    fun getNoiThats(id: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitBanNoiThat().server.getNoiThats(id)
                if (response.isSuccessful) {
                    _noiThats.value = response.body()
                    Log.d("VM_nthat", "Success")
                } else {
                    Log.d(
                        "VM_nthat",
                        "Not Success: ${response.code()} - ${response.message()}"
                    )
                    _noiThats.value = emptyList()
                }
            } catch (e: Exception) {
                Log.e("VM_nthat", "Not Success ${e.message}")
            }
        }
    }

    fun getNoiThatCT(id: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitBanNoiThat().server.getnoiThatCT(id)
                if (response.isSuccessful) {
                    _NoiThatCT.value = response.body()
                    Log.d("VM_nthat", "Success" + _NoiThatCT.value)
                } else {
                    Log.d(
                        "VM_nthat",
                        "Not Success: ${response.code()} - ${response.message()}"
                    )
                }
            } catch (e: Exception) {
                Log.e("VM_nthat", "Not Success ${e.message}")
            }
        }
    }

    fun getnew(id: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitBanNoiThat().server.getspnew(id)
                if (response.isSuccessful) {
                    _NewNoiThatCT.value = response.body()
                    Log.d("VM_new", "Success")
                } else {
                    Log.d(
                        "VM_new",
                        "Not Success: ${response.code()} - ${response.message()}"
                    )
                    _NewNoiThatCT.value = emptyList()
                }
            } catch (e: Exception) {
                Log.e("VM_new", "Not Success ${e.message}")
            }
        }
    }

    fun gettang(id: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitBanNoiThat().server.getTang(id)
                if (response.isSuccessful) {
                    _TangNoiThatCT.value = response.body()
                    Log.d("VM_tang", "Success")
                } else {
                    Log.d(
                        "VM_tang",
                        "Not Success: ${response.code()} - ${response.message()}"
                    )
                    _TangNoiThatCT.value = emptyList()
                }
            } catch (e: Exception) {
                Log.e("VM_tang", "Not Success ${e.message}")
            }
        }
    }

    fun getgiam(id: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitBanNoiThat().server.getgiam(id)
                if (response.isSuccessful) {
                    _GiamNoiThatCT.value = response.body()
                    Log.d("VM_giam", "Success")
                } else {
                    _GiamNoiThatCT.value = emptyList()
                    Log.d(
                        "VM_giam",
                        "Not Success: ${response.code()} - ${response.message()}"
                    )
                }
            } catch (e: Exception) {
                Log.e("VM_giam", "Not Success ${e.message}")
            }
        }
    }

    fun Test(test : Server.Test){
        viewModelScope.launch {
            try {
                val response = RetrofitBanNoiThat().server.postgioHangVaChiTiet(test)
                if (response.isSuccessful) {
                    _GHErr.value = "Thêm vào giỏ hàng thành công"
                    Log.d("VM_gioHangVaChiTiet", "Success" + "${response.body()}")
                } else {
                    when(response.code()){
                        404 -> _GHErr.value = "Sản phẩm không tồn tại"
                        409 -> _GHErr.value = "Đã tồn tại, sang trang giỏ hàng"
                        400 -> _GHErr.value = "Số lượng mua vượt quá số lượng tồn kho"
                        else -> _GHErr.value = "Thất bại"

                    }
                    Log.d(
                        "VM_gioHangVaChiTiet",
                        "Not Success: ${response.code()} - ${response.message()}"
                    )
                }
            } catch (e: Exception) {
                Log.e("VM_gioHangVaChiTiet", "Not Success ${e.message}")
            }
        }
    }

    fun getGHCT(id: String){
        viewModelScope.launch {
            try {
                val response = RetrofitBanNoiThat().server.getgioHangCT(id)
                if (response.isSuccessful) {
                     _GioHangCT.value = response.body()
                    Log.d("VM_gioHangCT", "Success" + "${response.body()}")
                } else {
                    Log.d(
                        "VM_gioHangCT",
                        "Not Success: ${response.code()} - ${response.message()}"
                    )
                     _GioHangCT.value = emptyList()
                }
            } catch (e: Exception) {
                Log.e("VM_gioHangCT", "Not Success ${e.message}")
            }
        }
    }

    fun PutGHCT(iduser: String, test : Server.Test){
        viewModelScope.launch {
            try {
                val response = RetrofitBanNoiThat().server.updateChiTietGH(test)
                if (response.isSuccessful) {
                    getGHCT(iduser)
                    Log.d("VM_putgioHangVaChiTiet", "Success" + "${response.body()}")
                } else {
                    Log.d(
                        "VM_putgioHangVaChiTiet",
                        "Not Success: ${response.code()} - ${response.message()}"
                    )
                }
            } catch (e: Exception) {
                Log.e("VM_putgioHangVaChiTiet", "Not Success ${e.message}")
            }
        }
    }

    fun getSlideshow(){
        viewModelScope.launch {
            try {
                val response = RetrofitBanNoiThat().server.getImage()
                if (response.isSuccessful) {
                    _slide.value = response.body()
                    Log.d("VM_Image", "Success" + "${response.body()}")
                } else {
                    Log.d(
                        "VM_Image",
                        "Not Success: ${response.code()} - ${response.message()}"
                    )
                }
            } catch (e: Exception) {
                Log.e("VM_Image", "Not Success ${e.message}")
            }
        }
    }

    fun getdel(id: String, iduser: String){
        viewModelScope.launch {
            try {
                val response = RetrofitBanNoiThat().server.delGHCT(id)
                if (response.isSuccessful) {
                    getGHCT(iduser)
                    Log.d("VM_del", "Success")
                } else {
                    Log.d(
                        "VM_del",
                        "Not Success: ${response.code()} - ${response.message()}"
                    )
                }
            } catch (e: Exception) {
                Log.e("VM_del", "Not Success ${e.message}")
            }
        }
    }

    fun postDH(id: String, donHangReq : DonHangReq){
        viewModelScope.launch {
            try {
                val response = RetrofitBanNoiThat().server.postDH(id, donHangReq)
                if (response.isSuccessful) {
                    //Xóa giỏ hàng
                    _GioHangCT.value = emptyList()
                    Log.d("VM_postDH", "Success")
                } else {
                    when(response.code()){
                        400 -> _GHErr.value = "Một số sản phẩm vượt quá số lượng tồn kho. Mình đã cập nhật lại số lượng trong giỏ hàng. Vui lòng kiểm tra lại giỏ hàng của bạn."
                        404 -> _GHErr.value = "Không tìm thấy giỏ hàng"
                        else -> _GHErr.value = "Thất bại"

                    }
                    Log.d(
                        "VM_postDH",
                        "Not Success: ${response.code()} - ${response.message()}"
                    )
                }
            } catch (e: Exception) {
                Log.e("VM_postDH", "Not Success ${e.message}")
            }
        }
    }

    fun getDH(id: String, role: Boolean){
        viewModelScope.launch {
            try {
                val response = RetrofitBanNoiThat().server.getDH(id, role)
                if (response.isSuccessful) {
                    _getDH.value = response.body()
                    Log.d("VM_getDH", "Success")
                } else {
                    when(response.code()){
                        404 -> _GHErr.value = "Không tồn tại hóa đơn"
                        else -> _GHErr.value = "Thất bại"
                    }
                    Log.d(
                        "VM_getDH",
                        "Not Success: ${response.code()} - ${response.message()}"
                    )
                    _getDH.value = emptyList()

                }
            } catch (e: Exception) {
                _getDH.value = emptyList()
                Log.e("VM_getDH", "Not Success ${e.message}")
            }
        }
    }

    fun putDH(idUser: String, id: String, donHangPUT : DonHangPUT){
        viewModelScope.launch {
            try {
                val response = RetrofitBanNoiThat().server.putDH(id, donHangPUT)
                if (response.isSuccessful) {
                    getDH(idUser, donHangPUT.role)
                    Log.d("VM_putDH", "Success")
                } else {
                    when(response.code()){
                        404 -> _GHErr.value = "Không tìm thấy đơn hàng"
                        403 -> _GHErr.value = "Bạn không có quyền thay đổi trạng thái đơn hàng này."
                        else -> _GHErr.value = "Thất bại"
                    }
                    Log.d(
                        "VM_putDH",
                        "Not Success: ${response.code()} - ${response.message()}"
                    )
                }
            } catch (e: Exception) {
                Log.e("VM_putDH", "Not Success ${e.message}")
            }
        }
    }

    fun getDHCT(id: String){
        viewModelScope.launch {
            try {
                val response = RetrofitBanNoiThat().server.getDHCT(id)
                if (response.isSuccessful) {
                    _getDHCT.value = response.body()
                    Log.d("VM_getHDCT", "Success")
                } else {
                    Log.d(
                        "VM_getHDCT",
                        "Not Success: ${response.code()} - ${response.message()}"
                    )
                }
            } catch (e: Exception) {
                Log.e("VM_getHDCT", "Not Success ${e.message}")
            }
        }
    }


}