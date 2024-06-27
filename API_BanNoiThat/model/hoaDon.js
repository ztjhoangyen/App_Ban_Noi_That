const mongoose = require('mongoose')

const HoaDonsSchema = new mongoose.Schema({
    don_hang_id: {
        type: String,
        ref: 'donHangs'
    },
    ngay_thanh_toan: {
        type: String
    },
    phuong_thuc_thanh_toan: {
        type: String
    },
    tong_gia: {
        type: Number
    },
    ma_hoa_don: {
        type: String
    }
},{
    timestamps: true
})
module.exports = new mongoose.model('hoaDons', HoaDonsSchema)