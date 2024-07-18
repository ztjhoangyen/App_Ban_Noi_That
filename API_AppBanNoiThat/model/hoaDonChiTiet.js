const mongoose = require('mongoose')

const HoaDonChiTietSchema = new mongoose.Schema({
    hoa_don_id: {
        type: String,
        ref: 'hoaDons'
    },
    don_hang_ct_id: {
        type: String, 
        ref:'donHangChiTiet'
    }
},{
    timestamps: true
})
module.exports = new mongoose.model('hoaDonChiTiet', HoaDonChiTietSchema)