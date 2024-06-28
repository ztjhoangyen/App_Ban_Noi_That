const mongoose = require('mongoose')

const loaiNoiThatsSchema = new mongoose.Schema({
    ten_loai:{
        type: String
    },
    img: {
        type: String
    }
},{
    timestamps: true
})
module.exports = new mongoose.model('loaiNoiThats', loaiNoiThatsSchema)