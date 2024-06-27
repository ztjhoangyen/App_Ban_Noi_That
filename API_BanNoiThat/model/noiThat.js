const mongoose = require('mongoose')

const NoiThatsSchema = new mongoose.Schema({
    ten_noi_that:{
        type: SVGStringList
    },
    mo_ta: {
        type: String
    },
    gia: {
        type: Number
    },
    so_luong: {
        type: Number
    },
    hinh_anh:{
        type: String
    },
    loai_noi_that_id:{
        type: String,
        ref:'loaiNoiThats'
    }
},{
    timestamps: true
})
module.exports = new mongoose.model('noiThats', NoiThatsSchema)