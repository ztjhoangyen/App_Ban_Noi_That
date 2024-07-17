const mongoose = require('mongoose')

const messagesSchema = new mongoose.Schema({
    senderId: {
        type: String
    },
    receiverId: {
        type: String
    },
    content: {
        type: String
    }
}, {
    timestamps: true
})

module.exports = new mongoose.model('message', messagesSchema)