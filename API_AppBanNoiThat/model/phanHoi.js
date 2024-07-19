const mongoose = require('mongoose')

const PhanHoisSchema = new mongoose.Schema({
    danh_gia_id: {
        type: String,
        ref: 'danhGias'
    },
    nguoi_dung_id: {
        type: String,
        ref: 'nguoiDungs'
    },
    noi_dung: {
        type: String
    }
},{
    timestamps: true
})
module.exports = new mongoose.model('phanhois', PhanHoisSchema)