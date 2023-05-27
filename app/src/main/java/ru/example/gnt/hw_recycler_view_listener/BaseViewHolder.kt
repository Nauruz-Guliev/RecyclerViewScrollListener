package ru.example.gnt.hw_recycler_view_listener

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewbinding.ViewBinding

abstract class BaseViewHolder<Item>(binding: ViewBinding) : ViewHolder(binding.root) {
    abstract fun onBind(item: Item)
}
