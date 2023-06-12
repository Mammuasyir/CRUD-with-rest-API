package com.rival.my_packet

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController

import com.rival.my_packet.databinding.ActivityMainBinding
import com.rival.my_packet.helper.NetworkConnection
import com.rival.my_packet.helper.SharedPreference
import com.rival.my_packet.ui.dashboard.DashboardFragment
import com.rival.my_packet.ui.home.HomeFragment
import com.rival.my_packet.ui.notifications.LoginFragment
import com.rival.my_packet.ui.notifications.NotificationsFragment

class MainActivity : AppCompatActivity() {

    val fragmentHome: Fragment = HomeFragment()
    val fragmentDashboard: Fragment = DashboardFragment()
    val fragmentAccount: Fragment = NotificationsFragment()
    val fragmentLogin: Fragment = LoginFragment()
    val fm: FragmentManager = supportFragmentManager
    var active: Fragment = fragmentHome

    private lateinit var menu: Menu
    private lateinit var menuItem: MenuItem
    private lateinit var bottomNavigationView: BottomNavigationView

    //    private var statusLogin = false
    private lateinit var sPH: SharedPreference

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sPH = SharedPreference(this)
        setUpBottomNav()

        val networkConnection = NetworkConnection(this)
        networkConnection.observe(this, { isConnected ->
            if (isConnected) {
                Toast.makeText(this, "Network is available", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Network is not available", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setUpBottomNav() {
        fm.beginTransaction()
            .add(R.id.container, fragmentHome)
            .add(R.id.container, fragmentDashboard)
            .add(R.id.container, fragmentAccount)
            .add(R.id.container, fragmentLogin)
            .hide(fragmentDashboard)
            .hide(fragmentAccount)
            .hide(fragmentLogin)
            .show(fragmentHome)
            .commit()

        bottomNavigationView = binding.navView
        menu = bottomNavigationView.menu
        menuItem = menu.getItem(0)
        menuItem.isChecked = true

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    callFragment(0, fragmentHome)
                    onRestart()
                }
                R.id.navigation_dashboard -> {
                    if (sPH.getStatusLogin()) {
                        callFragment(1, fragmentDashboard)
                    } else {
                        callFragment(1, fragmentLogin)
                    }
                    onRestart()
                }
                R.id.navigation_notifications -> {
                    onRestart()
                    if (sPH.getStatusLogin()) {
                        callFragment(2, fragmentAccount)
                    } else {
                        callFragment(2, fragmentLogin)
                    }
                }
            }
            true
        }
    }

    private fun callFragment(index: Int, fragment: Fragment) {
        menuItem = menu.getItem(index)
        menuItem?.isChecked = true
        fm.beginTransaction().apply {
            hide(active)
            show(fragment)
            commit()
        }
        active = fragment
    }


    override fun onResume() {
        super.onResume()
        // Kode untuk memanipulasi UI
    }
}