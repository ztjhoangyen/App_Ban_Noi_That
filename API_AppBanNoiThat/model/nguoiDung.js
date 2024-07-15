const mongoose = require('mongoose')

const NguoiDungsSchema = new mongoose.Schema({
    ten_tai_khoan:{
        type: String,
        minlength: 10,
        maxlength: 15,
        unique: true
    },
    email: {
        type: String,
        unique: true
    },
    mat_khau: {
        type: String,
        minlength: 8,
        maxlength: 15,
        require: true,
    },
    ho_ten: {
        type: String
    },
    dia_chi: {
        type: String
    },
    so_dien_thoai: {
        type: String
    },
    role:{
        type: Boolean,
        default: false
    },
    socketId:{
        type: String
    }
},{
    timestamps: true
})
module.exports = new mongoose.model('nguoiDungs', NguoiDungsSchema)