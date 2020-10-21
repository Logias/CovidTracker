package com.example.covidtracker.covid_data

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.covidtracker.databinding.FragmentCovidDataBinding
import com.google.android.material.tabs.TabLayoutMediator
import java.util.*

class CovidDataFragment : Fragment() {

    private lateinit var binding: FragmentCovidDataBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCovidDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPaper()
    }

    // setup the tablayout and viewpager
    private fun setupViewPaper() {
        var adapter = FeedPagerAdapter(childFragmentManager,lifecycle)
        binding.feedViewPager.adapter = adapter

        var tabTitles = arrayOf("Country","States")

        TabLayoutMediator(binding.tabLayout, binding.feedViewPager){ tab, position ->
            tab.text = tabTitles[position]
        }.attach()

    }

}

// pager adapter for the viewpager
class FeedPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    // the two fragments inside the viewpager, the user feed and the friends feed
    private val fragments:ArrayList<Fragment> = arrayListOf(
        CountryDataFragment(),
        StatesDataFragment()
    )

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}