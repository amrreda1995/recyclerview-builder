package com.recyclerviewbuilder.library

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView

private typealias _ViewItemRepresentable = ViewItem<ViewItemRepresentable>
private typealias ViewItemArrayList = ArrayList<_ViewItemRepresentable>

data class ViewItemsObserver(
    var viewItemsArrayList: ViewItemArrayList = arrayListOf(),
    var clearsOnSet: Boolean = false
)

open class RecyclerViewBuilder(
    private val recyclerView: RecyclerView,
    orientation: Int? = null,
    reverseLayout: Boolean? = null
) : LifecycleObserver {
    private var emptyView: View? = null
    private var loadingView: View? = null

    private var onItemClickBlock: ((View, ViewItemRepresentable, Int) -> Unit)? = null
    private var onItemLongClickBlock: ((View, ViewItemRepresentable, Int) -> Unit)? = null
    private var onUpdatingAdapterFinishedBlock: (() -> Unit)? = null
    private var onPaginateBlock: (() -> Unit)? = null

    private var adapter = Adapter()

    private var isLoading = false
    private var isPaginationFeatureEnabled = false

    private var viewItems: MutableLiveData<ViewItemsObserver>? = null

    private var lifecycle: Lifecycle? = null

    private val observer = Observer<ViewItemsObserver> {
        if (it.clearsOnSet) {
            adapter.viewItemsArrayList = it.viewItemsArrayList
        } else {
            adapter.viewItemsArrayList.addAll(it.viewItemsArrayList)
        }

        adapter.notifyDataSetChanged()
        toggleLoading(false)
        onUpdatingAdapterFinishedBlock?.invoke()
    }

    init {
        recyclerView.adapter = adapter

        recyclerView.addScrollListenerForPagination(orientation, reverseLayout) {
            if (!isLoading && isPaginationFeatureEnabled) {
                isLoading = true
                onPaginateBlock?.invoke()
            }
        }
    }

    fun setEmptyView(emptyView: View): RecyclerViewBuilder {
        this.emptyView = emptyView
        return this
    }

    fun setLoadingView(loadingView: View): RecyclerViewBuilder {
        this.loadingView = loadingView
        return this
    }

    fun startLoading(): RecyclerViewBuilder {
        toggleLoading(true)
        return this
    }

    fun finishLoading(): RecyclerViewBuilder {
        toggleLoading(false)
        return this
    }

    fun isLoading(): Boolean {
        return isLoading
    }

    fun setViewItems(
        viewItemsArrayList: ViewItemArrayList,
        clearsOnSet: Boolean = false
    ) {
        if (!clearsOnSet) {
            adapter.viewItemsArrayList.addAll(viewItemsArrayList)
        } else {
            adapter.viewItemsArrayList = viewItemsArrayList
        }

        adapter.notifyDataSetChanged()
        toggleLoading(false)
        onUpdatingAdapterFinishedBlock?.invoke()
    }

    fun bindViewItems(
        lifecycle: Lifecycle,
        viewItems: MutableLiveData<ViewItemsObserver>
    ): RecyclerViewBuilder {
        this.viewItems = viewItems
        this.lifecycle = lifecycle

        this.lifecycle?.addObserver(this)

        return this
    }

    fun notifyDataSetChanged() {
        adapter.notifyDataSetChanged()
        isAdapterEmpty()
    }

    fun setEmptyAdapter() {
        adapter.viewItemsArrayList.clear()
        notifyDataSetChanged()
        isAdapterEmpty()
    }

    fun setOnItemClick(block: (itemView: View, model: ViewItemRepresentable, position: Int) -> Unit): RecyclerViewBuilder {
        onItemClickBlock = block
        return this
    }

    fun setOnItemLongClick(block: (itemView: View, model: ViewItemRepresentable, position: Int) -> Unit): RecyclerViewBuilder {
        onItemLongClickBlock = block
        return this
    }

    fun onUpdatingAdapterFinished(block: () -> Unit): RecyclerViewBuilder {
        onUpdatingAdapterFinishedBlock = block
        return this
    }

    fun setPaginationFeatureEnabled(enable: Boolean): RecyclerViewBuilder {
        this.isPaginationFeatureEnabled = enable
        return this
    }

    fun onPaginate(block: () -> Unit): RecyclerViewBuilder {
        onPaginateBlock = block
        return this
    }

    private fun toggleLoading(isLoading: Boolean) {
        this.isLoading = isLoading

        if (isLoading) {
            recyclerView.visibility = View.GONE
            emptyView?.visibility = View.GONE
            loadingView?.visibility = View.VISIBLE
        } else {
            isAdapterEmpty()
        }
    }

    private fun isAdapterEmpty() {
        loadingView?.visibility = View.GONE

        if (adapter.viewItemsArrayList.isNotEmpty()) {
            recyclerView.visibility = View.VISIBLE
            emptyView?.visibility = View.GONE
        } else {
            recyclerView.visibility = View.GONE
            emptyView?.visibility = View.VISIBLE
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun bindViewItemsObserver() {
        viewItems?.observeForever(observer)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private fun removeViewItemsObserver() {
        viewItems?.removeObserver(observer)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun unbindLifecycleAware() {
        this.lifecycle?.removeObserver(this)
    }

    private inner class Adapter : RecyclerView.Adapter<ViewHolder>() {
        var viewItemsArrayList = ViewItemArrayList()

        override fun getItemViewType(position: Int): Int {
            return viewItemsArrayList[position].layoutResourceId
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                LayoutInflater.from(parent.context).inflate(viewType, parent, false)
            )
        }

        override fun getItemCount(): Int {
            return viewItemsArrayList.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            viewItemsArrayList[holder.adapterPosition].bind(holder.itemView)

            holder.itemView.setOnClickListener {
                onItemClickBlock?.invoke(
                    holder.itemView,
                    viewItemsArrayList[holder.adapterPosition].dataModel,
                    holder.adapterPosition
                )
            }

            holder.itemView.setOnLongClickListener {
                onItemLongClickBlock?.invoke(
                    holder.itemView,
                    viewItemsArrayList[holder.adapterPosition].dataModel,
                    holder.adapterPosition
                )
                true
            }
        }
    }

    private inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}