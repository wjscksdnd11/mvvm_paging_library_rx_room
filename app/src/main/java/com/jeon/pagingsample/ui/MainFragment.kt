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
import com.jeon.pagingsample.data.NetworkState
import com.jeon.pagingsample.data.Status
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.item_footer_loading.*


class MainFragment : Fragment() {
    private lateinit var viewModel: HotelViewModel
    private lateinit var hotelAdapter: HotelAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.also {
            viewModel = ViewModelProvider(
                it.viewModelStore,
                ViewModelFactory(it.application)
            ).get(HotelViewModel::class.java)
            hotelAdapter = HotelAdapter(viewModel)
            observe()
            recycler.adapter = hotelAdapter
            recycler.itemAnimator = DefaultItemAnimator()
            refresh.setOnRefreshListener { viewModel.refresh() }
        }
    }

    fun observe(){
        with(viewModel){
            hotelList.observe(this@MainFragment, Observer<PagedList<HotelItem>> {pagedList->
                hotelAdapter.submitList(pagedList)
            })
            getNetworkState()
                .observe(this@MainFragment, Observer<NetworkState> {networkState->
                    hotelAdapter.setNetworkState(networkState)
                })
            getRefreshState().observe(this@MainFragment, Observer { networkState ->
                if (hotelAdapter.currentList != null) {
                    if (hotelAdapter.currentList!!.size > 0) {
                        refresh.isRefreshing = networkState?.status == NetworkState.LOADING.status
                    } else {
                        setInitialLoadingState(networkState)
                    }
                } else {
                    setInitialLoadingState(networkState)
                }
            })
        }

    }

    private fun setInitialLoadingState(networkState: NetworkState?) {
        retryLoadingButton.visibility =
            if (networkState?.status == Status.FAILED) View.VISIBLE else View.GONE
        loadingProgressBar.visibility =
            if (networkState?.status == Status.RUNNING) View.VISIBLE else View.GONE

        refresh.isEnabled = networkState?.status == Status.SUCCESS
        retryLoadingButton.setOnClickListener { viewModel.retry() }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MainFragment()
    }


}