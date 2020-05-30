package com.jeon.pagingsample.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class PagerAdapter (fragmentManager: FragmentManager): FragmentStatePagerAdapter(fragmentManager,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val tabTitles = arrayOf("리스트","즐겨찾기")
    companion object{
        const val MAIN_TAB_POSITON = 0
    }
    override fun getItem(position: Int): Fragment {
        return if(MAIN_TAB_POSITON ==position){
            MainFragment.newInstance()
        }else {
            LikeFragment.newInstance()
        }
    }

    override fun getCount(): Int {
        return tabTitles.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return tabTitles[position]
    }
}