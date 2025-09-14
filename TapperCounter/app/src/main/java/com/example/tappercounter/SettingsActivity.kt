package com.example.tappercounter

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import com.example.tappercounter.databinding.ActivitySettingsBinding

/**
 * Manages the user-configurable settings for the application.
 * All settings are saved to SharedPreferences as they are changed.
 */
class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("TapperCounterPrefs", Context.MODE_PRIVATE)

        loadSettings()
        setupListeners()
    }

    /**
     * Loads all setting values from SharedPreferences and updates the UI controls.
     */
    private fun loadSettings() {
        val soundEnabled = sharedPreferences.getBoolean("soundEnabled", false)
        val goalEnabled = sharedPreferences.getBoolean("goalEnabled", false)
        val goalValue = sharedPreferences.getInt("goalValue", 100)

        binding.switchSound.isChecked = soundEnabled
        binding.switchGoal.isChecked = goalEnabled
        binding.editTextGoal.setText(goalValue.toString())

        updateGoalVisibility(goalEnabled)
    }

    /**
     * Sets up listeners for each UI control to save the setting value as soon as it changes.
     */
    private fun setupListeners() {
        binding.switchSound.setOnCheckedChangeListener { _, isChecked ->
            onSoundSwitchChanged(isChecked)
        }

        binding.switchGoal.setOnCheckedChangeListener { _, isChecked ->
            onGoalSwitchChanged(isChecked)
        }

        binding.editTextGoal.addTextChangedListener { text ->
            onGoalTextChanged(text.toString())
        }
    }

    internal fun onSoundSwitchChanged(isChecked: Boolean) {
        sharedPreferences.edit().putBoolean("soundEnabled", isChecked).apply()
    }

    internal fun onGoalSwitchChanged(isChecked: Boolean) {
        sharedPreferences.edit().putBoolean("goalEnabled", isChecked).apply()
        updateGoalVisibility(isChecked)
    }

    internal fun onGoalTextChanged(text: String) {
        val goalValue = text.toIntOrNull() ?: 0
        sharedPreferences.edit().putInt("goalValue", goalValue).apply()
    }

    /**
     * Shows or hides the EditText for the target goal based on its corresponding switch.
     */
    private fun updateGoalVisibility(isVisible: Boolean) {
        binding.editTextGoal.visibility = if (isVisible) View.VISIBLE else View.GONE
    }
}
