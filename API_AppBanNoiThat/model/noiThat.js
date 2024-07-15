const mongoose = require('mongoose')

const NoiThatsSchema = new mongoose.Schema({
    ten_noi_that: {
        type: String
    },
    mo_ta: {
        type: String
    },
    gia: {
        type: Number
    },
    so_luong: {
        type: Number
    },
    // hinh_anh: {
    //     type: String
    // },
    hinh_anh: {
        type: Array
    },
    loai_noi_that_id: {
        type: String,
        ref: 'loaiNoiThats'
    },
    ngay_nhap_kho: {
        type: Date,
        default: Date.now
    }
})
module.exports = new mongoose.model('noiThats', NoiThatsSchema)