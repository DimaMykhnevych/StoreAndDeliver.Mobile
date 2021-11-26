package com.example.storeanddeliver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.storeanddeliver.fragments.IndicatorsFragment
import com.example.storeanddeliver.fragments.Requests
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {
    private val requestsFragment = Requests()
    private val indicatorsFragment = IndicatorsFragment()
    private var bottomNavigation: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        replaceFragment(requestsFragment)
        bottomNavigation = findViewById(R.id.bottom_navigation)
        bottomNavigation?.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.ic_indicators -> replaceFragment(indicatorsFragment)
                R.id.ic_cargo -> replaceFragment(requestsFragment)
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