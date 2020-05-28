package com.jeon.pagingsample

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class PagerAdapter (fragmentManager: FragmentManager): FragmentStatePagerAdapter(fragmentManager,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    val tabTitles = arrayOf("리스트","즐겨찾기")
    companion object{
        const val MAIN_TAB_POSITON = 0
    }
    override fun getItem(position: Int): Fragment {
        if(MAIN_TAB_POSITON==position){

        }
    }

    override fun getCount(): Int {
        return tabTitles.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return super.getPageTitle(position)
    }
}