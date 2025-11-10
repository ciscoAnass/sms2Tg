package com.example.smsforwarder

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var tvStatus: TextView
    private lateinit var btnRequest: Button

    // This is the new, modern way to ask for a permission
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission Granted! App is active.", Toast.LENGTH_LONG).show()
                updateUi() // Update the screen
            } else {
                Toast.makeText(this, "Permission Denied. App cannot work.", Toast.LENGTH_LONG).show()
                updateUi() // Update the screen
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Connects to our XML layout

        // Find the UI components from the XML by their ID
        tvStatus = findViewById(R.id.tvStatus)
        btnRequest = findViewById(R.id.btnRequest)

        // Set a click listener for the button
        btnRequest.setOnClickListener {
            // Launch the permission dialog
            requestPermissionLauncher.launch(Manifest.permission.RECEIVE_SMS)
        }

        // Check permission and update the UI as soon as the app opens
        updateUi()
    }

    // A helper function to check permission and change the text
    private fun updateUi() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECEIVE_SMS
            ) == PackageManager.PERMISSION_GRANTED -> {
                // Permission is GRANTED
                tvStatus.text = "SMS Forwarding is ACTIVE."
                btnRequest.visibility = View.GONE // Hide the button
            }
            else -> {
                // Permission is DENIED
                tvStatus.text = "App needs SMS permission to work."
                btnRequest.visibility = View.VISIBLE // Show the button
            }
        }
    }
}