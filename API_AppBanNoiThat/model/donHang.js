const mongoose = require('mongoose')

const DonHangsSchema = new mongoose.Schema({
    nguoi_dung_id: {
        type: String,
        ref: 'nguoiDungs'
    },
    tong_gia: {
        type: Number
    },
    so_luong: {
        type: Number
    },
    //  (Đang xử lý, Đã giao, Đã hủy, ...)
    trang_thai: {
        type: String
    },
    phuong_thuc_thanh_toan: {
        type: String
    },
    dia_chi_giao_hang: {
        type: String
    },
    ghi_chu: {
        type: String
    },
    img:{
        type: String
    },
    tinh_trang:{
        type: String
    }
    // ma_giam_gia: {
    //     type: Number
    // }

},{
    timestamps: true
})
module.exports = new mongoose.model('donHangs', DonHangsSchema)