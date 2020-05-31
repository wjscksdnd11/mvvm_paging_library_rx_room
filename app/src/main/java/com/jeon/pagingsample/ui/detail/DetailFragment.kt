package com.jeon.pagingsample.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.jeon.pagingsample.R
import com.jeon.pagingsample.data.HotelItem
import com.jeon.pagingsample.ui.HotelViewModel
import com.jeon.pagingsample.ui.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.item_main_hotel.view.*

class DetailFragment : Fragment() {
    private lateinit var viewModel: HotelViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.also {
            viewModel = ViewModelProvider(it.viewModelStore, ViewModelFactory(it.application)).get(
                HotelViewModel::class.java
            )

            viewModel.detailItem.observe(this, Observer {detailItem->
                bind(detailItem)
            })
        }
    }

    private fun bind(hotelItem: HotelItem?) {

        hotelItem?.apply {
            iv_main_like_toggle.setOnClickListener {
                isLike = !isLike
                if (isLike) {
                    viewModel.likebyId(id)
                    Toast.makeText(
                        context, context?.getString(R.string.add_like),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    viewModel.unlikebyId(id)
                    Toast.makeText(
                        context, context?.getString(R.string.remove_unlike),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                setRateView(isLike)
            }
            setRateView(isLike)
            setTextViews(this)
            context?.let {
                Glide.with(it)
                    .load(description.imagePath)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(iv_main_thumbnail)
            }
            tv_description.text = description.subject
        }
    }

    private fun setRateView(isLike:Boolean){
        val rateDrawableId = if(isLike){
            R.drawable.ic_star_rate_black_18dp
        }else{
            R.drawable.ic_star_border_black_18dp
        }
       iv_main_like_toggle.setImageDrawable(context?.let { ContextCompat.getDrawable(it,rateDrawableId) })

    }
    private fun setTextViews(item: HotelItem) {

        tv_main_name.text = item.description.subject
        tv_main_price.text = String.format("%,d", item.description.price)
        if (item.rate > 0)
            tv_main_rate.text = item.rate.toString()

    }

    companion object {

        @JvmStatic
        fun newInstance() =
            DetailFragment()
    }
}
