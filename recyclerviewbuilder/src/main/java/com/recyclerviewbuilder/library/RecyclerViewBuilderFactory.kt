package com.recyclerviewbuilder.library

import androidx.recyclerview.widget.RecyclerView

class RecyclerViewBuilderFactory(private val recyclerView: RecyclerView) {

    fun buildWithGridLayout(
        isDataBindingEnabled: Boolean = false,
        columnCount: Int,
        orientation: Int? = null,
        reverseLayout: Boolean? = null,
        canScrollHorizontally: Boolean? = null,
        canScrollVertically: Boolean? = null
    ): GridRecyclerViewBuilder {
        return GridRecyclerViewBuilder(recyclerView, isDataBindingEnabled, columnCount, orientation, reverseLayout, canScrollHorizontally, canScrollVertically)
    }

    fun buildWithLinearLayout(
        isDataBindingEnabled: Boolean = false,
        orientation: Int? = null,
        reverseLayout: Boolean? = null,
        canScrollHorizontally: Boolean? = null,
        canScrollVertically: Boolean? = null
    ): LinearRecyclerViewBuilder {
        return LinearRecyclerViewBuilder(recyclerView, isDataBindingEnabled, orientation, reverseLayout, canScrollHorizontally, canScrollVertically)
    }
}