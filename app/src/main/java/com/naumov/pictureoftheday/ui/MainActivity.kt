package com.naumov.pictureoftheday.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.naumov.pictureoftheday.R
import com.naumov.pictureoftheday.databinding.ActivityMainBinding
import com.naumov.pictureoftheday.view.PictureOfTheDayFragment
import com.naumov.pictureoftheday.viewmodel.PictureOfTheDayData

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.container,PictureOfTheDayFragment.newInstance())
                .commitNow()
        }
    }
}