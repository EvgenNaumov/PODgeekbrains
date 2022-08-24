package com.naumov.pictureoftheday.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.naumov.pictureoftheday.R
import com.naumov.pictureoftheday.databinding.ActivityMainBinding
import com.naumov.pictureoftheday.utils.*
import com.naumov.pictureoftheday.view.PictureOfTheDayFragment
import com.naumov.pictureoftheday.viewmodel.PictureOfTheDayData

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setTheme(getRealStyle(getCurrentTheme()))
        setCurrenyTheme(getCurrentTheme())

        var binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.container,PictureOfTheDayFragment.newInstance())
                .commitNow()
        }
    }

    fun setCurrenyTheme(currentTheme:Int){
        val sharedPreferences = getSharedPreferences(KEY_SP, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt(KEY_CURRENT_THEME, currentTheme)
        editor.apply()
    }

    fun getCurrentTheme():Int{
        val sp = getSharedPreferences(KEY_SP, Context.MODE_PRIVATE)
        return sp.getInt(KEY_CURRENT_THEME,1)
    }

    private fun getRealStyle(currentTheme: Int): Int {
        return when (currentTheme) {
            THEME1 -> R.style.Theme_MainTheme
            THEME2 -> R.style.Theme_Luna
            THEME3 -> R.style.Theme_Mars
            else -> R.style.Theme_MainTheme
        }
    }

}