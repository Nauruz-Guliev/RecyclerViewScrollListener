package ru.example.gnt.hw_recycler_view_listener

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.example.gnt.hw_recycler_view_listener.databinding.HorizontalItemBinding

class HorizontalAdapter : ListAdapter<ContentItem, HorizontalAdapter.HorizontalViewHolder>(
    ContentItemCallback()
) {
    inner class HorizontalViewHolder(private val binding: HorizontalItemBinding) :
        BaseViewHolder<ContentItem>(binding) {
        override fun onBind(item: ContentItem) = with(binding) {
            binding.root.tag = item
            tvText.text = root.context.getString(R.string.percentage, item.id, item.visiblePercentage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorizontalViewHolder =
        HorizontalViewHolder(
            binding = HorizontalItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: HorizontalViewHolder, position: Int) =
        holder.onBind(currentList[position])

    override fun getItemViewType(position: Int): Int = RecyclerViewTypes.HORIZONTAL.id
}
