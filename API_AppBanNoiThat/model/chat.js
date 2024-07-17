const mongoose = require('mongoose')

const ChatSchemas = new mongoose.Schema({
    idSender: {
        type: String
    },
    idRecever:{
        type: String
    }
})
module.exports = new mongoose.model('chat', ChatSchemas)