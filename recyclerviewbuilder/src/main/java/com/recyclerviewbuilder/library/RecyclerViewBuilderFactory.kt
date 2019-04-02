package com.recyclerviewbuilder.library

import androidx.recyclerview.widget.RecyclerView

class RecyclerViewBuilderFactory(private val recyclerView: RecyclerView) {

    fun buildWithGridLayout(
        columnCount: Int,
        orientation: Int? = null,
        reverseLayout: Boolean? = null
    ): GridRecyclerViewBuilder {
        return GridRecyclerViewBuilder(recyclerView, columnCount, orientation, reverseLayout)
    }

    fun buildWithLinearLayout(
        orientation: Int? = null,
        reverseLayout: Boolean? = null
    ): LinearRecyclerViewBuilder {
        return LinearRecyclerViewBuilder(recyclerView, orientation, reverseLayout)
    }
}