package com.avicodes.halchalin.presentation.ui.home.reports

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.avicodes.halchalin.databinding.ItemProgressBinding

class LoaderStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<LoaderStateAdapter.LoaderViewHolder>() {

    override fun onBindViewHolder(holder: LoaderViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoaderViewHolder {
        val binding =
            ItemProgressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoaderViewHolder(binding, retry)
    }

    /**
     * view holder class for footer loader and error state handling
     */
    inner class LoaderViewHolder(private val binding: ItemProgressBinding, retry: () -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(loadState: LoadState) {

            binding.apply {
                btnRetry.setOnClickListener {
                    retry
                }

                when (loadState) {
                    is LoadState.Loading -> {
                        btnRetry.visibility = View.GONE
                        progBar.visibility = View.VISIBLE
                    }

                    is LoadState.Error -> {
                        btnRetry.visibility = View.VISIBLE
                        progBar.visibility = View.GONE
                    }

                    else -> {
                        btnRetry.visibility = View.GONE
                        progBar.visibility = View.GONE
                    }
                }
            }
        }
    }
}

