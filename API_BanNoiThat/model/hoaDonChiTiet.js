const mongoose = require('mongoose')

const HoaDonChiTietSchema = new mongoose.Schema({
    hoa_don_id: {
        type: String,
        ref: 'hoaDons'
    },
    noi_that_id: {
        type: String, 
        ref:'noiThats'
    },
    so_luong: {
        type: Number
    },
    gia: {
        type: Number
    }
},{
    timestamps: true
})
module.exports = new mongoose.model('hoaDonChiTiet', HoaDonChiTietSchema)