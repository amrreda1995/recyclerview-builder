package com.recyclerviewbuilder.library

import android.view.View

abstract class ViewItem<T>(val layoutResourceId: Int, val dataModel: T) {
    abstract fun bind(itemView: View)
}