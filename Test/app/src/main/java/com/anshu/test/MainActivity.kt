package com.anshu.test

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.anshu.test.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnSubmit = findViewById<Button>(R.id.button_first)
        val editTextName = findViewById<EditText>(R.id.editText_name)
        val textView = findViewById<TextView>(R.id.textView)

        btnSubmit.setOnClickListener {
            val name = editTextName.text.toString().trim()

            if(name.isBlank())
            {
                Toast.makeText(this, "Please enter your name!", Toast.LENGTH_SHORT).show()
            }
            else if(!name.matches(Regex("^[a-zA-Z]+( [a-zA-Z]+)?$")))
            {
                Toast.makeText(this, "Enter a valid name using only letters!", Toast.LENGTH_SHORT).show()
            }
            else{
                textView.text = "Hi, Mr./Mrs. $name"
            }

        }
    }
}