package com.example.tappercounter

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import com.example.tappercounter.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var count = 0
    private lateinit var vibrator: Vibrator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        // Set initial count text
        updateCounterText()

        // Set up click listeners
        binding.rightTapArea.setOnClickListener {
            count++
            updateCounterText()
            vibrate()
        }

        binding.leftTapArea.setOnClickListener {
            count--
            updateCounterText()
            vibrate()
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

    private fun vibrate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            //deprecated in API 26
            @Suppress("DEPRECATION")
            vibrator.vibrate(50)
        }
    }
}
