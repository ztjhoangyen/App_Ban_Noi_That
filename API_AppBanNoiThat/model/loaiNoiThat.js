const mongoose = require('mongoose')

const loaiNoiThatsSchema = new mongoose.Schema({
    ten_loai:{
        type: String
    },
    img: {
        type: String
    },
    id_danh_muc:{
        type: String, 
        ref: 'danhMucs'
    }
},{
    timestamps: true
})
module.exports = new mongoose.model('loaiNoiThats', loaiNoiThatsSchema)