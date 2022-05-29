package com.example.storeanddeliver

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.UserManager
import android.view.View
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.example.storeanddeliver.constants.Constants
import com.example.storeanddeliver.constants.Roles
import com.example.storeanddeliver.fragments.*
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
    private val carriersManagementFragment = CarriersManagementFragment()
    private val storeManagementFragment = StoreManagementFragment()
    private lateinit var carrierRequestsFragment: CarrierRequestsFragment
    private var bottomNavigation: BottomNavigationView? = null
    private var companyAdminBottomNavigation: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        carrierRequestsFragment = CarrierRequestsFragment(onMapBtnClick, onAddPhotoClick)
        setLocale(resources, UserSettingsManager.currentLocale)
        setContentView(R.layout.activity_home)
        bottomNavigation = findViewById(R.id.bottom_navigation)
        companyAdminBottomNavigation = findViewById(R.id.company_admin_bottom_navigation)
        setupMenuItems()

        bottomNavigation?.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.ic_indicators -> replaceFragment(indicatorsFragment)
                R.id.ic_carrier_requests -> replaceFragment(carrierRequestsFragment)
                R.id.ic_cargo -> replaceFragment(requestsFragment)
                R.id.ic_settings -> replaceFragment(settingsFragment)
                R.id.ic_carriers_management -> replaceFragment(carrierRequestsFragment)
                R.id.ic_company_admin_settings -> replaceFragment(settingsFragment)
            }
            true
        }

        companyAdminBottomNavigation?.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.ic_carriers_management -> replaceFragment(carriersManagementFragment)
                R.id.ic_company_admin_settings -> replaceFragment(settingsFragment)
                R.id.ic_store_management -> replaceFragment(storeManagementFragment)
            }
            true
        }

    }

    private fun setupMenuItems() {
        if (CredentialsManager.role == Roles.CompanyAdmin) {
            companyAdminBottomNavigation?.visibility = View.VISIBLE
            bottomNavigation?.visibility = View.GONE
            replaceFragment(carriersManagementFragment)
        } else {
            when (CredentialsManager.role) {
                Roles.Carrier -> {
                    bottomNavigation?.menu?.removeItem(R.id.ic_indicators)
                    bottomNavigation?.menu?.removeItem(R.id.ic_cargo)
                    companyAdminBottomNavigation?.visibility = View.GONE
                    bottomNavigation?.visibility = View.VISIBLE
                    replaceFragment(carrierRequestsFragment)
                }
                Roles.User -> {
                    bottomNavigation?.menu?.removeItem(R.id.ic_carrier_requests)
                    companyAdminBottomNavigation?.visibility = View.GONE
                    bottomNavigation?.visibility = View.VISIBLE
                    replaceFragment(requestsFragment)
                }
            }
        }
    }

    private val onMapBtnClick: (CargoRequest) -> Unit = { cargoRequest ->
        val intent = Intent(this@HomeActivity, MapsActivity::class.java)
        intent.putExtra("cargoRequest", Json.encodeToString(cargoRequest))
        startActivity(intent)
    }

    private val onAddPhotoClick: (String) -> Unit = { cargoRequestId ->
        val intent = Intent(this@HomeActivity, UploadPhotoActivity::class.java)
        intent.putExtra(Constants.cargoRequestIdKey, cargoRequestId)
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