package com.recyclerviewbuilder.library

import android.view.View

interface BaseAdapterInterface {
    var viewItemsArrayList: ViewItemArrayList

    fun notifyDataSetChanged()
    fun notifyItemChanged(position: Int)
    fun notifyItemInserted(position: Int)
    fun notifyItemMoved(fromPosition: Int, toPosition: Int)
    fun notifyItemRemoved(position: Int)
    fun setOnItemClick(block: (itemView: View, model: ViewItemRepresentable?, position: Int) -> Unit)
    fun setOnItemLongClick(block: (itemView: View, model: ViewItemRepresentable?, position: Int) -> Unit)
}