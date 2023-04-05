package com.rival.my_packet.ui.dashboard


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.rival.my_packet.R

import com.rival.my_packet.adapter.ViewPagerAdapter
import com.rival.my_packet.databinding.FragmentDashboardBinding
import com.rival.my_packet.helper.SharedPreference


class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewPagerTab()

        return root
    }

    private fun viewPagerTab() {
        val sPH = SharedPreference(requireActivity())
        tabLayout = binding.tabLayout
        viewPager = binding.viewPager
        viewPager.adapter = ViewPagerAdapter(requireActivity(), sPH)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->

            if (sPH.getUser()?.role == "Satpam") {
                when (position) {
                    0 -> tab.text = "Dashboard"
                    1 -> tab.text = "POS Satpam"
                    2 -> tab.text = "Selesai"
                }
            } else if (sPH.getUser()?.role == "Musyrif") {
                when (position) {
                    0 -> tab.text = "Dashboard"
                    1 -> tab.text = "R. Musyrif"
                    2 -> tab.text = "Selesai"
                }
            } else
            when (position) {
                0 -> tab.text = "Dash"
                1 -> tab.text = "Satpam"
                2 -> tab.text = "Musyrif"
                3 -> tab.text = "Selesai"
            }
        }.attach()
    }


}