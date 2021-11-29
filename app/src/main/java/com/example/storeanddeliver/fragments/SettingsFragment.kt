package com.example.storeanddeliver.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.storeanddeliver.R
import com.example.storeanddeliver.databinding.FragmentSettingsBinding
import com.example.storeanddeliver.managers.UserSettingsManager
import com.example.storeanddeliver.utils.ContextUtils.Companion.setLocale


class SettingsFragment(onLanguageChange: (() -> Unit)?) : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val onLanguageChangeCallback = onLanguageChange
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        setSelectedLanguage()
        binding.langEn.setOnClickListener(radioButtonClickListener)
        binding.langUa.setOnClickListener(radioButtonClickListener)
        binding.langRu.setOnClickListener(radioButtonClickListener)
        return binding.root
    }

    private fun setSelectedLanguage() {
        when (UserSettingsManager.currentLanguage) {
            "ua" -> binding.langUa.isChecked = true
            "en" -> binding.langEn.isChecked = true
            "ru" -> binding.langRu.isChecked = true
        }
    }

    private fun reloadFragment() {
        var currentFragment = this
        var ft: FragmentTransaction = activity!!.supportFragmentManager.beginTransaction();
        ft.detach(currentFragment);
        ft.attach(currentFragment);
        ft.commit();
        onLanguageChangeCallback?.let { it() }
    }

    private var radioButtonClickListener =
        View.OnClickListener { v ->
            val rb = v as RadioButton
            var locale = ""
            when (rb.id) {
                R.id.lang_en -> locale = "en"
                R.id.lang_ru -> locale = "ru"
                R.id.lang_ua -> locale = "uk"
                else -> {
                }
            }
            UserSettingsManager.currentLanguage = locale
            if (locale == "uk") {
                UserSettingsManager.currentLanguage = "ua"
            }
            setLocale(resources, locale)
            UserSettingsManager.currentLocale = locale
            rb.isChecked = true
            reloadFragment()
        }

}