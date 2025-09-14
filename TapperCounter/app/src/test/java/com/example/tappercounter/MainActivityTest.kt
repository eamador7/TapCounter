package com.example.tappercounter

import android.content.SharedPreferences
import android.graphics.Color
import android.media.SoundPool
import android.os.Vibrator
import android.widget.TextView
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.tappercounter.databinding.ActivityMainBinding
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.*

@RunWith(MockitoJUnitRunner::class)
class MainActivityTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mockBinding: ActivityMainBinding

    @Mock
    private lateinit var mockSharedPreferences: SharedPreferences

    @Mock
    private lateinit var mockEditor: SharedPreferences.Editor

    @Mock
    private lateinit var mockVibrator: Vibrator

    @Mock
    private lateinit var mockSoundPool: SoundPool

    @Mock
    private lateinit var mockTextView: TextView

    private lateinit var mainActivity: MainActivity

    @Before
    fun setUp() {
        whenever(mockBinding.textViewCounter).thenReturn(mockTextView)
        whenever(mockSharedPreferences.edit()).thenReturn(mockEditor)
        whenever(mockEditor.putInt(any(), any())).thenReturn(mockEditor)

        mainActivity = MainActivity()

        setField(mainActivity, "binding", mockBinding)
        setField(mainActivity, "sharedPreferences", mockSharedPreferences)
        setField(mainActivity, "vibrator", mockVibrator)
        setField(mainActivity, "soundPool", mockSoundPool)
        setField(mainActivity, "count", 0)
        setField(mainActivity, "soundUpId", 1)
        setField(mainActivity, "soundDownId", 2)
        setField(mainActivity, "soundGoalId", 3)
    }

    @Test
    fun `handleIncrement should increment count and update UI`() {
        whenever(mockSharedPreferences.getBoolean("soundEnabled", false)).thenReturn(true)

        mainActivity.handleIncrement()

        assert(getField(mainActivity, "count") as Int == 1)
        verify(mockTextView).text = "1"
        verify(mockVibrator).vibrate(any<Long>())
        verify(mockSoundPool).play(1, 1f, 1f, 0, 0, 1f)
    }

    @Test
    fun `handleDecrement should decrement count and update UI`() {
        setField(mainActivity, "count", 1)
        whenever(mockSharedPreferences.getBoolean("soundEnabled", false)).thenReturn(true)

        mainActivity.handleDecrement()

        assert(getField(mainActivity, "count") as Int == 0)
        verify(mockTextView).text = "0"
        verify(mockVibrator).vibrate(any<Long>())
        verify(mockSoundPool).play(2, 1f, 1f, 0, 0, 1f)
    }

    @Test
    fun `handleReset should reset count and update UI`() {
        setField(mainActivity, "count", 5)

        mainActivity.handleReset()

        assert(getField(mainActivity, "count") as Int == 0)
        verify(mockTextView).text = "0"
    }

    @Test
    fun `onStop should save current count to SharedPreferences`() {
        setField(mainActivity, "count", 10)

        mainActivity.onStop()

        verify(mockEditor).putInt("currentCount", 10)
        verify(mockEditor).apply()
    }

    @Test
    fun `loadState should load count from SharedPreferences`() {
        whenever(mockSharedPreferences.getInt("currentCount", 0)).thenReturn(20)
        val loadStateMethod = mainActivity::class.java.getDeclaredMethod("loadState")
        loadStateMethod.isAccessible = true

        loadStateMethod.invoke(mainActivity)

        assert(getField(mainActivity, "count") as Int == 20)
        verify(mockTextView).text = "20"
    }

    @Test
    fun `checkGoalReached should trigger goal notifications when goal is met`() {
        setField(mainActivity, "count", 100)
        whenever(mockSharedPreferences.getBoolean("goalEnabled", false)).thenReturn(true)
        whenever(mockSharedPreferences.getInt("goalValue", 0)).thenReturn(100)
        whenever(mockSharedPreferences.getBoolean("soundEnabled", false)).thenReturn(true)
        val checkGoalReachedMethod = mainActivity::class.java.getDeclaredMethod("checkGoalReached")
        checkGoalReachedMethod.isAccessible = true

        checkGoalReachedMethod.invoke(mainActivity)

        verify(mockSoundPool).play(3, 1f, 1f, 0, 0, 1f)
        verify(mockVibrator).vibrate(200L)
        verify(mockTextView).setBackgroundColor(Color.GREEN)
    }

    private fun setField(obj: Any, fieldName: String, value: Any) {
        val field = obj::class.java.getDeclaredField(fieldName)
        field.isAccessible = true
        field.set(obj, value)
    }

    private fun getField(obj: Any, fieldName: String): Any {
        val field = obj::class.java.getDeclaredField(fieldName)
        field.isAccessible = true
        return field.get(obj)
    }
}
