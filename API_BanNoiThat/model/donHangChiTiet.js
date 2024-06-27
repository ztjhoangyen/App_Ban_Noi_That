const mongoose = require('mongoose')

const DonHangChiTietsSchema = new mongoose.Schema({
    don_hang_id: {
        type: String,
        ref: 'donHangs'
    },
    noi_that_id: {
        type: String,
        ref: 'noiThats'
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
module.exports = new mongoose.model('donHangChiTiet', DonHangChiTietsSchema)