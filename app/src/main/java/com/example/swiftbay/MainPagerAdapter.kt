package com.example.swiftbay

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


internal class MainPagerAdapter(fm: FragmentManager?) :
        FragmentPagerAdapter(fm!!) {
    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = LoginTabFragment()
            1 -> fragment = SignupTabFragment()
        }
        return fragment!!
    }

    override fun getCount(): Int {
        return 2
    }
}