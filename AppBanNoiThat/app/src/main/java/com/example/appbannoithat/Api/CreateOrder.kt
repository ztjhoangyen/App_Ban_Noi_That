package com.example.bai.Api


import com.example.bai.Constant.AppInfo
import com.example.bai.Helper.Helpers
import okhttp3.FormBody
import okhttp3.RequestBody
import org.json.JSONObject
import java.util.Date

class CreateOrder {
    private inner class CreateOrderData(amount: String) {
        var AppId: String
        var AppUser: String
        var AppTime: String
        var Amount: String
        var AppTransId: String
        var EmbedData: String
        var Items: String
        var BankCode: String
        var Description: String
        var Mac: String

        init {
            val appTime: Long = Date().time
            AppId = AppInfo.APP_ID.toString()
            AppUser = "Android_Demo"
            AppTime = appTime.toString()
            Amount = amount
            AppTransId = Helpers.appTransId
            EmbedData = "{}"
            Items = "[]"
            BankCode = "zalopayapp"
            Description = "Merchant pay for order #" + Helpers.appTransId
            val inputHMac = "${this.AppId}|${this.AppTransId}|${this.AppUser}|${this.Amount}|${this.AppTime}|${this.EmbedData}|${this.Items}"

            Mac = Helpers.getMac(AppInfo.MAC_KEY, inputHMac)
        }
    }

    @Throws(Exception::class)
    fun createOrder(amount: String): JSONObject {
        val input = CreateOrderData(amount)

        val formBody: RequestBody = FormBody.Builder()
            .add("app_id", input.AppId)
            .add("app_user", input.AppUser)
            .add("app_time", input.AppTime)
            .add("amount", input.Amount)
            .add("app_trans_id", input.AppTransId)
            .add("embed_data", input.EmbedData)
            .add("item", input.Items)
            .add("bank_code", input.BankCode)
            .add("description", input.Description)
            .add("mac", input.Mac)
            .build()

        val data: JSONObject = HttpProvider.sendPost(AppInfo.URL_CREATE_ORDER, formBody)!!
        return data
    }
}
