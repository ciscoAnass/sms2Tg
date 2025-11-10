package com.example.smsforwarder

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

// This is an 'object' (a singleton)
// We just use it as a helper, no need to create instances
object TelegramForwarder {

    // A 'suspend fun' is a special function for background coroutines
    suspend fun sendMessage(token: String, chatId: String, text: String) {
        // Run this on the 'IO' (Input/Output) background thread
        withContext(Dispatchers.IO) {
            try {
                // Change "Hello world!" into "Hello%20world!"
                val textEncoded = URLEncoder.encode(text, "UTF-8")

                // Build the final URL
                val urlString = "https://api.telegram.org/bot$token/sendMessage?chat_id=$chatId&text=$textEncoded"
                val url = URL(urlString)

                // Make the web request
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"

                // This line actually sends it. We don't care about the response.
                connection.responseCode
                connection.disconnect()

            } catch (e: Exception) {
                // Something went wrong (no internet?)
                e.printStackTrace()
            }
        }
    }
}