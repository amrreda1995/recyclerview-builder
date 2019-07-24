package com.recyclerviewbuilder.library

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LinearRecyclerViewBuilder(
    recyclerView: RecyclerView,
    isDataBindingEnabled: Boolean,
    orientation: Int? = null,
    reverseLayout: Boolean? = null,
    canScrollHorizontally: Boolean? = null,
    canScrollVertically: Boolean? = null
) : RecyclerViewBuilder(recyclerView, isDataBindingEnabled, orientation, reverseLayout) {

    init {
        val layoutManager = object : LinearLayoutManager(recyclerView.context) {

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