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
import com.example.appbannoithat.Model.DonHang
import com.example.appbannoithat.Model.DonHangCT
import com.example.appbannoithat.Model.DonHangPUT
import com.example.appbannoithat.Model.DonHangReq
import com.example.appbannoithat.Model.GioHang
import com.example.appbannoithat.Model.GioHangCT
import com.example.appbannoithat.Model.HoaDon
import com.example.appbannoithat.Model.HoaDonChiTietRes
import com.example.appbannoithat.Model.HoaDonRes
import com.example.appbannoithat.Model.LoaiNoiThat
import com.example.appbannoithat.Model.MessageR
import com.example.appbannoithat.Model.NguoiDungDK
import com.example.appbannoithat.Model.NguoiDungDN
import com.example.appbannoithat.Model.NoiThat
import com.example.appbannoithat.Model.Slideshow
import com.example.appbannoithat.Model.TotalFav
import com.example.appbannoithat.Model.YeuThich
import com.example.appbannoithat.Model.updateSocket
import com.example.appbannoithat.Server.RetrofitBanNoiThat
import com.example.appbannoithat.Server.Server
import com.example.appbannoithat.nav.SortState
import com.example.appbannoithat.socket.SocketManger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject

class ViewModel : ViewModel() {
    private val socketManger = SocketManger()

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

    private val _acc = MutableLiveData<Account>()
    val acc: LiveData<Account> = _acc

    private val _GioHangCT = MutableLiveData<List<GioHangCT>>()
    val GioHangCT: LiveData<List<GioHangCT>> = _GioHangCT

    private val _slide = MutableStateFlow<List<Slideshow>?>(null)
    val slide: StateFlow<List<Slideshow>?> get() = _slide

    private val _getDH = MutableLiveData<List<DonHang>>()
    val getDH: LiveData<List<DonHang>> = _getDH

    private val _getDHCT = MutableLiveData<List<DonHangCT>>()
    val getDHCT: LiveData<List<DonHangCT>> = _getDHCT

    private val _TotalFav = MutableLiveData<List<TotalFav>>()
    val TotalFav: LiveData<List<TotalFav>> = _TotalFav

    private val _favUser = MutableLiveData<List<YeuThich>>()
    val favUser: LiveData<List<YeuThich>> = _favUser

    private val _searchResults = MutableStateFlow<List<NoiThat>>(emptyList())
    val searchResults: StateFlow<List<NoiThat>> = _searchResults

    private val _ngdungs = MutableLiveData<List<Account>>(emptyList())
    val ngdungs: LiveData<List<Account>> = _ngdungs

    private val _messages = MutableLiveData<List<MessageR>>()
    val messages: LiveData<List<MessageR>> = _messages

    private val _socketID = MutableLiveData<String>()
    val socketID: LiveData<String> = _socketID

    private val _allloainoithat = MutableLiveData<List<LoaiNoiThat>>()
    val allloainoithat: LiveData<List<LoaiNoiThat>> = _allloainoithat

    private val _gethoadon = MutableLiveData<List<HoaDonRes>>()
    val gethoadon: LiveData<List<HoaDonRes>> = _gethoadon

    private val _thongtindh = MutableLiveData<DonHang>()
    val thongtindh: LiveData<DonHang> = _thongtindh

    private val _hdct = MutableLiveData<List<HoaDonChiTietRes>>()
    val hdct: LiveData<List<HoaDonChiTietRes>> = _hdct

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
        socketManger.connect()
        listForSocketId()

