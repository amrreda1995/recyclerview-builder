package com.recyclerviewbuilder.library

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

abstract class AbstractRecyclerViewBuilder {

    protected var emptyView: View? = null
    protected var loadingView: View? = null

    protected var headerViewItem: AbstractViewItem? = null
    protected var footerViewItem: AbstractViewItem? = null

    protected lateinit var recyclerViewAdapter: BaseAdapterInterface

    protected var onUpdatingAdapterFinishedBlock: (() -> Unit)? = null
    protected var onPaginateBlock: (() -> Unit)? = null

    protected var isRecyclerViewLoading = false
    protected var isPaginationEnabled = false

    protected var lifecycleOwner: LifecycleOwner? = null

    protected var viewItems: MutableLiveData<ViewItemsObserver>? = null

    protected val observer = Observer<ViewItemsObserver> {
        setAdapterViewItems(it.viewItemsArrayList, it.clearsOnSet, it.appendToEnd)
        notifyDataSetChanged()
        toggleLoading(false)
        onUpdatingAdapterFinishedBlock?.invoke()
    }

    abstract fun setEmptyView(emptyView: View): RecyclerViewBuilder

    abstract fun setLoadingView(loadingView: View): RecyclerViewBuilder

    abstract fun startLoading(): RecyclerViewBuilder

    abstract fun finishLoading(): RecyclerViewBuilder

    abstract fun isLoading(): Boolean

    abstract fun isAdapterEmpty(): Boolean

    abstract fun setHeader(headerViewItem: AbstractViewItem?): RecyclerViewBuilder

    abstract fun setFooter(footerViewItem: AbstractViewItem?): RecyclerViewBuilder

    abstract fun scrollTo(viewItemIndex: Int, smoothScroll: Boolean = true): RecyclerViewBuilder

    abstract fun setPaginationEnabled(enable: Boolean): RecyclerViewBuilder

    abstract fun notifyDataSetChanged()

    abstract fun notifyViewItemChanged(atIndex: Int)

    abstract fun <T : ViewItemRepresentable> modifyViewItem(atIndex: Int, block: (model: T?) -> Unit)

    abstract fun <T : ViewItemRepresentable?> modifyViewItems(
        vararg atIndices: Int,
        block: (models: List<T?>) -> Unit
    )

    abstract fun insertViewItem(atIndex: Int, viewItem: AbstractViewItem)

    abstract fun switchViewItem(ofIndex: Int, withIndex: Int)

    abstract fun removeViewItem(atIndex: Int)

    abstract fun indexOf(viewItemRepresentable: ViewItemRepresentable): Int

    abstract fun setEmptyAdapter(): RecyclerViewBuilder

    abstract fun setViewItems(
        viewItemsArrayList: ArrayList<AbstractViewItem>,
        clearsOnSet: Boolean = false,
        appendToEnd: Boolean = true
    ): RecyclerViewBuilder

    abstract fun bindViewItems(
        lifecycleOwner: LifecycleOwner,
        viewItems: MutableLiveData<ViewItemsObserver>
    ): RecyclerViewBuilder

    abstract fun onPaginate(block: () -> Unit): RecyclerViewBuilder

    abstract fun onUpdatingAdapterFinished(block: () -> Unit): RecyclerViewBuilder

    abstract fun setOnItemClick(
        block: (itemView: View, model: ViewItemRepresentable?, position: Int) -> Unit
    ): RecyclerViewBuilder

    abstract fun setOnItemLongClick(
        block: (itemView: View, model: ViewItemRepresentable?, position: Int) -> Unit
    ): RecyclerViewBuilder

    protected abstract fun setAdapterViewItems(
        viewItemsArrayList: ArrayList<AbstractViewItem>,
        clearsOnSet: Boolean = false,
        appendToEnd: Boolean = true
    )

    protected abstract fun toggleLoading(isLoading: Boolean)

    protected abstract fun setViewsVisibility()

    protected abstract fun setFullWidthHeaderForGridLayout(enabled: Boolean)

    protected abstract fun setFullWidthFooterForGridLayout(enabled: Boolean)
}