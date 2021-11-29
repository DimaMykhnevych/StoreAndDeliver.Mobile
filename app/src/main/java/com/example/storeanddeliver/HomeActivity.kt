package com.example.storeanddeliver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.UserManager
import androidx.fragment.app.Fragment
import com.example.storeanddeliver.fragments.IndicatorsFragment
import com.example.storeanddeliver.fragments.Requests
import com.example.storeanddeliver.fragments.SettingsFragment
import com.example.storeanddeliver.managers.UserSettingsManager
import com.example.storeanddeliver.utils.ContextUtils
import com.example.storeanddeliver.utils.ContextUtils.Companion.setLocale
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

class HomeActivity : AppCompatActivity() {
    private val requestsFragment = Requests()
    private val indicatorsFragment = IndicatorsFragment()
    private val settingsFragment = SettingsFragment(null)
    private var bottomNavigation: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLocale(resources, UserSettingsManager.currentLocale)
        setContentView(R.layout.activity_home)
        replaceFragment(requestsFragment)
        bottomNavigation = findViewById(R.id.bottom_navigation)
        bottomNavigation?.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.ic_indicators -> replaceFragment(indicatorsFragment)
                R.id.ic_cargo -> replaceFragment(requestsFragment)
                R.id.ic_settings -> replaceFragment(settingsFragment)
            }
            true
        }

    }

    private fun replaceFragment(fragment: Fragment){
        if(fragment != null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()
        }
    }
}