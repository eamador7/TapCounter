package com.example.tappercounter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tappercounter.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set initial count text
        updateCounterText()

        // Set up click listeners
        binding.rightTapArea.setOnClickListener {
            count++
            updateCounterText()
        }

        binding.leftTapArea.setOnClickListener {
            count--
            updateCounterText()
        }

        binding.buttonReset.setOnClickListener {
            count = 0
            updateCounterText()
        }

        binding.buttonExit.setOnClickListener {
            finish()
        }
    }

    private fun updateCounterText() {
        binding.textViewCounter.text = count.toString()
    }
}
