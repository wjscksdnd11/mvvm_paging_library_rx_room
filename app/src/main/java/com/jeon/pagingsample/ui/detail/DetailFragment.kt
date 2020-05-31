package com.jeon.pagingsample.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jeon.pagingsample.R
import com.jeon.pagingsample.ui.HotelViewModel
import com.jeon.pagingsample.ui.ViewModelFactory

class DetailFragment : Fragment() {
    private lateinit var viewModel:HotelViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.also{
            viewModel = ViewModelProvider(viewModelStore,ViewModelFactory(it.application)).get(HotelViewModel::class.java)
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            DetailFragment()
    }
}
