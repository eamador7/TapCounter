package com.example.tappercounter

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import com.example.tappercounter.databinding.ActivitySettingsBinding

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

    private fun loadSettings() {
        val soundEnabled = sharedPreferences.getBoolean("soundEnabled", false)
        val goalEnabled = sharedPreferences.getBoolean("goalEnabled", false)
        val goalValue = sharedPreferences.getInt("goalValue", 100)

        binding.switchSound.isChecked = soundEnabled
        binding.switchGoal.isChecked = goalEnabled
        binding.editTextGoal.setText(goalValue.toString())

        updateGoalVisibility(goalEnabled)
    }

    private fun setupListeners() {
        val editor = sharedPreferences.edit()

        binding.switchSound.setOnCheckedChangeListener { _, isChecked ->
            editor.putBoolean("soundEnabled", isChecked).apply()
        }

        binding.switchGoal.setOnCheckedChangeListener { _, isChecked ->
            editor.putBoolean("goalEnabled", isChecked).apply()
            updateGoalVisibility(isChecked)
        }

        binding.editTextGoal.addTextChangedListener { text ->
            val goalValue = text.toString().toIntOrNull() ?: 0
            editor.putInt("goalValue", goalValue).apply()
        }
    }

    private fun updateGoalVisibility(isVisible: Boolean) {
        binding.editTextGoal.visibility = if (isVisible) View.VISIBLE else View.GONE
    }
}
