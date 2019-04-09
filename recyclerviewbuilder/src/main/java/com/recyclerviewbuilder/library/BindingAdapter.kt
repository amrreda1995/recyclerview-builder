package com.recyclerviewbuilder.library

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class BindingAdapter : RecyclerView.Adapter<BindingAdapter.BindingViewHolder>(), BaseAdapterInterface {
    override var viewItemsArrayList = ViewItemArrayList()

    private var onItemClickBlock: ((View, ViewItemRepresentable?, Int) -> Unit)? = null
    private var onItemLongClickBlock: ((View, ViewItemRepresentable?, Int) -> Unit)? = null

    override fun setOnItemClick(block: (itemView: View, model: ViewItemRepresentable?, position: Int) -> Unit) {
        onItemClickBlock = block
    }

    override fun setOnItemLongClick(block: (itemView: View, model: ViewItemRepresentable?, position: Int) -> Unit) {
        onItemLongClickBlock = block
    }

    override fun getItemViewType(position: Int): Int {
        if (viewItemsArrayList[position] !is BindingViewItem<*, *>) {
            throw Throwable("ViewItem type mismatch, item should be of type BindingViewItem")
        }

        return viewItemsArrayList[position].layoutResourceId
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        return BindingViewHolder(
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), viewType, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return viewItemsArrayList.size
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {

        (viewItemsArrayList[holder.adapterPosition] as BindingViewItem<ViewItemRepresentable, ViewDataBinding>)
            .bind(holder.binding, holder.adapterPosition)

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

    inner class BindingViewHolder(
        val binding: ViewDataBinding
    ) : RecyclerView.ViewHolder(binding.root)
}