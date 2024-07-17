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
    val content: String,
    val _id: String
)

//lấy danh sách user
data class UserResponse(
    val status: Boolean,
    val result: List<Account>
)

data class MessageR(
    val senderId: String,
    val receiverId: String,
    val content: String
)