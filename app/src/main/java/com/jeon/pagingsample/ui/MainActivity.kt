package com.jeon.pagingsample.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.jeon.pagingsample.R
import com.jeon.pagingsample.data.repository.HotelRepository
import com.jeon.pagingsample.ui.detail.DetailFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(),View.OnClickListener {
    private lateinit var viewModel:HotelViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(viewModelStore,ViewModelFactory(this.application)).get(HotelViewModel::class.java)
        viewModel.moveDetailLive.observe(this, Observer {
            moveFragment(DetailFragment.newInstance())
        })
        defaultTab()
        asc.setOnClickListener(this)
        desc.setOnClickListener(this)
        rate_sort.setOnClickListener(this)
        date_sort.setOnClickListener(this)
        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.list_page -> {
                    moveFragment(MainFragment.newInstance())
                    sort_container.visibility = View.GONE

                }
                R.id.like_page -> {
                    moveFragment(LikeFragment.newInstance())
                    sort_container.visibility = View.VISIBLE

                }
            }
            true
        }
    }
    private fun defaultTab(){
        bottom_navigation.selectedItemId = R.id.list_page
        moveFragment(MainFragment.newInstance())
        sort_container.visibility = View.GONE

    }

    private fun moveFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            if (fragment is DetailFragment){
                addToBackStack("tag")
            }
            replace(R.id.container, fragment)
            commit()
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.asc->{viewModel.changeQuery(HotelRepository.SORT.ASC)}
            R.id.desc->{viewModel.changeQuery(HotelRepository.SORT.DESC)}
            R.id.rate_sort->{viewModel.changeQuery(value =HotelRepository.FIELD.RATE)}
            R.id.date_sort->{viewModel.changeQuery(value =HotelRepository.FIELD.DATE)}
        }
    }


}
