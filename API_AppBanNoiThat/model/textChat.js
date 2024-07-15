const mongoose = require('mongoose')

const textChatSchema = new mongoose.Schema({
    idRoom : {
        type: String
    },
    message:[{
        content: String
    }]
})

module.exports = new mongoose.model('textChat', textChatSchema)