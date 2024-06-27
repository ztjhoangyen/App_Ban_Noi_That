const mongoose = require('mongoose')

const GioHangsSchema = new mongoose.Schema({
    nguoi_dung_id: {
        type: String,
        ref: 'nguoiDungs'
    },
    tong_gia: {
        type: Number
    }
},{
    timestamps: true
})
module.exports = new mongoose.model('gioHangs', GioHangsSchema)