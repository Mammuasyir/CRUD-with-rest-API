package com.rival.my_packet

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController

import com.rival.my_packet.databinding.ActivityMainBinding
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


    }

    private fun setUpBottomNav() {

        fm.beginTransaction().add(R.id.container, fragmentHome).show(fragmentHome).commit()
        fm.beginTransaction().add(R.id.container, fragmentDashboard).hide(fragmentDashboard).commit()
        fm.beginTransaction().add(R.id.container, fragmentAccount).hide(fragmentAccount).commit()
        fm.beginTransaction().add(R.id.container, fragmentLogin).hide(fragmentLogin).commit()

//        bottomNavigationView = findViewById(R.id.nav_view)
        bottomNavigationView = binding.navView
        menu = bottomNavigationView.menu
        menuItem = menu.getItem(0)
        menuItem.isChecked = true

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->

            when(item.itemId){
                R.id.navigation_home -> {
                    callFragment(0, fragmentHome)
                }

                R.id.navigation_dashboard -> {
                    callFragment(1, fragmentDashboard)
                }

                R.id.navigation_notifications -> {

                    if (sPH.getStatusLogin()){
                        callFragment(2, fragmentAccount)
                    }else{
                       callFragment(2, fragmentLogin)
//                        Toast.makeText(this,"Login Dulu !", Toast.LENGTH_SHORT).show()
                    }

                }
            }

            false
        }

    }

    private fun callFragment(index: Int, fragment: Fragment) {
        menuItem = menu.getItem(index)
        menuItem.isChecked = true
        fm.beginTransaction().hide(active).show(fragment).commit()
        active = fragment
    }
}