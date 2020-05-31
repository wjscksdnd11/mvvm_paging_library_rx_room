package com.jeon.pagingsample.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import androidx.recyclerview.widget.DefaultItemAnimator
import com.jeon.pagingsample.R
import com.jeon.pagingsample.data.HotelItem
import kotlinx.android.synthetic.main.fragment_main.*


class LikeFragment : Fragment() {
    private lateinit var viewModel: HotelViewModel
    private lateinit var hotelAdapter: HotelAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.also {
            viewModel = ViewModelProvider(
                it.viewModelStore,
                ViewModelFactory(it.application)
            ).get(HotelViewModel::class.java)
            hotelAdapter = HotelAdapter(viewModel,true)
            observe()
            recycler.adapter = hotelAdapter
            recycler.itemAnimator = DefaultItemAnimator()
            refresh.setOnRefreshListener { viewModel.localDataRefresh() }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_like, container, false)
    }

    private fun observe(){
        with(viewModel){
            likeList.observe(this@LikeFragment, Observer<PagedList<HotelItem>> { pagedList->
                refresh.isRefreshing = false
                hotelAdapter.submitList(pagedList)
            })

        }

    }
    companion object {

        @JvmStatic
        fun newInstance() =
            LikeFragment()
    }
}
