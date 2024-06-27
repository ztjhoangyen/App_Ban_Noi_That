const mongoose = require('mongoose')

const yeuThichSchema = new mongoose.Schema({
    nguoi_dung_id: {
        type: String,
        ref: 'nguoiDungs'
    },
    noi_that_id: {
        type: String,
        ref: 'noiThats'
    }
}, {
    timestamps: true
})
module.exports = new mongoose.model('yeuThichs', yeuThichSchema)