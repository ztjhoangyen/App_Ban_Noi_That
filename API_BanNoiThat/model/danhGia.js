const mongoose = require('mongoose')

const DanhGiasSchema = new mongoose.Schema({
    nguoi_dung_id: {
        type: String,
        ref: 'nguoiDungs'
    },
    noi_that_id: {
        type: String,
        ref: 'noiThats'
    },
    diem: {
        type: Number
    },
    binh_luan: {
        type: String
    }
},{
    timestamps: true
})
module.exports = new mongoose.model('danhGias', DanhGiasSchema)