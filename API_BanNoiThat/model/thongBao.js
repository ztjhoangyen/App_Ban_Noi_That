const mongoose = require('mongoose')

const ThongBaosSchema = new mongoose.Schema({
    don_hang_id: {
        type: String,
        ref: 'donHangs'
    },
    noi_dung: {
        type: String
    },
    trang_thai_giao: {
        type: String
    }
},{
    timestamps: true
})
module.exports = new mongoose.model('thongbaos', ThongBaosSchema)