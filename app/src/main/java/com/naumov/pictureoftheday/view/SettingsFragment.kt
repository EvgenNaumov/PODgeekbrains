package com.naumov.pictureoftheday.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.naumov.pictureoftheday.R
import com.naumov.pictureoftheday.databinding.FragmentSettingsBinding
import com.naumov.pictureoftheday.ui.MainActivity
import com.naumov.pictureoftheday.utils.*

class SettingsFragment:Fragment() , View.OnClickListener{

    private var _binding : FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    lateinit var parentActivity:MainActivity

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
       fun newInstance() = SettingsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        parentActivity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setClickChipGroup()
        val sharedPreferences = parentActivity.getSharedPreferences(KEY_SP, AppCompatActivity.MODE_PRIVATE)
        val currentTheme = sharedPreferences.getInt(KEY_CURRENT_THEME,1)
        when (currentTheme) {
            THEME1 -> binding.chipGroupSetting.check(R.id.chip_theme1)
            THEME2 -> binding.chipGroupSetting.check(R.id.chip_theme2)
            THEME3 -> binding.chipGroupSetting.check(R.id.chip_theme3)
            else -> binding.chipGroupSetting.check(R.id.chip_theme1)
        }
    }

    private fun setClickChipGroup() {
        binding.chipTheme1.setOnClickListener(this)
        binding.chipTheme2.setOnClickListener(this)
        binding.chipTheme3.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.chip_theme1-> {
                parentActivity.setCurrenyTheme(THEME1)
                parentActivity.recreate()
            }
            R.id.chip_theme2->{
                parentActivity.setCurrenyTheme(THEME2)
                parentActivity.recreate()
            }
            R.id.chip_theme3->{
                parentActivity.setCurrenyTheme(THEME3)
                parentActivity.recreate()
            }
        }
    }

}