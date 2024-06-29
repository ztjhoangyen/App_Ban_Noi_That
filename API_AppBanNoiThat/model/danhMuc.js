const mongoose = require('mongoose')

const DanhMucsSchema = new mongoose.Schema({
    ten_danh_muc: {
        type: String
    }
})
module.exports = new mongoose.model('danhMucs', DanhMucsSchema)