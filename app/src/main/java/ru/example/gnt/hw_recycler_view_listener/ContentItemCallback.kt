package ru.example.gnt.hw_recycler_view_listener

import androidx.recyclerview.widget.DiffUtil.ItemCallback

class ContentItemCallback : ItemCallback<ContentItem>() {
    override fun areItemsTheSame(oldItem: ContentItem, newItem: ContentItem): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: ContentItem, newItem: ContentItem): Boolean =
        oldItem == newItem
}
