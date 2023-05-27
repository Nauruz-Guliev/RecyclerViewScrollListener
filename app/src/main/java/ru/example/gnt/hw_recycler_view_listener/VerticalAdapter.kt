package ru.example.gnt.hw_recycler_view_listener

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.example.gnt.hw_recycler_view_listener.databinding.VerticalItemBinding

class VerticalAdapter :
    ListAdapter<ContentItem, VerticalAdapter.VerticalViewHolder>(ContentItemCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VerticalViewHolder =
        VerticalViewHolder(
            binding = VerticalItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: VerticalViewHolder, position: Int) =
        holder.onBind(currentList[position])

    inner class VerticalViewHolder(private val binding: VerticalItemBinding) :
        BaseViewHolder<ContentItem>(binding) {
        override fun onBind(item: ContentItem) = with(binding) {
            binding.root.tag = item
            tvText.text =
                root.context.getString(R.string.percentage, item.id, item.visiblePercentage)
        }
    }

    override fun getItemViewType(position: Int): Int =
        RecyclerViewTypes.VERTICAL.id
}
