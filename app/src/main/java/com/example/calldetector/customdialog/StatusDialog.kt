package com.example.calldetector.customdialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.calldetector.R
import com.example.calldetector.util.SharedPreferenceHelper

@SuppressLint("MissingInflatedId")
class StatusDialog(context:Context):Dialog(context)
{
    private var editText:EditText
    private var saveTxtBtn:TextView
    private var cancelTxtBtn:TextView
    private var sharedPreferenceHelper:SharedPreferenceHelper

    init {
        setContentView(R.layout.status_dialog)
        editText = findViewById(R.id.input_et)
        saveTxtBtn = findViewById(R.id.btn_save)
        cancelTxtBtn=findViewById(R.id.btn_cancel)

        sharedPreferenceHelper=SharedPreferenceHelper(context)

        saveTxtBtn.setOnClickListener {addStatus()}
        cancelTxtBtn.setOnClickListener {closeDialog()}

    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

    }

    private fun addStatus()
    {
        val text=editText.text.toString()

        if(text=="")
        {
            Toast.makeText(context, "field is empty", Toast.LENGTH_SHORT).show()
        }
        else
        {
            sharedPreferenceHelper.setStatus(text)
            Toast.makeText(context, "message saved successfully", Toast.LENGTH_SHORT).show()
            editText.setText("")
            dismiss()
        }

    }

    private fun closeDialog()
    {
        editText.setText("")
        dismiss()
    }
}