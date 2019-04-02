package com.recyclerviewbuilder.library

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LinearRecyclerViewBuilder(
    recyclerView: RecyclerView,
    orientation: Int? = null,
    reverseLayout: Boolean? = null
) : RecyclerViewBuilder(recyclerView, orientation, reverseLayout) {

    init {
        val layoutManager = LinearLayoutManager(recyclerView.context)

        orientation?.let {
            layoutManager.orientation = it
        }

        reverseLayout?.let {
            layoutManager.reverseLayout = it
        }

        recyclerView.layoutManager = layoutManager
    }
}