package com.rival.my_packet.adapter

import android.app.Fragment
import android.app.FragmentManager
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.rival.my_packet.helper.SharedPreference
import com.rival.my_packet.ui.paket.DashboardPaketFragment
import com.rival.my_packet.ui.paket.MusyrifPaketFragment
import com.rival.my_packet.ui.paket.SatpamPaketFragment
import com.rival.my_packet.ui.paket.SelesaiPaketFragment

import kotlinx.android.synthetic.main.fragment_dashboard.*


class ViewPagerAdapter(fragmentActivity: FragmentActivity,
                        private val sPH: SharedPreference
) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = if (sPH.getUser()?.role == "Admin") 4 else 3

    override fun createFragment(position: Int): androidx.fragment.app.Fragment {
        if (sPH.getUser()?.role == "Satpam") {
            return when (position) {
                // if role == "satpam"
                0 -> {
                    DashboardPaketFragment()
                }
                1 -> {
                    SatpamPaketFragment()
                }
                else -> {
                    SelesaiPaketFragment()
                }
            }
        } else if (sPH.getUser()?.role == "Musyrif") {
            return when (position) {
                // if role == "musyrif"
                0 -> {
                    DashboardPaketFragment()
                }
                1 -> {
                    MusyrifPaketFragment()
                }
                else -> {
                    SelesaiPaketFragment()
                }
            }

        } else {
            return when (position) {
                // if role == "admin"
                0 -> {
                    DashboardPaketFragment()
                }
                1 -> {
                    SatpamPaketFragment()
                }
                2 -> {
                    MusyrifPaketFragment()
                }
                else -> {
                    SelesaiPaketFragment()
                }
            }
        }
    }
}


