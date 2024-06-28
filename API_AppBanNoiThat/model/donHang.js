const mongoose = require('mongoose')

const DonHangsSchema = new mongoose.Schema({
    nguoi_dung_id: {
        type: String,
        ref: 'nguoiDungs'
    },
    tong_gia: {
        type: Number
    },
    //  (Đang xử lý, Đã giao, Đã hủy, ...)
    trang_thai: {
        type: Array
    },
    phuong_thuc_thanh_toan: {
        type: Array
    },
    dia_chi_giao_hang: {
        type: Array
    },
    ghi_chu: {
        type: Number
    },
    trang_thai: {
        type: Array
    },
    tong_gia: {
        type: Number
    }
    // ma_giam_gia: {
    //     type: Number
    // }

},{
    timestamps: true
})
module.exports = new mongoose.model('donHangs', DonHangsSchema)