package com.recyclerviewbuilder.library

import android.view.View
import androidx.databinding.ViewDataBinding

abstract class AbstractViewItem<T>(
    open val layoutResourceId: Int,
    open val dataModel: T? = null
) where T : ViewItemRepresentable

abstract class ViewItem<T>(
    override val layoutResourceId: Int, override val dataModel: T? = null
) : AbstractViewItem<T>(layoutResourceId, dataModel) where T : ViewItemRepresentable {
    open fun bind(itemView: View, viewItemPosition: Int) {}
}

abstract class BindingViewItem<T, U>(
    override val layoutResourceId: Int, override val dataModel: T? = null
) : AbstractViewItem<T>(layoutResourceId, dataModel) where T : ViewItemRepresentable, U : ViewDataBinding {
    open fun bind(binding: U, viewItemPosition: Int) {}
}