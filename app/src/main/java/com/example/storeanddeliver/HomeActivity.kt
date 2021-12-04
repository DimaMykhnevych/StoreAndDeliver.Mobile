package com.example.storeanddeliver

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.UserManager
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.example.storeanddeliver.constants.Roles
import com.example.storeanddeliver.fragments.CarrierRequestsFragment
import com.example.storeanddeliver.fragments.IndicatorsFragment
import com.example.storeanddeliver.fragments.Requests
import com.example.storeanddeliver.fragments.SettingsFragment
import com.example.storeanddeliver.managers.CredentialsManager
import com.example.storeanddeliver.managers.UserSettingsManager
import com.example.storeanddeliver.models.CargoRequest
import com.example.storeanddeliver.utils.ContextUtils
import com.example.storeanddeliver.utils.ContextUtils.Companion.setLocale
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*

class HomeActivity : AppCompatActivity() {
    private val requestsFragment = Requests()
    private val indicatorsFragment = IndicatorsFragment()
    private val settingsFragment = SettingsFragment(null)
    private lateinit var carrierRequestsFragment: CarrierRequestsFragment
    private var bottomNavigation: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        carrierRequestsFragment = CarrierRequestsFragment(onMapBtnClick)
        setLocale(resources, UserSettingsManager.currentLocale)
        setContentView(R.layout.activity_home)
        bottomNavigation = findViewById(R.id.bottom_navigation)
        if (CredentialsManager.role == Roles.Carrier) {
            bottomNavigation?.menu?.removeItem(R.id.ic_indicators)
            bottomNavigation?.menu?.removeItem(R.id.ic_cargo)
            replaceFragment(carrierRequestsFragment)
        } else if (CredentialsManager.role == Roles.User) {
            bottomNavigation?.menu?.removeItem(R.id.ic_carrier_requests)
            replaceFragment(requestsFragment)
        }
        bottomNavigation?.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.ic_indicators -> replaceFragment(indicatorsFragment)
                R.id.ic_carrier_requests -> replaceFragment(carrierRequestsFragment)
                R.id.ic_cargo -> replaceFragment(requestsFragment)
                R.id.ic_settings -> replaceFragment(settingsFragment)
            }
            true
        }

    }

    private val onMapBtnClick: (CargoRequest) -> Unit = { cargoRequest ->
        val intent = Intent(this@HomeActivity, MapsActivity::class.java)
        intent.putExtra("cargoRequest", Json.encodeToString(cargoRequest))
        startActivity(intent)
    }

    private fun replaceFragment(fragment: Fragment) {
        if (fragment != null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()
        }
    }
}