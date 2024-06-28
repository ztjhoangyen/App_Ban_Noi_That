const mongoose = require('mongoose')

const GioHangsSchema = new mongoose.Schema({
    nguoi_dung_id: {
        type: String,
        ref: 'nguoiDungs'
    },
    // tong_gia: {
    //     type: Number
    // },
    // 1 tạo mới giỏ hàng (chưa thanh toán), 0 đã thanh toán(đã thanh toán)
    trang_thai:{
        type: Number,
        default: 1
    }
},{
    timestamps: true
})
module.exports = new mongoose.model('gioHangs', GioHangsSchema)