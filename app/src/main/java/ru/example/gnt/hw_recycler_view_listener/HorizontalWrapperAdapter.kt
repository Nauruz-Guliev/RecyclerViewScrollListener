package ru.example.gnt.hw_recycler_view_listener

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.example.gnt.hw_recycler_view_listener.databinding.ViewHorizontalWrapperBinding

class HorizontalWrapperAdapter(
    private val adapter: HorizontalAdapter
) : ListAdapter<ContentItem, HorizontalWrapperAdapter.HorizontalWrapperViewHolder>(
    ContentItemCallback()
) {
    private var lastScrollX: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorizontalWrapperViewHolder =
        HorizontalWrapperViewHolder(
            ViewHorizontalWrapperBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: HorizontalWrapperViewHolder, position: Int) =
        holder.bind(adapter, lastScrollX ?:0) {
            lastScrollX = it
        }

    override fun getItemCount(): Int = 1

    override fun getItemViewType(position: Int): Int =
        RecyclerViewTypes.HORIZONTAL_WRAPPER.id


    fun onSaveState(outState: Bundle) {
        outState.putInt(SCROLL_X_ARG, lastScrollX)
    }

    fun onRestoreState(savedState: Bundle) {
        lastScrollX = savedState.getInt(SCROLL_X_ARG, 0)
    }

    class HorizontalWrapperViewHolder(
        private val binding: ViewHorizontalWrapperBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(adapter: HorizontalAdapter, lastScrollX: Int, onScrolled: (Int) -> Unit) {
            with(binding.rvHorizontal) {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                binding.rvHorizontal.adapter = adapter
                doOnPreDraw {
                    scrollBy(lastScrollX, 0)
                }
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        onScrolled(recyclerView.computeHorizontalScrollOffset())
                    }
                })
            }
        }

    }

    companion object {
        private const val SCROLL_X_ARG = "SCROLL_X_ARG"
    }
}
