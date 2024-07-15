package com.example.appbannoithat.Model

data class ChatReq(
    val idSender : String,
    val idRecever : String
)

data class ChatRes(
    val _id : String,
    val idSender : String,
    val idRecever : String
)

data class textChatReq(
    val idRoom : String,
    val content : String,
    val idRecever: String,
    val idSender: String
)

data class Message(
    val content: String
)

data class TextChatRess(
    val messages: List<Message>
)