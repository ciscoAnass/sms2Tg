package com.example.smsforwarder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SmsReceiver : BroadcastReceiver() {

    private val TAG = "SmsReceiver"

    // ++++++++++ PASTE YOUR SECRETS HERE ++++++++++
    companion object {
        // 1. PUT YOUR NEW BOT TOKEN HERE (inside the " ")
        const val BOT_TOKEN = ""

        // 2. PUT YOUR PERSONAL CHAT ID HERE (inside the " ")
        const val CHAT_ID = ""
    }
    // +++++++++++++++++++++++++++++++++++++++++++++


    override fun onReceive(context: Context, intent: Intent) {

        Log.d(TAG, "onReceive triggered! Checking action...")

        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {

            Log.d(TAG, "SMS_RECEIVED_ACTION confirmed.")

            if (BOT_TOKEN.isBlank() || CHAT_ID.isBlank() || BOT_TOKEN == "YOUR_BOT_TOKEN_HERE") {
                Log.e(TAG, "BOT_TOKEN or CHAT_ID is not set in SmsReceiver.kt! Stopping.")
                return
            }

            // --- Parse the SMS message ---
            val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            val stringBuilder = StringBuilder()
            var sender = ""

            messages.forEach { smsMessage ->
                sender = smsMessage.displayOriginatingAddress
                stringBuilder.append(smsMessage.messageBody)
            }

            val fullMessageBody = stringBuilder.toString()
            val messageToSend = "New SMS from: $sender\n\n$fullMessageBody"

            Log.d(TAG, "Message parsed. Preparing to send to Telegram...")

            // --- Forward the message (on a background thread) ---
            val pendingResult = goAsync()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    // We send the hard-coded token and ID
                    TelegramForwarder.sendMessage(BOT_TOKEN, CHAT_ID, messageToSend)
                    Log.d(TAG, "SUCCESS! Message forwarded to Telegram.")
                } catch (e: Exception) {
                    Log.e(TAG, "Error sending message to Telegram", e)
                } finally {
                    pendingResult.finish()
                }
            }
        }
    }
}
