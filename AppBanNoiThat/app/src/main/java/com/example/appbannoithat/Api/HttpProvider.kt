package com.example.bai.Api

import android.util.Log
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.CipherSuite.Companion.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256
import okhttp3.CipherSuite.Companion.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256
import okhttp3.CipherSuite.Companion.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256
import okhttp3.TlsVersion
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

object HttpProvider {
    fun sendPost(URL: String?, formBody: RequestBody?): JSONObject? {
        requireNotNull(URL) { "URL không được là null" }
        requireNotNull(formBody) { "formBody không được là null" }

        var data: JSONObject? = JSONObject()
        try {
            val spec: ConnectionSpec = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_2)
                .cipherSuites(
                    TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                    TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                    TLS_DHE_RSA_WITH_AES_128_GCM_SHA256
                )
                .build()

            val client: OkHttpClient = OkHttpClient.Builder()
                .connectionSpecs(listOf(spec))
                .callTimeout(5, TimeUnit.SECONDS)
                .build()

            val request: Request = Request.Builder()
                .url(URL)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .post(formBody)
                .build()

            val response = client.newCall(request).execute()

            if (!response.isSuccessful) {
                val responseBody = response.body?.string()
                if (responseBody != null) {
                    Log.println(Log.ERROR, "BAD_REQUEST", responseBody)
                } else {
                    Log.println(Log.ERROR, "ERROR", "Response body is null")
                }
                data = null
            } else {
                val responseBody = response.body?.string()
                if (responseBody != null) {
                    data = JSONObject(responseBody)
                } else {
                    Log.println(Log.ERROR, "ERROR", "Response body is null")
                }
            }

        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return data
    }
}
