package com.recyclerviewbuilder.library

import android.view.View
import androidx.lifecycle.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

typealias _ViewItemRepresentable = AbstractViewItem
typealias ViewItemArrayList = ArrayList<_ViewItemRepresentable>

open class RecyclerViewBuilder(
    private val recyclerView: RecyclerView,
    private var isDataBindingEnabled: Boolean,
    private var orientation: Int? = null,
    private var reverseLayout: Boolean? = null,
    private val columnCount: Int? = null
) : LifecycleObserver, AbstractRecyclerViewBuilder() {

    init {
        unbindViewItemsObserver()

        if (isDataBindingEnabled) {
            recyclerView.adapter = BindingAdapter()
            recyclerViewAdapter = recyclerView.adapter as BindingAdapter
        } else {
            recyclerView.adapter = Adapter()
            recyclerViewAdapter = recyclerView.adapter as Adapter
        }

        recyclerView.addScrollListenerForPagination(orientation, reverseLayout) {
            if (!isRecyclerViewLoading && isPaginationEnabled && !isAdapterEmpty()) {
                isRecyclerViewLoading = true
                onPaginateBlock?.invoke()
            }
        }
    }

    override fun setEmptyView(emptyView: View): RecyclerViewBuilder {
        this.emptyView = emptyView
        return this
    }

    override fun setLoadingView(loadingView: View): RecyclerViewBuilder {
        this.loadingView = loadingView
        return this
    }

    override fun startLoading(): RecyclerViewBuilder {
        toggleLoading(true)
        return this
    }

    override fun finishLoading(): RecyclerViewBuilder {
        toggleLoading(false)
        return this
    }

    override fun isLoading(): Boolean {
        return isRecyclerViewLoading
    }

    override fun isAdapterEmpty(): Boolean {
        return recyclerViewAdapter.viewItemsArrayList.isEmpty()
    }

    override fun setHeader(headerViewItem: AbstractViewItem?): RecyclerViewBuilder {
        if (this.headerViewItem == null && headerViewItem != null) {
            recyclerViewAdapter.viewItemsArrayList.add(0, headerViewItem)

            setFullWidthHeaderForGridLayout(true)
        } else if (this.headerViewItem != null && headerViewItem == null) {
            recyclerViewAdapter.viewItemsArrayList.removeAt(0)

            setFullWidthHeaderForGridLayout(false)
        } else if (this.headerViewItem != null && headerViewItem != null) {
            recyclerViewAdapter.viewItemsArrayList.removeAt(0)

            recyclerViewAdapter.viewItemsArrayList.add(0, headerViewItem)

            setFullWidthHeaderForGridLayout(true)
        }

        notifyDataSetChanged()
        this.headerViewItem = headerViewItem
        return this
    }

    override fun setFooter(
        footerViewItem: AbstractViewItem?
    ): RecyclerViewBuilder {
        var indexToBeAdded = 0
        var indexToBeRemoved = 0

        if (recyclerViewAdapter.viewItemsArrayList.size > 0) {
            indexToBeAdded = recyclerViewAdapter.viewItemsArrayList.size
            indexToBeRemoved = recyclerViewAdapter.viewItemsArrayList.size - 1
        }

        if (this.footerViewItem == null && footerViewItem != null) {
            recyclerViewAdapter.viewItemsArrayList.add(indexToBeAdded, footerViewItem)
        } else if (this.footerViewItem != null && footerViewItem == null) {
            recyclerViewAdapter.viewItemsArrayList.removeAt(indexToBeRemoved)
        } else if (this.footerViewItem != null && footerViewItem != null) {
            recyclerViewAdapter.viewItemsArrayList.removeAt(indexToBeRemoved)

            if (indexToBeRemoved == 0) {
                recyclerViewAdapter.viewItemsArrayList.add(footerViewItem)
            } else {
                recyclerViewAdapter.viewItemsArrayList.add(
                    recyclerViewAdapter.viewItemsArrayList.size,
                    footerViewItem
                )
            }
        }

        if (footerViewItem != null) {
            setFullWidthFooterForGridLayout(true)
        } else {
            setFullWidthFooterForGridLayout(false)
        }
        notifyDataSetChanged()
        this.footerViewItem = footerViewItem
        return this
    }

    override fun scrollTo(viewItemIndex: Int, smoothScroll: Boolean): RecyclerViewBuilder {
        if (smoothScroll) {
            recyclerView.smoothScrollToPosition(viewItemIndex)
        } else {
            recyclerView.scrollToPosition(viewItemIndex)
        }

        return this
    }

    override fun setPaginationEnabled(enable: Boolean): RecyclerViewBuilder {
        this.isPaginationEnabled = enable
        return this
    }

    override fun notifyDataSetChanged() {
        recyclerViewAdapter.notifyDataSetChanged()
        setViewsVisibility()
        onUpdatingAdapterFinishedBlock?.invoke()
    }

    override fun notifyViewItemChanged(atIndex: Int) {
        recyclerViewAdapter.notifyItemChanged(atIndex)
        onUpdatingAdapterFinishedBlock?.invoke()
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewItemRepresentable> modifyViewItem(atIndex: Int, block: (model: T?) -> Unit) {
        block(recyclerViewAdapter.viewItemsArrayList[atIndex].dataModel as T)
        recyclerViewAdapter.notifyItemChanged(atIndex)
        onUpdatingAdapterFinishedBlock?.invoke()
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewItemRepresentable?> modifyViewItems(
        vararg atIndices: Int,
        block: (models: List<T?>) -> Unit
    ) {
        val models = ArrayList<T?>()
        atIndices.forEach { models.add(recyclerViewAdapter.viewItemsArrayList[it].dataModel as T) }
        block(models)
        atIndices.forEach { recyclerViewAdapter.notifyItemChanged(it) }
        onUpdatingAdapterFinishedBlock?.invoke()
    }

    override fun insertViewItem(
        atIndex: Int,
        viewItem: AbstractViewItem
    ) {
        if (headerViewItem == null && footerViewItem == null) {
            recyclerViewAdapter.viewItemsArrayList.add(atIndex, viewItem)
            recyclerViewAdapter.notifyItemInserted(atIndex)
        } else if (headerViewItem != null && footerViewItem == null) {
            if (atIndex == 0) {
                recyclerViewAdapter.viewItemsArrayList.add(1, viewItem)
                recyclerViewAdapter.notifyItemInserted(1)
            } else {
                recyclerViewAdapter.viewItemsArrayList.add(atIndex, viewItem)
                recyclerViewAdapter.notifyItemInserted(atIndex)
            }
        } else if (headerViewItem == null && footerViewItem != null) {
            if (atIndex == recyclerViewAdapter.viewItemsArrayList.size - 1) {
                recyclerViewAdapter.viewItemsArrayList.add(recyclerViewAdapter.viewItemsArrayList.size - 2, viewItem)
                recyclerViewAdapter.notifyItemInserted(recyclerViewAdapter.viewItemsArrayList.size - 2)
            } else {
                recyclerViewAdapter.viewItemsArrayList.add(atIndex, viewItem)
                recyclerViewAdapter.notifyItemInserted(atIndex)
            }
        } else if (headerViewItem != null && footerViewItem != null) {
            when (atIndex) {
                0 -> {
                    recyclerViewAdapter.viewItemsArrayList.add(1, viewItem)
                    recyclerViewAdapter.notifyItemInserted(1)
                }
                recyclerViewAdapter.viewItemsArrayList.size - 1 -> {
                    recyclerViewAdapter.viewItemsArrayList.add(
                        recyclerViewAdapter.viewItemsArrayList.size - 2,
                        viewItem
                    )
                    recyclerViewAdapter.notifyItemInserted(recyclerViewAdapter.viewItemsArrayList.size - 2)
                }
                else -> {
                    recyclerViewAdapter.viewItemsArrayList.add(atIndex, viewItem)
                    recyclerViewAdapter.notifyItemInserted(atIndex)
                }
            }
        }

        setViewsVisibility()
        onUpdatingAdapterFinishedBlock?.invoke()
    }

    override fun switchViewItem(ofIndex: Int, withIndex: Int) {
        if (headerViewItem != null && (ofIndex == 0 || withIndex == 0)) {
            throw Throwable("Can not switch header view item with another index")
        } else if (footerViewItem != null && (ofIndex == recyclerViewAdapter.viewItemsArrayList.size - 1
                    || withIndex == recyclerViewAdapter.viewItemsArrayList.size - 1)
        ) {
            throw Throwable("Can not switch footer view item with another index")
        } else {
            val tmpViewItem = recyclerViewAdapter.viewItemsArrayList[ofIndex]
            recyclerViewAdapter.viewItemsArrayList[ofIndex] = recyclerViewAdapter.viewItemsArrayList[withIndex]
            recyclerViewAdapter.viewItemsArrayList[withIndex] = tmpViewItem
            recyclerViewAdapter.notifyItemMoved(ofIndex, withIndex)
        }

        onUpdatingAdapterFinishedBlock?.invoke()
    }

    override fun removeViewItem(atIndex: Int) {
        recyclerViewAdapter.viewItemsArrayList.removeAt(atIndex)
        recyclerViewAdapter.notifyItemRemoved(atIndex)
        setViewsVisibility()

        if (headerViewItem != null && atIndex == 0) {
            headerViewItem = null
        } else if (footerViewItem != null && atIndex == recyclerViewAdapter.viewItemsArrayList.size - 1) {
            headerViewItem = null
        }

        onUpdatingAdapterFinishedBlock?.invoke()
    }

    override fun indexOf(viewItemRepresentable: ViewItemRepresentable): Int {
        return recyclerViewAdapter.viewItemsArrayList.indexOfFirst {
            it.dataModel?.equals(viewItemRepresentable) == true
        }
    }

    override fun setEmptyAdapter(): RecyclerViewBuilder {
        val size = recyclerViewAdapter.viewItemsArrayList.size
        recyclerViewAdapter.viewItemsArrayList.clear()
        recyclerViewAdapter.notifyItemRangeRemoved(0, size)
        setViewsVisibility()
        onUpdatingAdapterFinishedBlock?.invoke()
        return this
    }

    override fun setViewItems(
        viewItemsArrayList: ViewItemArrayList,
        clearsOnSet: Boolean,
        appendToEnd: Boolean
    ): RecyclerViewBuilder {
        setAdapterViewItems(viewItemsArrayList, clearsOnSet, appendToEnd)

        notifyDataSetChanged()
        toggleLoading(false)
        onUpdatingAdapterFinishedBlock?.invoke()
        return this
    }

    override fun bindViewItems(
        lifecycleOwner: LifecycleOwner,
        viewItems: MutableLiveData<ViewItemsObserver>
    ): RecyclerViewBuilder {
        this.lifecycleOwner = lifecycleOwner
        this.viewItems = viewItems
        lifecycleOwner.lifecycle.addObserver(this)
        return this
    }

    override fun setOnItemClick(block: (itemView: View, model: ViewItemRepresentable?, position: Int) -> Unit): RecyclerViewBuilder {
        recyclerViewAdapter.setOnItemClick(block)
        return this
    }

    override fun setOnItemLongClick(block: (itemView: View, model: ViewItemRepresentable?, position: Int) -> Unit): RecyclerViewBuilder {
        recyclerViewAdapter.setOnItemLongClick(block)
        return this
    }

    override fun onPaginate(block: () -> Unit): RecyclerViewBuilder {
        onPaginateBlock = block
        return this
    }

    override fun onUpdatingAdapterFinished(block: () -> Unit): RecyclerViewBuilder {
        onUpdatingAdapterFinishedBlock = block
        return this
    }

    override fun setAdapterViewItems(
        viewItemsArrayList: ViewItemArrayList,
        clearsOnSet: Boolean,
        appendToEnd: Boolean
    ) {
        if (clearsOnSet) {
            recyclerViewAdapter.viewItemsArrayList.clear()

            if (headerViewItem == null && footerViewItem == null) {
                recyclerViewAdapter.viewItemsArrayList = viewItemsArrayList
            } else if (headerViewItem != null && footerViewItem == null) {
                recyclerViewAdapter.viewItemsArrayList.add(headerViewItem!!)
                recyclerViewAdapter.viewItemsArrayList.addAll(viewItemsArrayList)
            } else if (headerViewItem == null && footerViewItem != null) {
                recyclerViewAdapter.viewItemsArrayList.addAll(viewItemsArrayList)
                recyclerViewAdapter.viewItemsArrayList.add(footerViewItem!!)
            } else if (headerViewItem != null && footerViewItem != null) {
                recyclerViewAdapter.viewItemsArrayList.add(headerViewItem!!)
                recyclerViewAdapter.viewItemsArrayList.addAll(viewItemsArrayList)
                recyclerViewAdapter.viewItemsArrayList.add(footerViewItem!!)
            }

        } else {
            if (appendToEnd) {
                if ((headerViewItem == null && footerViewItem == null) || (headerViewItem != null && footerViewItem == null)) {
                    recyclerViewAdapter.viewItemsArrayList.addAll(viewItemsArrayList)
                } else if ((headerViewItem == null && footerViewItem != null) || (headerViewItem != null && footerViewItem != null)) {
                    recyclerViewAdapter.viewItemsArrayList.removeAt(recyclerViewAdapter.viewItemsArrayList.size - 1)
                    recyclerViewAdapter.viewItemsArrayList.addAll(viewItemsArrayList)
                    recyclerViewAdapter.viewItemsArrayList.add(footerViewItem!!)
                }
            } else {
                if ((headerViewItem == null && footerViewItem == null) || (headerViewItem == null && footerViewItem != null)) {
                    recyclerViewAdapter.viewItemsArrayList =
                        ArrayList((viewItemsArrayList + recyclerViewAdapter.viewItemsArrayList))
                } else if ((headerViewItem != null && footerViewItem == null) || (headerViewItem != null && footerViewItem != null)) {
                    recyclerViewAdapter.viewItemsArrayList =
                        ArrayList(arrayListOf(headerViewItem!!) + viewItemsArrayList + recyclerViewAdapter.viewItemsArrayList)
                }
            }
        }

        if (headerViewItem != null) {
            setFullWidthHeaderForGridLayout(true)
        } else {
            setFullWidthHeaderForGridLayout(false)
        }

        if (footerViewItem != null) {
            setFullWidthFooterForGridLayout(true)
        } else {
            setFullWidthFooterForGridLayout(false)
        }
    }

    override fun toggleLoading(isLoading: Boolean) {
        this.isRecyclerViewLoading = isLoading

        if (isRecyclerViewLoading) {
            recyclerView.visibility = View.GONE
            emptyView?.visibility = View.GONE
            loadingView?.visibility = View.VISIBLE
        } else {
            setViewsVisibility()
        }
    }

    override fun setViewsVisibility() {
        loadingView?.visibility = View.GONE

        if (recyclerViewAdapter.viewItemsArrayList.isNotEmpty()) {
            recyclerView.visibility = View.VISIBLE
            emptyView?.visibility = View.GONE
        } else {
            recyclerView.visibility = View.GONE
            emptyView?.visibility = View.VISIBLE
        }
    }

    override fun setFullWidthHeaderForGridLayout(enabled: Boolean) {
        columnCount?.let {
            (recyclerView.layoutManager as GridLayoutManager).spanSizeLookup =
                object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        if (enabled && headerViewItem != null && position == 0) {
                            return columnCount
                        }

                        return 1
                    }
                }
        }
    }

    override fun setFullWidthFooterForGridLayout(enabled: Boolean) {
        columnCount?.let {
            (recyclerView.layoutManager as GridLayoutManager).spanSizeLookup =
                object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        if (enabled
                            && footerViewItem != null
                            && position == recyclerViewAdapter.viewItemsArrayList.size - 1
                        ) {
                            return columnCount
                        }

                        return 1
                    }
                }
        }
    }

    @Suppress("Unused")
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun bindViewItemsObserver() {
        lifecycleOwner?.let {
            viewItems?.observe(it, observer)
        }
    }

    @Suppress("Unused")
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun unbindViewItemsObserver() {
        lifecycleOwner?.let {
            viewItems?.removeObservers(it)
            lifecycleOwner?.lifecycle?.removeObserver(this)
        }
    }
}