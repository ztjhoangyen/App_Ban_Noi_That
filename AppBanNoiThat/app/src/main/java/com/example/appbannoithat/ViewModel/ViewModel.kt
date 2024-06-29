package com.example.appbannoithat.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appbannoithat.Model.DanhMuc
import com.example.appbannoithat.Model.GioHangReq
import com.example.appbannoithat.Model.LoaiNoiThat
import com.example.appbannoithat.Model.NoiThat
import com.example.appbannoithat.Server.RetrofitBanNoiThat
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

    //thong bao
    private val _loaiNTErr = MutableLiveData<String>()
    val loaiNTErr: LiveData<String> = _loaiNTErr


    init {
        getDM()
    }

    fun setSortState(state: SortState) {
        _currentSortState.value = state
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
                    Log.d("VM_nthat", "Success")
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
}