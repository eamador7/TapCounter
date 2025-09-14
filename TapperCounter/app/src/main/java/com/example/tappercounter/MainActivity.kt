package com.example.tappercounter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import com.example.tappercounter.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var count = 0
    private lateinit var vibrator: Vibrator
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var soundPool: SoundPool
    private var soundUpId: Int = 0
    private var soundDownId: Int = 0
    private var soundGoalId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        sharedPreferences = getSharedPreferences("TapperCounterPrefs", Context.MODE_PRIVATE)

        setupSoundPool()
        loadSounds()

        // Load the saved count
        count = sharedPreferences.getInt("currentCount", 0)

        // Set initial count text
        updateCounterText()

        // Set up click listeners
        binding.rightTapArea.setOnClickListener {
            count++
            updateCounterText()
            vibrate()
            playSound(soundUpId)
            checkGoalReached()
        }

        binding.leftTapArea.setOnClickListener {
            count--
            updateCounterText()
            vibrate()
            playSound(soundDownId)
            checkGoalReached()
        }

        binding.buttonReset.setOnClickListener {
            count = 0
            updateCounterText()
        }

        binding.buttonExit.setOnClickListener {
            finish()
        }

        binding.buttonSettings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStop() {
        super.onStop()
        // Save the current count
        val editor = sharedPreferences.edit()
        editor.putInt("currentCount", count)
        editor.apply()
    }

    private fun setupSoundPool() {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        soundPool = SoundPool.Builder()
            .setMaxStreams(3) // Increased for goal sound
            .setAudioAttributes(audioAttributes)
            .build()
    }

    private fun loadSounds() {
        // These will cause an error until the files are added to res/raw
        // See res/raw/README.md for instructions
        soundUpId = soundPool.load(this, R.raw.tick_up, 1)
        soundDownId = soundPool.load(this, R.raw.tick_down, 1)
        soundGoalId = soundPool.load(this, R.raw.goal_reached, 1)
    }

    private fun updateCounterText() {
        binding.textViewCounter.text = count.toString()
    }

    private fun vibrate(duration: Long = 50) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(duration)
        }
    }

    private fun playSound(soundId: Int) {
        if (sharedPreferences.getBoolean("soundEnabled", false)) {
            soundPool.play(soundId, 1f, 1f, 0, 0, 1f)
        }
    }

    private fun checkGoalReached() {
        val goalEnabled = sharedPreferences.getBoolean("goalEnabled", false)
        if (goalEnabled) {
            val goalValue = sharedPreferences.getInt("goalValue", 0)
            if (count == goalValue) {
                // Goal reached! Trigger special notifications.
                playSound(soundGoalId) // Play goal sound in addition to tap sound
                vibrate(200)       // Vibrate longer
                flashGoalIndicator()
            }
        }
    }

    private fun flashGoalIndicator() {
        binding.textViewCounter.setBackgroundColor(Color.GREEN)
        Handler(Looper.getMainLooper()).postDelayed({
            binding.textViewCounter.setBackgroundColor(Color.TRANSPARENT)
        }, 500)
    }
}
