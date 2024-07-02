package com.example.appbannoithat.ViewModel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appbannoithat.Model.Account
import com.example.appbannoithat.Model.DanhMuc
import com.example.appbannoithat.Model.GioHang
import com.example.appbannoithat.Model.GioHangCT
import com.example.appbannoithat.Model.GioHangCTReq
import com.example.appbannoithat.Model.GioHangReq
import com.example.appbannoithat.Model.LoaiNoiThat
import com.example.appbannoithat.Model.NguoiDungDK
import com.example.appbannoithat.Model.NguoiDungDN
import com.example.appbannoithat.Model.NoiThat
import com.example.appbannoithat.Server.RetrofitBanNoiThat
import com.example.appbannoithat.Server.Server
import com.example.appbannoithat.nav.SortState
import kotlinx.coroutines.launch

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

    fun postGioHang(gioHangReq: GioHangReq) {
        viewModelScope.launch {
            try {
                val response = RetrofitBanNoiThat().server.postGioHang(gioHangReq)
                if (response.isSuccessful) {
                    _ghCT.value = response.body()
                    Log.d("VM_gioHang", "Success")
                } else {
                    Log.d(
                        "VM_gioHang",
                        "Not Success: ${response.code()} - ${response.message()}"
                    )
                }
            } catch (e: Exception) {
                Log.e("VM_gioHang", "Not Success ${e.message}")
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

//    fun postgioHangCT(gioHangCTReq: GioHangCTReq) {
//        viewModelScope.launch {
//            try {
//                val response = RetrofitBanNoiThat().server.postgioHangCT(gioHangCTReq)
//                if (response.isSuccessful) {
//                    Log.d("VM_GHCT", "Success" + "${response.body()}")
//                } else {
//                    Log.d(
//                        "VM_GHCT",
//                        "Not Success: ${response.code()} - ${response.message()}"
//                    )
//                }
//            } catch (e: Exception) {
//                Log.e("VM_GHCT", "Not Success ${e.message}")
//            }
//        }
//    }

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
//                   đang không cập nhật lại số lượng khi thêm
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

}