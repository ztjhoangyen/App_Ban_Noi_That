const mongoose = require('mongoose')

const ChatSchemas = new mongoose.Schema({
    idSender: {
        type: String
    },
    idRecever:{
        type: String
    }
})
// gửi dữ liệu thay vì gửi tất cả thì gửi riêng, trước tiên là gửi tất cả
module.exports = new mongoose.model('chat', ChatSchemas)