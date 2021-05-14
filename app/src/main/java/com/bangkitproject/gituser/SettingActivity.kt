package com.bangkitproject.gituser

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Switch
import androidx.core.view.isVisible
import com.bangkitproject.gituser.Alarm.AlarmReceiver

class SettingActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var setreminderbtn: Switch
    private lateinit var alarmReceiver: AlarmReceiver

    companion object{
        const val PREFS_NAME = "Settings"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "GitUser - Setting"
        alarmReceiver = AlarmReceiver()
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        setreminderbtn = findViewById(R.id.switch1)
        Settingup()

        setreminderbtn.setOnCheckedChangeListener {_, isChecked->
            if (isChecked){
                alarmReceiver.setReminder(this, AlarmReceiver.EXTRA_DAILY, "Hei, have you open the app today?")
            }else{
                alarmReceiver.unsetReminder(this)
            }
            setChange(isChecked)
        }

    }

    private fun Settingup() {
        setreminderbtn.isChecked = sharedPreferences.getBoolean("BOOLEAN_KEY",false)
    }


    fun setChange(checked: Boolean) {
        val edit = sharedPreferences.edit()
                edit.apply{
                putBoolean("BOOLEAN_KEY",checked )
            }.apply()
    }

}


