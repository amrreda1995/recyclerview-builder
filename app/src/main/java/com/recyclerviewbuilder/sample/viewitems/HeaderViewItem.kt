package com.recyclerviewbuilder.sample.viewitems

import android.view.View
import com.recyclerviewbuilder.library.ViewItem
import com.recyclerviewbuilder.library.ViewItemRepresentable
import com.recyclerviewbuilder.sample.R

class HeaderViewItem: ViewItem<ViewItemRepresentable>(R.layout.item_header) {
    override fun bind(itemView: View, viewItemPosition: Int) {
        // do something
    }
}