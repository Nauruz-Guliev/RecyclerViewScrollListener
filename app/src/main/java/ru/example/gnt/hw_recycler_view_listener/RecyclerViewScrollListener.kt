package ru.example.gnt.hw_recycler_view_listener

import android.annotation.SuppressLint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewScrollListener(
    private val recyclerViewVertical: RecyclerView,
    private val horizontalAdapter: HorizontalAdapter,
    private val verticalAdapter: VerticalAdapter,
    private val notificationMode: ScrollListenerNotificationMode
) : View.OnScrollChangeListener {
    private var recyclerViewHorizontal: RecyclerView? = null
    override fun onScrollChange(
        v: View?,
        scrollX: Int,
        scrollY: Int,
        oldScrollX: Int,
        oldScrollY: Int
    ) {
        val layoutManagerVertical = recyclerViewVertical.layoutManager as LinearLayoutManager

        recyclerViewHorizontal =
            layoutManagerVertical.findViewByPosition(layoutManagerVertical.findFirstVisibleItemPosition())
                ?.findViewById(R.id.rv_horizontal) as? RecyclerView

        when (notificationMode) {
            ScrollListenerNotificationMode.TOAST -> showChangeWithToast(
                layoutManagerVertical,
                isVertical = true
            )
            ScrollListenerNotificationMode.TEXT -> showChangeByItemUpdating(
                layoutManagerVertical,
                isVertical = true
            )
        }
        initHorizontalRecyclerViewListener()
    }

    private fun initHorizontalRecyclerViewListener() {
        recyclerViewHorizontal?.setOnScrollChangeListener { _, _, _, _, _ ->
            val layoutManagerHorizontal =
                recyclerViewHorizontal?.layoutManager as? LinearLayoutManager
            if (layoutManagerHorizontal != null) {
                when (notificationMode) {
                    ScrollListenerNotificationMode.TOAST -> showChangeWithToast(
                        layoutManagerHorizontal,
                        isVertical = false
                    )
                    ScrollListenerNotificationMode.TEXT -> showChangeByItemUpdating(
                        layoutManagerHorizontal,
                        isVertical = false
                    )
                }
            }
        }
    }

    private fun findPercentageVertical(
        layoutManager: RecyclerView.LayoutManager,
        position: Int
    ): Int {
        val rvRect = Rect()
        recyclerViewVertical.getGlobalVisibleRect(rvRect);

        val rowRect = Rect();
        layoutManager.findViewByPosition(position)?.getGlobalVisibleRect(rowRect)

        val percentFirst = if (rowRect.bottom >= rvRect.bottom) {
            val visibleHeightFirst = rvRect.bottom - rowRect.top
            (visibleHeightFirst * 100) / (layoutManager.findViewByPosition(position) as View).height
        } else {
            val visibleHeightFirst = rowRect.bottom - rvRect.top
            (visibleHeightFirst * 100) / (layoutManager.findViewByPosition(position) as View).height
        }
        return if (percentFirst >= 100) 100 else if (percentFirst <= 0) 0 else percentFirst
    }

    private fun findPercentageHorizontal(
        layoutManager: RecyclerView.LayoutManager,
        position: Int
    ): Int {
        val rvRect = Rect()
        recyclerViewVertical.getGlobalVisibleRect(rvRect);

        val rowRect = Rect();
        layoutManager.findViewByPosition(position)?.getGlobalVisibleRect(rowRect)

        val percentFirst = if (rowRect.right >= rvRect.right) {
            val visibleHeightFirst = rvRect.right - rowRect.left
            (visibleHeightFirst * 100) / (layoutManager.findViewByPosition(position) as View).width
        } else {
            val visibleHeightFirst = rowRect.right - rvRect.left
            (visibleHeightFirst * 100) / (layoutManager.findViewByPosition(position) as View).width
        }
        return if (percentFirst >= 95) 100 else if (percentFirst <= 0) 0 else percentFirst
    }

    @SuppressLint("StringFormatInvalid")
    private fun showChangeWithToast(layoutManager: LinearLayoutManager, isVertical: Boolean) {
        with(recyclerViewVertical.context) {
            val positionLast = layoutManager.findLastVisibleItemPosition()
            val item = layoutManager.findViewByPosition(layoutManager.findLastVisibleItemPosition())
            val percentLast = if (isVertical) findPercentageVertical(
                layoutManager,
                positionLast
            ) else findPercentageHorizontal(layoutManager, positionLast)
            showToast(
                getString(
                    R.string.last_percentage_visiblity,
                    (item?.tag as? ContentItem)?.id,
                    percentLast
                )
            )
        }
    }


    private fun showChangeByItemUpdating(layoutManager: LinearLayoutManager, isVertical: Boolean) {
        val positionFirst = layoutManager.findFirstVisibleItemPosition()
        val positionLast = layoutManager.findLastVisibleItemPosition()

        val itemFirst = layoutManager.findViewByPosition(positionFirst)
        val itemLast = layoutManager.findViewByPosition(positionLast)

        val percentFirst = if (isVertical) findPercentageVertical(
            layoutManager,
            positionFirst
        ) else findPercentageHorizontal(layoutManager, positionFirst)
        val percentLast = if (isVertical) findPercentageVertical(
            layoutManager,
            positionLast
        ) else findPercentageHorizontal(layoutManager, positionLast)

        (itemFirst?.tag as ContentItem?)?.apply {
            visiblePercentage = percentFirst
        }

        (itemLast?.tag as ContentItem?)?.apply {
            visiblePercentage = percentLast
        }

        // runCatching используется для тех случаев, когда notifyDataSetChanged не успел отрисовать
        // а скроллинг произвести надо. Выбросится исключение, перерисовка не произойдет, но на скролл это не повлияет
        kotlin.runCatching {
            if (isVertical) verticalAdapter.notifyDataSetChanged() else horizontalAdapter.notifyDataSetChanged()
            /*
            // перерисовка каждого элемента визуально отрабатывает хуже, чем notifyDataSetChanged
                 verticalAdapter.notifyItemChanged(positionLast)
                 verticalAdapter.notifyItemChanged(positionFirst)
             */

            /*
            // этот способ не работает. submit list не успевает перерисовать каждое изменение
            // может быть потому что ему ещё приходится элементы найти???
            val list = ContentStorage.listOfItems.apply {
                first { it.id ==  (itemLast?.tag as? ContentItem)?.id }.visiblePercentage = percentLast
                first { it.id ==  (itemFirst?.tag as? ContentItem)?.id }.visiblePercentage = percentFirst
            }
            verticalAdapter.submitList(list)

             */
        }

    }
}
