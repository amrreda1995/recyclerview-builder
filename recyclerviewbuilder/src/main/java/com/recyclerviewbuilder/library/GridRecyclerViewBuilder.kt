package com.recyclerviewbuilder.library

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GridRecyclerViewBuilder(
    recyclerView: RecyclerView,
    isDataBindingEnabled: Boolean,
    columnCount: Int,
    orientation: Int? = null,
    reverseLayout: Boolean? = null,
    canScrollHorizontally: Boolean? = null,
    canScrollVertically: Boolean? = null
) : RecyclerViewBuilder(recyclerView, isDataBindingEnabled, orientation, reverseLayout, columnCount) {

    init {
        val layoutManager = object : GridLayoutManager(recyclerView.context, columnCount) {

            override fun canScrollHorizontally(): Boolean {
                canScrollHorizontally?.let {
                    return it
                } ?: run {
                    return super.canScrollHorizontally()
                }
            }

            override fun canScrollVertically(): Boolean {
                canScrollVertically?.let {
                    return it
                } ?: run {
                    return super.canScrollVertically()
                }
            }
        }

        orientation?.let {
            layoutManager.orientation = it
        }

        reverseLayout?.let {
            layoutManager.reverseLayout = it
        }

        recyclerView.layoutManager = layoutManager
    }
}