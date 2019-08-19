package com.recyclerviewbuilder.library

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class Adapter : RecyclerView.Adapter<Adapter.ViewHolder>(), BaseAdapterInterface {
    override var viewItemsArrayList = ArrayList<AbstractViewItem>()

    private var onItemClickBlock: ((View, ViewItemRepresentable?, Int) -> Unit)? = null
    private var onItemLongClickBlock: ((View, ViewItemRepresentable?, Int) -> Unit)? = null

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return viewItemsArrayList[position].hashCode().toLong()
    }

    override fun setOnItemClick(block: (itemView: View, model: ViewItemRepresentable?, position: Int) -> Unit) {
        onItemClickBlock = block
    }

    override fun setOnItemLongClick(block: (itemView: View, model: ViewItemRepresentable?, position: Int) -> Unit) {
        onItemLongClickBlock = block
    }

    override fun getItemViewType(position: Int): Int {
        if (viewItemsArrayList[position] !is ViewItem) {
            throw Throwable("ViewItem type mismatch, item should be of type ViewItem")
        }

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
        (viewItemsArrayList[holder.adapterPosition] as ViewItem).bind(holder.itemView, holder.adapterPosition)

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

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}