package com.rival.my_packet.adapter

import android.app.Fragment
import android.app.FragmentManager
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.rival.my_packet.ui.paket.DashboardPaketFragment
import com.rival.my_packet.ui.paket.MusyrifPaketFragment
import com.rival.my_packet.ui.paket.SatpamPaketFragment
import com.rival.my_packet.ui.paket.SelesaiPaketFragment

import kotlinx.android.synthetic.main.fragment_dashboard.*


class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): androidx.fragment.app.Fragment {
        return when (position) {
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


