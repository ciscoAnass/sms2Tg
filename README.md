# SMS2Tg

## Introduction

SMS Forwarder is a robust Android application designed to automatically forward incoming SMS messages to a Telegram chat using a bot. This application is ideal for users who need to monitor messages remotely, receive alerts from specific numbers, or integrate SMS notifications with other systems via Telegram.

The app is built entirely in Kotlin and leverages modern Android APIs to ensure smooth performance, secure permission handling, and reliable background operations.

![Screenshot](screenshot.png)

## Features

* **Automatic SMS Forwarding:** No need to manually check messages.
* **Telegram Integration:** Forwarded messages are sent instantly to your Telegram account.
* **Modern Permission Handling:** Uses Android's `ActivityResultContracts` for runtime permissions.
* **Asynchronous Networking:** Network calls are handled using Kotlin Coroutines for non-blocking operations.
* **Detailed Logging:** Logs the forwarding process for easier debugging.
* **Lightweight & Efficient:** Minimal background resource consumption.

## Prerequisites

Ensure you have:

* An Android device running API level 23 or higher.
* A stable Internet connection on the device.
* A Telegram account.
* A Telegram bot token (obtainable from [@BotFather](https://t.me/BotFather) on Telegram).
* Your personal Telegram chat ID (can be retrieved using your bot or the `getUpdates` method of the Telegram Bot API).
* Android Studio installed to build and run the app from source.
* Basic knowledge of Kotlin and Android app structure.

## Installation

1. Clone or download the repository to your local machine.
2. Open the project in Android Studio.
3. In `SmsReceiver.kt`, replace the placeholder constants with your bot token and chat ID:

```kotlin
companion object {
    const val BOT_TOKEN = "YOUR_BOT_TOKEN_HERE"
    const val CHAT_ID = "YOUR_CHAT_ID_HERE"
}
```

4. Sync the project to install dependencies.
5. Build and run the app on your Android device.
6. Grant SMS permissions when prompted.

## How It Works

The app functions through a combination of Android BroadcastReceivers and background coroutines:

1. **Permission Handling:**

   * The app checks for the `RECEIVE_SMS` permission on startup.
   * If not granted, it prompts the user to allow permission via a dialog.

2. **Listening for SMS:**

   * `SmsReceiver.kt` listens for the `Telephony.Sms.Intents.SMS_RECEIVED_ACTION` broadcast.
   * When an SMS is received, it extracts the sender and message content.

3. **Forwarding Messages to Telegram:**

   * The message is formatted with the sender's number and the message body.
   * `TelegramForwarder.kt` sends the message to the Telegram Bot API using an HTTP GET request.
   * Network operations are handled asynchronously with Kotlin Coroutines on the `Dispatchers.IO` context to avoid blocking the main thread.

4. **Background Processing:**

   * The app uses `goAsync()` in the BroadcastReceiver to allow asynchronous execution even while the receiver finishes its initial call.

## Back-End Architecture

The app’s back-end is fully self-contained on the Android device:

* **SmsReceiver.kt:**

  * Core component that listens for SMS broadcasts.
  * Validates the bot token and chat ID.
  * Prepares and forwards SMS content to Telegram.

* **TelegramForwarder.kt:**

  * Singleton object that sends HTTP requests to the Telegram Bot API.
  * URL encoding ensures special characters in SMS messages are properly transmitted.
  * Handles exceptions to prevent crashes during network errors.

* **MainActivity.kt:**

  * Manages the user interface.
  * Checks and requests SMS permissions.
  * Provides real-time feedback on permission status.

* **Coroutines:**

  * Ensures that network requests run off the main thread.
  * Provides non-blocking execution to maintain app responsiveness.

## Permissions

The following permissions are required:

* `INTERNET` – Required to send messages to Telegram.
* `RECEIVE_SMS` – Required to detect incoming SMS.
* `READ_SMS` – Required to read the SMS content.

Ensure these permissions are declared in `AndroidManifest.xml`.

## Dependencies

```kotlin
dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.activity:activity-ktx:1.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")
}
```

## Usage Instructions

1. Open the app and check the permission status.
2. Grant SMS permissions if requested.
3. Once active, incoming SMS messages will automatically appear in your Telegram chat.
4. Monitor all SMS messages remotely without opening your phone.

## Troubleshooting

* **Messages not forwarding:** Ensure that the bot token and chat ID are correctly set.
* **Permission issues:** Check that the app has `RECEIVE_SMS` permission granted.
* **Network issues:** Make sure the device is connected to the Internet.

## Security & Privacy

* The app only forwards SMS messages that arrive on the device.
* No SMS data is stored locally beyond standard system storage.
* Messages are sent directly to Telegram using HTTPS.

## Contributing

Contributions are welcome! To contribute:

1. Fork the repository.
2. Create a new branch with your changes.
3. Submit a pull request with a detailed description of your improvements.

## License

This project is licensed under the MIT License. You are free to use, modify, and distribute it for personal or commercial purposes.