        getDM()

    }

    fun setSortState(state: SortState) {
        _currentSortState.value = state
    }

    fun postRegister(account: NguoiDungDK) {
        viewModelScope.launch {
            try {
                val response = RetrofitBanNoiThat().server.postdangky(account)
                if (response.isSuccessful) {
                    _isregister.value = true
                    Log.d("VM_Register", "Success")
                } else {
                    _isregister.value = false
                    when (response.code()) {
                        404 -> _registerErr.value = "Nhập thiếu thông tin"
                        409 -> _registerErr.value = "Tài khoản này đã tồn tại"
                        else -> _registerErr.value = "Đăng ký thất bại"
                    }
                    Log.d("VM_Register", "Not Success")
                }
            } catch (e: Exception) {
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
                        else -> _loginErr.value = "Đăng nhập thất bại"
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

    fun Test(test: Server.Test) {
        viewModelScope.launch {
            try {
                val response = RetrofitBanNoiThat().server.postgioHangVaChiTiet(test)
                if (response.isSuccessful) {
                    _GHErr.value = "Thêm vào giỏ hàng thành công"
                    Log.d("VM_gioHangVaChiTiet", "Success" + "${response.body()}")
                } else {
                    when (response.code()) {
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

    fun getGHCT(id: String) {
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

    fun PutGHCT(iduser: String, test: Server.Test) {
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

    fun getSlideshow() {
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

    fun getdel(id: String, iduser: String) {
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

    fun postDH(id: String, donHangReq: DonHangReq, role: Boolean) {
        viewModelScope.launch {
            try {
                val response = RetrofitBanNoiThat().server.postDH(id, donHangReq)
                if (response.isSuccessful) {
                    //Xóa giỏ hàng
                    _GioHangCT.value = emptyList()
                    getDH(id, role)
                    Log.d("VM_postDH", "Success")
                } else {
                    when (response.code()) {
                        400 -> _GHErr.value =
                            "Một số sản phẩm vượt quá số lượng tồn kho. Mình đã cập nhật lại số lượng trong giỏ hàng. Vui lòng kiểm tra lại giỏ hàng của bạn."

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

    fun getDH(id: String, role: Boolean) {
        viewModelScope.launch {
            try {
                val response = RetrofitBanNoiThat().server.getDH(id, role)
                if (response.isSuccessful) {
                    _getDH.value = response.body()
                    Log.d("VM_getDH", "Success")
                } else {
                    when (response.code()) {
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

    fun putDH(idUser: String, id: String, donHangPUT: DonHangPUT) {
        viewModelScope.launch {
            try {
                val response = RetrofitBanNoiThat().server.putDH(id, donHangPUT)
                if (response.isSuccessful) {
                    getDH(idUser, donHangPUT.role)
                    Log.d("VM_putDH", "Success")
                } else {
                    when (response.code()) {
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

    fun getDHCT(id: String) {
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

    fun postFav(id: String, yeuThich: YeuThich) {
        viewModelScope.launch {
            try {
                val response = RetrofitBanNoiThat().server.postFav(yeuThich)
                if (response.isSuccessful) {
                    getFavUser(id)
                    Log.d("postFav", "Success - ${response.message()}")
                } else {
                    Log.d(
                        "postFav",
                        "Not Success: ${response.code()} - ${response.message()}"
                    )
                }
            } catch (e: Exception) {
                Log.e("postFav", "Not Success ${e.message}")
            }
        }
    }


    fun gettotalFav() {
        viewModelScope.launch {
            try {
                val response = RetrofitBanNoiThat().server.gettotalFav()
                if (response.isSuccessful) {
                    _TotalFav.value = response.body()
                    Log.d("gettotalFav", "Success - ${response.message()}")
                } else {
                    Log.d(
                        "gettotalFav",
                        "Not Success: ${response.code()} - ${response.message()}"
                    )
                }
            } catch (e: Exception) {
                Log.e("gettotalFav", "Not Success ${e.message}")
            }
        }
    }

    fun getFav() {
        viewModelScope.launch {
            try {
                val response = RetrofitBanNoiThat().server.getFav()
                if (response.isSuccessful) {
                    Log.d("getFav", "Success - ${response.message()}")
                } else {
                    Log.d(
                        "getFav",
                        "Not Success: ${response.code()} - ${response.message()}"
                    )
                }
            } catch (e: Exception) {
                Log.e("getFav", "Not Success ${e.message}")
            }
        }
    }

    fun getFavUser(id: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitBanNoiThat().server.getFavUser(id)
                if (response.isSuccessful) {
                    _favUser.value = response.body()
                    Log.d("getFav", "Success - ${response.message()}")
                } else {
                    Log.d(
                        "getFav",
                        "Not Success: ${response.code()} - ${response.message()}"
                    )
                }
            } catch (e: Exception) {
                Log.e("getFav", "Not Success ${e.message}")
            }
        }
    }

    fun searchNoiThat(query: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitBanNoiThat().server.gettimKiem(query)
                if (response.isSuccessful) {
                    _searchResults.value = response.body() ?: emptyList()
                    Log.d("searchNoiThat", "Success - ${response.message()}")
                } else {
                    Log.d(
                        "searchNoiThat",
                        "Not Success: ${response.code()} - ${response.message()}"
                    )
                    _searchResults.value = emptyList()
                }
            } catch (e: Exception) {
                Log.e("searchNoiThat", "Not Success ${e.message}")
            }
        }
    }

    fun getnguoidung(id: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitBanNoiThat().server.getnguoidung(id)
                if (response.isSuccessful) {
                    _ngdungs.value = response.body()!!
                    Log.d("getFav", "Success - ${response.message()}")
                } else {
                    Log.d(
                        "getFav",
                        "Not Success: ${response.code()} - ${response.message()}"
                    )
                    _ngdungs.value = emptyList()
                }
            } catch (e: Exception) {
                Log.e("getFav", "Not Success ${e.message}")
            }
        }
    }

    fun getAllloaiNoiThat() {
        viewModelScope.launch {
            try {
                val response = RetrofitBanNoiThat().server.getAllloaiNoiThat()
                if (response.isSuccessful) {
                    _allloainoithat.value = response.body()
                    Log.d("VM_getnoiThats", "Success")
                } else {
                    _allloainoithat.value = emptyList()
                    Log.d(
                        "VM_getnoiThats",
                        "Not Success: ${response.code()} - ${response.message()}"
                    )
                }
            } catch (e: Exception) {
                Log.e("VM_getnoiThats", "Not Success ${e.message}")
            }
        }
    }

    fun posthdAndHdct(hoaDon: HoaDon) {
        viewModelScope.launch {
            try {
                val response = RetrofitBanNoiThat().server.posthdAndHdct(hoaDon)
                if (response.isSuccessful) {
                    Log.d("VM_hd", "Success")
                } else {
                    Log.d(
                        "VM_hd",
                        "Not Success: ${response.code()} - ${response.message()}"
                    )
                }
            } catch (e: Exception) {
                Log.e("VM_hd", "Not Success ${e.message}")
            }
        }
    }

    fun gethoadon() {
        viewModelScope.launch {
            try {
                val response = RetrofitBanNoiThat().server.gethoadon()
                if (response.isSuccessful) {
                    _gethoadon.value = response.body()
                    Log.d("gethd", "Success")
                } else {
                    Log.d(
                        "gethd",
                        "Not Success: ${response.code()} - ${response.message()}"
                    )
                    _gethoadon.value = emptyList()
                }
            } catch (e: Exception) {
                Log.e("gethd", "Not Success ${e.message}")
            }
        }
    }

    fun getthongtindonhang(id: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitBanNoiThat().server.getthongtindonhang(id)
                if (response.isSuccessful) {
                    _thongtindh.value = response.body()
                    Log.d("ttdh", "Success")
                } else {
                    Log.d(
                        "ttdh",
                        "Not Success: ${response.code()} - ${response.message()}"
                    )
                }
            } catch (e: Exception) {
                Log.e("ttdh", "Not Success ${e.message}")
            }
        }
    }

    fun gethdct(id: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitBanNoiThat().server.gethoadonchitiet(id)
                if (response.isSuccessful) {
                    _hdct.value = response.body()
                    Log.d("gethdct", "Success")
                } else {
                    Log.d(
                        "gethdct",
                        "Not Success: ${response.code()} - ${response.message()}"
                    )
                    _hdct.value = emptyList()
                }
            } catch (e: Exception) {
                Log.e("gethdct", "Not Success ${e.message}")
            }
        }
    }

    //-----------------gạch


    fun listForSocketId() {
        socketManger.onSocketIdReceived { socketId ->
            _socketID.postValue(socketId)
            Log.d("_socketID", "Success - ${_socketID.value}")

        }
    }

    fun updateSocket(updateSocket: updateSocket) {
        viewModelScope.launch {
            try {
                val response = RetrofitBanNoiThat().server.updateSocket(updateSocket)
                if (response.isSuccessful) {
                    Log.d("_socketID", "Success - ${response.body()}")
                    Log.d("updateSocket", "Success - ${response.message()}")
                } else {
                    Log.d(
                        "updateSocket",
                        "Not Success: ${response.code()} - ${response.message()}"
                    )
                }
            } catch (e: Exception) {
                Log.e("updateSocket", "Not Success ${e.message}")
            }
        }
    }

    fun emit(evt: String, arg: String) {
        val json = JSONObject()
        json.put(evt, arg)
        socketManger.emit(evt, json)
    }

    fun on(evt: String, callBack: (arg: JSONObject) -> Unit) {
        socketManger.on(evt) { json ->
            viewModelScope.launch {
                callBack(json)
            }
        }
    }

    fun sendMessage(sender: String, receiver: String, content: String, id: String, idU: String) {
        val message = JSONObject().apply {
            put("senderId", sender)
            put("receiverId", receiver)
            put("content", content)
        }
        socketManger.emit("message", message)
        Log.d(" message", "${message}")
        Log.d(" sender", "${sender}")
        Log.d(" receiver", "${receiver}")
        Log.d(" Gửi ", "gửi thêm lần gửi để load")

        requestMessages(id, idU)
//        listenForMessages()
    }

    fun listenForMessages() {
        socketManger.onMessagesListReceived { messagesArray ->
            val messagesList = mutableListOf<MessageR>()
            for (i in 0 until messagesArray.length()) {
                val messageObj = messagesArray.getJSONObject(i)
                val message = MessageR(
                    senderId = messageObj.getString("senderId"),
                    receiverId = messageObj.getString("receiverId"),
                    content = messageObj.getString("content")
                )
                messagesList.add(message)
            }
            Log.d("Gửi ", "LẮng nghe dư lieeuj")

//            COi ở danh sách load rồi từ từ mà xóa bớt hàm gọi list đi hoặc hàm gửi thêm thật nheiefu log và chú ý tới nó
            viewModelScope.launch {
                _messages.value = messagesList
                Log.d(" _messages.value", "${_messages.value}")
            }
        }
    }

    fun requestMessages(senderId: String, receiverId: String) {
        val messageRequest = JSONObject().apply {
            put("senderId", senderId)
            put("receiverId", receiverId)
        }
        Log.d("getMessages", "${messageRequest}")

        socketManger.emit("getMessages", messageRequest)
    }




}


//========r sẽ test lại thôi

//private val _chatRess = MutableLiveData<ChatRes>()
//val chatRess: LiveData<ChatRes> = _chatRess

//private val _textChatRess = MutableLiveData<List<Message>>()
//val textChatRess: LiveData<List<Message>> = _textChatRess

//fun postchat(chatReq: ChatReq) {
//    viewModelScope.launch {
//        try {
//            val response = RetrofitBanNoiThat().server.postchat(chatReq)
//            if (response.isSuccessful) {
//                _chatRess.value = response.body()
//                Log.d("postchat", "Success - ${response.message()}")
//            } else {
//                Log.d(
//                    "postchat",
//                    "Not Success: ${response.code()} - ${response.message()}"
//                )
//            }
//        } catch (e: Exception) {
//            Log.e("postchat", "Not Success ${e.message}")
//        }
//    }
//}
//
////    gửi tin nhắn lên để lấy danh sách tin nhắn
//fun sendMessage(idRoom: String, content: String) {
//    val message = JSONObject().apply {
//        put("idRoom", idRoom)
//        put("content", content)
//    }
//    Log.d("send", "${message}")
//    socketManger.emit("sendMessage", message)
//}
//
////KHông nhận được
////    nhận danh sách khi gửi tin nhắn
//fun listenForMessages() {
//    socketManger.onMessagesListReceived { messagesArray ->
//        val messagesList = mutableListOf<Message>()
//        for (i in 0 until messagesArray.length()) {
//            val messageObj = messagesArray.getJSONObject(i)
//            val message = Message(
//                content = messageObj.getString("content"),
//                _id = messageObj.getString("_id")
//            )
//            messagesList.add(message)
//        }
//        viewModelScope.launch {
//            _textChatRess.value = messagesList
//            Log.d(" _messages.value", "${_textChatRess.value}")
//        }
//    }
//}
//
//
////    Gửi để lấy danh sách
//fun getDanhSachTextChat(senderId: String, receiverId: String) {
//    val messageRequest = JSONObject().apply {
//        put("idSender", senderId)
//        put("idReceiver", receiverId)
//    }
//    Log.d("messageRequest", "${messageRequest}")
//    socketManger.emit("message", messageRequest)
//}
//
////    lấy danh sách của server trả về
//fun listenForMessage() {
//    socketManger.MessageGet { mes ->
//        val messageList = mutableListOf<Message>()
//        for (i in 0 until mes.length()) {
//            val messageObj = mes.getJSONObject(i)
//            val message = Message(
//                content = messageObj.getString("content"),
//                _id = messageObj.getString("_id")
//            )
//            Log.d("DDDDD", "MMMM")
//
//            messageList.add(message)
//        }
//        Log.d("DDDDD", "${messageList}")
//
//        viewModelScope.launch {
//            _textChatRess.value = messageList
//            Log.d("JJJJJ", "${_textChatRess.value}")
//        }
//    }
//}