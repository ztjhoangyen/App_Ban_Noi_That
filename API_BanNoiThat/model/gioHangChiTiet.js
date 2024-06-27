const mongoose = require('mongoose')

const GioHangChiTietsSchema = new mongoose.Schema({
    gio_hang_id: {
        type: String,
        ref: 'gioHangs'
    },
    noi_that_id: {
        type: String,
        ref: 'noiThats'
    },
    so_luong: {
        type: Number
    },
    gia: {
        type: String
    }
},{
    timestamps: true
})
module.exports = new mongoose.model('gioHangChiTiet', GioHangChiTietsSchema)