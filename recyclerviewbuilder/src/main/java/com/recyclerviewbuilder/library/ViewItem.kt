package com.recyclerviewbuilder.library

import android.view.View
import androidx.databinding.ViewDataBinding

abstract class AbstractViewItem(
    open val layoutResourceId: Int,
    open val dataModel: ViewItemRepresentable? = null
)

abstract class ViewItem(
    override val layoutResourceId: Int, override val dataModel: ViewItemRepresentable? = null
) : AbstractViewItem(layoutResourceId, dataModel) {
    open fun bind(itemView: View, viewItemPosition: Int) {}
}

abstract class BindingViewItem<T>(
    override val layoutResourceId: Int, override val dataModel: ViewItemRepresentable? = null
) : AbstractViewItem(layoutResourceId, dataModel) where T : ViewDataBinding {
    open fun bind(binding: T, viewItemPosition: Int) {}
}