package com.recyclerviewbuilder.library

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GridRecyclerViewBuilder(
    recyclerView: RecyclerView,
    isDataBindingEnabled: Boolean,
    columnCount: Int,
    orientation: Int? = null,
    reverseLayout: Boolean? = null
) : RecyclerViewBuilder(recyclerView, isDataBindingEnabled, orientation, reverseLayout, columnCount) {

    init {
        val layoutManager = GridLayoutManager(recyclerView.context, columnCount)

        orientation?.let {
            layoutManager.orientation = it
        }

        reverseLayout?.let {
            layoutManager.reverseLayout = it
        }

        recyclerView.layoutManager = layoutManager
    }
}