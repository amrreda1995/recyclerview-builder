package com.recyclerviewbuilder.library

import android.view.View
import androidx.lifecycle.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

typealias _ViewItemRepresentable = AbstractViewItem<ViewItemRepresentable>
typealias ViewItemArrayList = ArrayList<_ViewItemRepresentable>

open class RecyclerViewBuilder(
    private val recyclerView: RecyclerView,
    private var isDataBindingEnabled: Boolean,
    private var orientation: Int? = null,
    private var reverseLayout: Boolean? = null,
    private val columnCount: Int? = null
) : LifecycleObserver, AbstractRecyclerViewBuilder() {

    init {
        if (isDataBindingEnabled) {
            recyclerView.adapter = BindingAdapter()
            recyclerViewAdapter = recyclerView.adapter as BindingAdapter
        } else {
            recyclerView.adapter = Adapter()
            recyclerViewAdapter = recyclerView.adapter as Adapter
        }

        recyclerView.addScrollListenerForPagination(orientation, reverseLayout) {
            if (!isRecyclerViewLoading && isPaginationEnabled) {
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

    override fun setHeader(headerViewItem: AbstractViewItem<ViewItemRepresentable>?): RecyclerViewBuilder {
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

    override fun setFullWidthHeaderForGridLayout(enabled: Boolean) {
        val layoutManager = recyclerView.layoutManager

        if (layoutManager is GridLayoutManager) {
            layoutManager.spanSizeLookup =
                object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        if (enabled && columnCount != null && headerViewItem != null && position == 0) {
                            return columnCount
                        }

                        return 1
                    }
                }
        }
    }

    override fun setFooter(
        footerViewItem: AbstractViewItem<ViewItemRepresentable>?
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

    override fun setFullWidthFooterForGridLayout(enabled: Boolean) {
        val layoutManager = recyclerView.layoutManager

        if (layoutManager is GridLayoutManager) {
            layoutManager.spanSizeLookup =
                object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        if (enabled
                            && columnCount != null
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

    override fun setViewItems(
        viewItemsArrayList: ViewItemArrayList,
        clearsOnSet: Boolean,
        appendToEnd: Boolean
    ) {
        setAdapterViewItems(viewItemsArrayList, clearsOnSet, appendToEnd)

        notifyDataSetChanged()
        toggleLoading(false)
        onUpdatingAdapterFinishedBlock?.invoke()
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

    override fun notifyDataSetChanged() {
        recyclerViewAdapter.notifyDataSetChanged()
        isAdapterEmpty()
    }

    override fun setEmptyAdapter() {
        recyclerViewAdapter.viewItemsArrayList.clear()
        notifyDataSetChanged()
        isAdapterEmpty()
    }

    override fun setOnItemClick(block: (itemView: View, model: ViewItemRepresentable?, position: Int) -> Unit): RecyclerViewBuilder {
        recyclerViewAdapter.setOnItemClick(block)
        return this
    }

    override fun setOnItemLongClick(block: (itemView: View, model: ViewItemRepresentable?, position: Int) -> Unit): RecyclerViewBuilder {
        recyclerViewAdapter.setOnItemLongClick(block)
        return this
    }

    override fun onUpdatingAdapterFinished(block: () -> Unit): RecyclerViewBuilder {
        onUpdatingAdapterFinishedBlock = block
        return this
    }

    override fun setPaginationEnabled(enable: Boolean): RecyclerViewBuilder {
        this.isPaginationEnabled = enable
        return this
    }

    override fun onPaginate(block: () -> Unit): RecyclerViewBuilder {
        onPaginateBlock = block
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
            isAdapterEmpty()
        }
    }

    override fun isAdapterEmpty() {
        loadingView?.visibility = View.GONE

        if (recyclerViewAdapter.viewItemsArrayList.isNotEmpty()) {
            recyclerView.visibility = View.VISIBLE
            emptyView?.visibility = View.GONE
        } else {
            recyclerView.visibility = View.GONE
            emptyView?.visibility = View.VISIBLE
        }
    }

    @Suppress("Unused")
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun bindViewItemsObserver() {
        viewItems?.observe(lifecycleOwner!!, observer)
    }

    @Suppress("Unused")
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun unbindLifecycle() {
        viewItems?.removeObservers(lifecycleOwner!!)
        lifecycleOwner?.lifecycle?.removeObserver(this)
    }
}