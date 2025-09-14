package com.example.tappercounter

import android.content.SharedPreferences
import android.view.View
import android.widget.EditText
import android.widget.Switch
import com.example.tappercounter.databinding.ActivitySettingsBinding
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.*

@RunWith(MockitoJUnitRunner::class)
class SettingsActivityTest {

    @Mock
    private lateinit var mockBinding: ActivitySettingsBinding

    @Mock
    private lateinit var mockSharedPreferences: SharedPreferences

    @Mock
    private lateinit var mockEditor: SharedPreferences.Editor

    @Mock
    private lateinit var mockSwitchSound: Switch

    @Mock
    private lateinit var mockSwitchGoal: Switch

    @Mock
    private lateinit var mockEditTextGoal: EditText

    private lateinit var settingsActivity: SettingsActivity

    @Before
    fun setUp() {
        whenever(mockSharedPreferences.edit()).thenReturn(mockEditor)
        whenever(mockEditor.putBoolean(any(), any())).thenReturn(mockEditor)
        whenever(mockEditor.putInt(any(), any())).thenReturn(mockEditor)

        whenever(mockBinding.switchSound).thenReturn(mockSwitchSound)
        whenever(mockBinding.switchGoal).thenReturn(mockSwitchGoal)
        whenever(mockBinding.editTextGoal).thenReturn(mockEditTextGoal)

        settingsActivity = SettingsActivity()

        setField(settingsActivity, "binding", mockBinding)
        setField(settingsActivity, "sharedPreferences", mockSharedPreferences)
    }

    @Test
    fun `loadSettings should load settings from SharedPreferences and update UI`() {
        whenever(mockSharedPreferences.getBoolean("soundEnabled", false)).thenReturn(true)
        whenever(mockSharedPreferences.getBoolean("goalEnabled", false)).thenReturn(true)
        whenever(mockSharedPreferences.getInt("goalValue", 100)).thenReturn(200)

        val loadSettingsMethod = settingsActivity::class.java.getDeclaredMethod("loadSettings")
        loadSettingsMethod.isAccessible = true
        loadSettingsMethod.invoke(settingsActivity)

        verify(mockSwitchSound).isChecked = true
        verify(mockSwitchGoal).isChecked = true
        verify(mockEditTextGoal).setText("200")
        verify(mockEditTextGoal).visibility = View.VISIBLE
    }

    @Test
    fun `onSoundSwitchChanged should save setting to SharedPreferences`() {
        settingsActivity.onSoundSwitchChanged(true)

        verify(mockEditor).putBoolean("soundEnabled", true)
        verify(mockEditor).apply()
    }

    @Test
    fun `onGoalSwitchChanged should save setting and update visibility`() {
        settingsActivity.onGoalSwitchChanged(true)

        verify(mockEditor).putBoolean("goalEnabled", true)
        verify(mockEditor).apply()
        verify(mockEditTextGoal).visibility = View.VISIBLE
    }

    @Test
    fun `onGoalTextChanged should save setting to SharedPreferences`() {
        settingsActivity.onGoalTextChanged("300")

        verify(mockEditor).putInt("goalValue", 300)
        verify(mockEditor).apply()
    }

    private fun setField(obj: Any, fieldName: String, value: Any) {
        val field = obj::class.java.getDeclaredField(fieldName)
        field.isAccessible = true
        field.set(obj, value)
    }
}
