package com.example.covidtracker

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity

class SupportActivity : AppCompatActivity() {
    private var mEditTextTo: EditText? = null
    private var mEditTextSubject: EditText? = null
    private var mEditTextMessage: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_support)
        mEditTextTo = findViewById(R.id.edit_text_to)
        mEditTextSubject = findViewById(R.id.edit_text_subject)
        mEditTextMessage = findViewById(R.id.edit_text_message)
        val buttonSend = findViewById<Button>(R.id.button_send)
        buttonSend.setOnClickListener { sendMail() }
    }

    private fun sendMail() {
        val recipientList = mEditTextTo!!.text.toString()
        val recipients = recipientList.split(",".toRegex()).toTypedArray()
        val subject = mEditTextSubject!!.text.toString()
        val message = mEditTextMessage!!.text.toString()
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_EMAIL, recipients)
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        intent.putExtra(Intent.EXTRA_TEXT, message)
        intent.type = "message/rfc822"
        startActivity(Intent.createChooser(intent, "Choose an email client"))
    }
}