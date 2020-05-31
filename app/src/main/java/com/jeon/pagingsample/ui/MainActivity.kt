package com.jeon.pagingsample.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jeon.pagingsample.R
import com.jeon.pagingsample.ui.detail.DetailFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val viewModel = ViewModelProvider(viewModelStore,ViewModelFactory(this.application)).get(HotelViewModel::class.java)
        viewModel.moveDetailLive.observe(this, Observer {
            moveFragment(DetailFragment.newInstance())
        })
        bottom_navigation.selectedItemId = R.id.list_page
        bottom_navigation.setOnNavigationItemReselectedListener { item ->
            when(item.itemId) {
                R.id.list_page -> {
                    moveFragment(MainFragment.newInstance())
                }
                R.id.like_page -> {
                    moveFragment(LikeFragment.newInstance())
                }
            }
        }
    }


    private fun moveFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, fragment)
            commit()
        }
    }



}
