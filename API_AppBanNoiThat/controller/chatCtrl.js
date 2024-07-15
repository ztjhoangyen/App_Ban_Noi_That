const route = require('express').Router()

const chat = require('../model/chat')
const textChat = require('../model/textChat')
const io = require("socket.io")()

route.post("/chat", async function(req, res, next){
    try {
        const {idSender, idRecever} = req.body

        const updateObj = {
            idSender: idSender,
            idRecever: idRecever
        }
        const result = await chat.findOneAndUpdate(
            updateObj,
            {
              $set: updateObj,
            },
            { upsert: true, new: true }
          )
          res.status(200).json(result)
    } catch (error) {
        res.status(500).json(error)
    }
})

route.post("/textChat", async function(req, res, next){
    try {
        const {idRoom, content, idRecever, idSender} = req.body
       
        const objTextChat = await textChat.findOneAndUpdate(
            {idRoom: idRoom},
            {
                $push:{
                    message:{
                        content: content
                    },
                },
            },
            {upsert: true, new: true}
        )
        // Gửi tin nhắn mới tới client của người nhận

        io.to(idRecever).emit('newMessage', objTextChat.message);
        io.to(idSender).emit('newMessage', objTextChat.message);

        res.status(200).json(objTextChat.message)
    } catch (error) {
        res.status(500).json(error)
    }
})
// lấy dữ liệu đây rồi và giờ gửi vào ok
route.get('/messages', async function(req, res, next){
    try {
        const { idSender, idReceiver } = req.query
        console.log("idSender" + idSender);
        console.log("idReceiver" + idReceiver);

        const chats = await chat.findOne({
            idSender: idSender,
            idRecever: idReceiver
        });

        if (!chats) {
            console.log("chat" + chats);
            return res.status(404).json({ message: 'Không tìm thấy cuộc trò chuyện' });
        }

        const texts = await textChat.find({idRoom: chats._id})

        if (texts.length === 0) {   
            return res.status(404).json({ message: 'Không tìm thấy danh sách trò chuyện' });
        }
        console.log("texts" +  texts[0].message);

        res.status(200).json( texts[0].message);
    } catch (error) {
        console.error('Lỗi khi lấy tin nhắn:', error);
        res.status(500).json({ message: 'Lỗi server khi lấy tin nhắn' });
    }
});

module.exports = route