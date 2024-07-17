package com.example.appbannoithat.socket

import android.util.Log
import com.example.appbannoithat.config.ipconfig
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.net.URISyntaxException

class SocketManger {
    private var socket: Socket? = null

    init {
        try {
            socket = IO.socket(ipconfig.MYIP)
            Log.d("Đã ok sockket", "Success - ")

        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }

    fun connect() {
        socket?.connect()
    }

    fun disconnect() {
        socket?.disconnect()
    }

    fun emit(event: String, message: JSONObject) {
        socket?.emit(event, message)
    }

    fun on(event: String, listener: (JSONObject) -> Unit) {
        socket?.on(event) {
            listener(it[0] as JSONObject)
        }
    }

    fun onSocketIdReceived(listener: (String) -> Unit) {
        socket?.on("socketId") { args ->
            if (args.isNotEmpty() && args[0] is String) {
                listener(args[0] as String)
            }
        }
    }
//    nhận danh sách tin nhắn
    fun onMessagesListReceived(listener: (JSONArray) -> Unit) {
        socket?.on("messagesList") { args ->
            if (args.isNotEmpty() && args[0] is JSONArray) {
                listener(args[0] as JSONArray)
            }
        }
    }

}

//=================== rồi sẽ test lại thôi


////nhận danh sách tin nhắn
//fun onList(listener: (JSONArray) -> Unit) {
//    socket?.on("getmessages") { args ->
//        if (args.isNotEmpty() && args[0] is JSONArray) {
//            listener(args[0] as JSONArray)
//        }
//    }
//}
//
////      nhận danh sách tin nhắn gửi
//fun onMessagesListReceived(listener: (JSONArray) -> Unit) {
//    socket?.on("newMessage") { args ->
//        if (args.isNotEmpty() && args[0] is JSONArray) {
//            listener(args[0] as JSONArray)
//        }
//    }
//}
//
////      nhận danh sách lúc gửi yêu cầu lấy danh sach
//fun MessageGet(listener: (JSONArray) -> Unit) {
//    socket?.on("getmessages") { args ->
//        if (args.isNotEmpty() && args[0] is JSONArray) {
//            listener(args[0] as JSONArray)
//        }
//    }
//    Log.d("socketmagea", "socketmagea")
//}