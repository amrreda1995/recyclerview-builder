package com.recyclerviewbuilder.library

import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.addScrollListenerForPagination(
    orientation: Int? = null,
    reverseLayout: Boolean? = null,
    block: () -> Unit
) {
    this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            // 1 = down; -1 = up; 0 = up or down
            var direction = 1

            reverseLayout?.let {
                if (it) {
                    direction = -1
                }
            }

            orientation?.let {
                when (it) {
                    RecyclerView.VERTICAL -> {
                        if (!recyclerView.canScrollVertically(direction)) {
                            block()
                        }
                    }

                    RecyclerView.HORIZONTAL -> {
                        if (!recyclerView.canScrollHorizontally(direction)) {
                            block()
                        }
                    }
                }
            } ?: kotlin.run {
                if (!recyclerView.canScrollVertically(direction)) {
                    block()
                }
            }
        }
    })
}