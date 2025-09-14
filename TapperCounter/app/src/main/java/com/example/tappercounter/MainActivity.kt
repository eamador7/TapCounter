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

/**
 * The main screen of the Tapper Counter application.
 * Handles user taps to increment/decrement a counter and provides access to settings.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var count = 0
    private lateinit var vibrator: Vibrator
    private lateinit var sharedPreferences: SharedPreferences

    // Sound-related properties
    private lateinit var soundPool: SoundPool
    private var soundUpId: Int = 0
    private var soundDownId: Int = 0
    private var soundGoalId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize core components
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        sharedPreferences = getSharedPreferences("TapperCounterPrefs", Context.MODE_PRIVATE)

        // Setup audio playback
        setupSoundPool()
        loadSounds()

        // Restore previous state
        loadState()

        // Setup listeners for all UI interactions
        setupListeners()
    }

    override fun onStop() {
        super.onStop()
        // Save the current count when the app is no longer visible
        val editor = sharedPreferences.edit()
        editor.putInt("currentCount", count)
        editor.apply()
    }

    private fun loadState() {
        count = sharedPreferences.getInt("currentCount", 0)
        updateCounterText()
    }

    private fun setupListeners() {
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

    private fun setupSoundPool() {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        soundPool = SoundPool.Builder()
            .setMaxStreams(3)
            .setAudioAttributes(audioAttributes)
            .build()
    }

    private fun loadSounds() {
        // NOTE: This will cause a compile error until the sound files are added.
        // See res/raw/README.md for instructions on how to add the required assets.
        soundUpId = soundPool.load(this, R.raw.tick_up, 1)
        soundDownId = soundPool.load(this, R.raw.tick_down, 1)
        soundGoalId = soundPool.load(this, R.raw.goal_reached, 1)
    }

    private fun updateCounterText() {
        binding.textViewCounter.text = count.toString()
    }

    /**
     * Triggers a short vibration.
     * @param duration The duration of the vibration in milliseconds.
     */
    private fun vibrate(duration: Long = 50) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(duration)
        }
    }

    /**
     * Plays a sound from the SoundPool, if sound effects are enabled in settings.
     * @param soundId The ID of the sound to play.
     */
    private fun playSound(soundId: Int) {
        if (sharedPreferences.getBoolean("soundEnabled", false)) {
            soundPool.play(soundId, 1f, 1f, 0, 0, 1f)
        }
    }

    /**
     * Checks if the current count has reached the user-defined target goal and triggers notifications if so.
     * The goal-reached notifications are played in addition to the standard tap feedback.
     */
    private fun checkGoalReached() {
        val goalEnabled = sharedPreferences.getBoolean("goalEnabled", false)
        if (goalEnabled) {
            val goalValue = sharedPreferences.getInt("goalValue", 0)
            if (count == goalValue) {
                playSound(soundGoalId)
                vibrate(200) // Longer vibration for emphasis
                flashGoalIndicator()
            }
        }
    }

    /**
     * Briefly flashes the background of the counter to indicate the goal has been met.
     */
    private fun flashGoalIndicator() {
        // For a more theme-friendly implementation, this color could be defined as a theme attribute.
        binding.textViewCounter.setBackgroundColor(Color.GREEN)
        Handler(Looper.getMainLooper()).postDelayed({
            binding.textViewCounter.setBackgroundColor(Color.TRANSPARENT)
        }, 500)
    }
}
