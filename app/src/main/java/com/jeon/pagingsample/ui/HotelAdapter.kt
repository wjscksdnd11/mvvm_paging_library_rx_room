package com.jeon.pagingsample.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jeon.pagingsample.R
import com.jeon.pagingsample.data.HotelItem
import com.jeon.pagingsample.data.NetworkState
import com.jeon.pagingsample.data.Status
import kotlinx.android.synthetic.main.item_footer_loading.view.*
import kotlinx.android.synthetic.main.item_main_hotel.view.*

class HotelAdapter (private val retryCallback:()->Unit):PagedListAdapter<HotelItem, RecyclerView.ViewHolder>(diffCallback){


    private var networkState: NetworkState? = null


    companion object{
        val diffCallback = object : DiffUtil.ItemCallback<HotelItem>() {
            override fun areItemsTheSame(oldItem: HotelItem, newItem: HotelItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: HotelItem, newItem: HotelItem): Boolean {
                return oldItem == newItem
            }
        }
        const val ITEM_VIEW_TYPE =0
        const val LOADING_VIEW_TYPE=1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            ITEM_VIEW_TYPE->{ HotelViewHolder.create(parent)}
            LOADING_VIEW_TYPE->{LoadingViewHolder.create(parent, retryCallback)}
            else -> throw IllegalArgumentException("unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(getItemViewType(position)){
            ITEM_VIEW_TYPE->(holder as HotelViewHolder).bind(getItem(position))
            LOADING_VIEW_TYPE->(holder as LoadingViewHolder).bind(networkState)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
           LOADING_VIEW_TYPE
        } else {
            ITEM_VIEW_TYPE
        }
    }


    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }

    class HotelViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(hotelItem: HotelItem?) {
            hotelItem?.also {
                Glide.with(itemView.context)
                    .load(it.thumbnail)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(itemView.iv_main_thumbnail)
            }
        }

        companion object {
            fun create(parent: ViewGroup): HotelViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.item_main_hotel, parent, false)
                return HotelViewHolder(view)
            }
        }
    }
    class LoadingViewHolder(view: View,  private val retryCallback: () -> Unit) : RecyclerView.ViewHolder(view) {

        init {
            itemView.retryLoadingButton.setOnClickListener { retryCallback() }
        }

        fun bind(networkState: NetworkState?) {
            itemView.errorMessageTextView.visibility = if (networkState?.message != null) View.VISIBLE else View.GONE
            if (networkState?.message != null) {
                itemView.errorMessageTextView.text = networkState.message
            }
            itemView.retryLoadingButton.visibility = if (networkState?.status == Status.FAILED) View.VISIBLE else View.GONE
            itemView.loadingProgressBar.visibility = if (networkState?.status == Status.RUNNING) View.VISIBLE else View.GONE
        }

        companion object {
            fun create(parent: ViewGroup, retryCallback: () -> Unit): LoadingViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.item_footer_loading, parent, false)
                return LoadingViewHolder(view, retryCallback)
            }
        }

        }

    fun setNetworkState(newNetworkState: NetworkState?) {
        if (currentList != null) {
            if (currentList!!.size != 0) {
                val previousState = this.networkState
                val hadExtraRow = hasExtraRow()
                this.networkState = newNetworkState
                val hasExtraRow = hasExtraRow()
                if (hadExtraRow != hasExtraRow) {
                    if (hadExtraRow) {
                        notifyItemRemoved(super.getItemCount())
                    } else {
                        notifyItemInserted(super.getItemCount())
                    }
                } else if (hasExtraRow && previousState !== newNetworkState) {
                    notifyItemChanged(itemCount - 1)
                }
            }
        }
    }

}