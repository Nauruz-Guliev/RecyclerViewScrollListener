package ru.example.gnt.hw_recycler_view_listener

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import ru.example.gnt.hw_recycler_view_listener.databinding.MainFragmentBinding


class MainFragment : Fragment() {
    private var _binding: MainFragmentBinding? = null
    private val binding by lazy { _binding!! }
    private var scrollListener: RecyclerViewScrollListener? = null

    private val horizontalAdapter: HorizontalAdapter by lazy {
        HorizontalAdapter().apply {
            submitList(ContentStorage.horizontalItems)
        }
    }

    private val verticalAdapter: VerticalAdapter by lazy {
        VerticalAdapter().apply {
            submitList(ContentStorage.verticalItems)
        }
    }

    private val horizontalWrapperAdapter: HorizontalWrapperAdapter by lazy {
        HorizontalWrapperAdapter(horizontalAdapter)
    }

    private val concatAdapter: ConcatAdapter by lazy {
        val config = ConcatAdapter.Config.Builder().apply {
            setIsolateViewTypes(false)
        }.build()
        ConcatAdapter(config, horizontalWrapperAdapter,  verticalAdapter)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        val layoutManager = GridLayoutManager(requireContext(), ContentStorage.verticalItems.size)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
               return ContentStorage.verticalItems.size
            }
        }
        binding.rvVertical.apply {
            this.layoutManager = layoutManager
            this.adapter = concatAdapter
        }
        initScrollListener()
    }

    private fun initScrollListener() {
        scrollListener = RecyclerViewScrollListener(
            recyclerViewVertical = binding.rvVertical,
            verticalAdapter = verticalAdapter,
            horizontalAdapter = horizontalAdapter,
            notificationMode = ScrollListenerNotificationMode.TEXT
        )
        binding.rvVertical.setOnScrollChangeListener(scrollListener)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        horizontalWrapperAdapter.onRestoreState(savedInstanceState ?: bundleOf())
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        horizontalWrapperAdapter.onSaveState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        scrollListener = null
    }

}
