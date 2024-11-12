// MainActivity.kt
package com.example.emergencyalertapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var emergencyButton: Button
    private lateinit var safetyTipsButton: FloatingActionButton
    private lateinit var welcomeMessage: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        welcomeMessage = findViewById(R.id.welcomeMessage)
        emergencyButton = findViewById(R.id.emergencyButton)
        safetyTipsButton = findViewById(R.id.safetyTipsButton)

        updateWelcomeMessage()

        emergencyButton.setOnClickListener {
            if (checkPermissions()) {
                showEmergencyConfirmationDialog()
            } else {
                requestPermissions()
            }
        }

        safetyTipsButton.setOnClickListener {
            startActivity(Intent(this, SafetyTipsActivity::class.java))
        }
    }

    private fun updateWelcomeMessage() {
        val timeOfDay = when (java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)) {
            in 0..11 -> "Good morning"
            in 12..16 -> "Good afternoon"
            else -> "Good evening"
        }
        welcomeMessage.text = "$timeOfDay. Stay safe. We're here if you need us."
    }

    private fun showEmergencyConfirmationDialog() {
        // Show a dialog to confirm the emergency alert
        // If confirmed, call sendEmergencyAlert()
    }

    private fun sendEmergencyAlert() {
        val alertService = EmergencyAlertService(this)
        alertService.sendAlertToContacts()
        alertService.notifyPolice()
    }

    // ... (rest of the code remains the same)
}

// EmergencyAlertService.kt
package com.example.emergencyalertapp

import android.content.Context
import android.telephony.SmsManager
import android.util.Log
import androidx.core.content.ContextCompat

class EmergencyAlertService(private val context: Context) {

    fun sendAlertToContacts() {
        val message = context.getString(R.string.emergency_message)
        val contacts = getEmergencyContacts()
        
        contacts.forEach { contact ->
            try {
                SmsManager.getDefault().sendTextMessage(contact.phoneNumber, null, message, null, null)
                Log.d("EmergencyAlert", "Alert sent to ${contact.name}")
            } catch (e: Exception) {
                Log.e("EmergencyAlert", "Failed to send alert to ${contact.name}", e)
            }
        }
    }

    fun notifyPolice() {
        // Implement actual police notification
        // This could involve making an API call to an emergency service
        // or using a dedicated emergency calling functionality
        Log.d("EmergencyAlert", "Notifying police")
    }

    private fun getEmergencyContacts(): List<EmergencyContact> {
        // In a real app, this would fetch contacts from a database or shared preferences
        return listOf(
            EmergencyContact("Mom", "1234567890"),
            EmergencyContact("Dad", "0987654321")
        )
    }
}

data class EmergencyContact(val name: String, val phoneNumber: String)

// SafetyTipsActivity.kt
package com.example.emergencyalertapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class SafetyTipsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_safety_tips)

        val recyclerView: RecyclerView = findViewById(R.id.safetyTipsRecyclerView)
        // Set up RecyclerView with SafetyTipsAdapter
    }
}

// activity_main.xml
&lt;?xml version="1.0" encoding="utf-8"?&gt;
&lt;androidx.constraintlayout.widget.ConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity"&gt;

    &lt;TextView
        android:id="@+id/welcomeMessage"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textSize="18sp"
        android:textStyle="bold"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        android:layout_marginTop="32dp" /&gt;

    &lt;Button
        android:id="@+id/emergencyButton"
        android:layout_width="200dp"
        android:layout_height="200dp"
        android:text="@string/emergency_button_text"
        android:background="@drawable/round_button"
        android:textColor="@android:color/white"
        android:textSize="24sp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent" /&gt;

    &lt;com.google.android.material.floatingactionbutton.FloatingActionButton
        android:id="@+id/safetyTipsButton"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:src="@drawable/ic_info"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        android:layout_margin="16dp" /&gt;

&lt;/androidx.constraintlayout.widget.ConstraintLayout&gt;

// strings.xml
&lt;resources&gt;
    &lt;string name="app_name"&gt;SafeGuard&lt;/string&gt;
    &lt;string name="emergency_button_text"&gt;HELP&lt;/string&gt;
    &lt;string name="emergency_message"&gt;I\'m in an emergency situation. Please help! My last known location is: %1$s&lt;/string&gt;
&lt;/resources&gt;
