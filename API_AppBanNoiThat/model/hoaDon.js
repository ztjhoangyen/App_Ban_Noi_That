const mongoose = require('mongoose')

const HoaDonsSchema = new mongoose.Schema({
    don_hang_id: {
        type: String,
        ref: 'donHangs'
    }
},{
    timestamps: true
})
module.exports = new mongoose.model('hoaDons', HoaDonsSchema)