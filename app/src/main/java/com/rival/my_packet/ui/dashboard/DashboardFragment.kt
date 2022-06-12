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
        tabLayout = binding.tabLayout
        viewPager = binding.viewPager
        viewPager.adapter = ViewPagerAdapter(requireActivity())
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.icon = ContextCompat.getDrawable(requireActivity() ,R.drawable.ic_vector)
                1 -> tab.icon = ContextCompat.getDrawable(requireActivity() ,R.drawable.ic_satpam)
                2 -> tab.icon = ContextCompat.getDrawable(requireActivity() ,R.drawable.ic_musyrif)
                3 -> tab.icon = ContextCompat.getDrawable(requireActivity() ,R.drawable.ic_selesai)
            }
        }.attach()
    }


}