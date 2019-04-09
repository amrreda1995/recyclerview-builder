package com.recyclerviewbuilder.sample.viewitems

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.recyclerviewbuilder.library.BindingViewItem
import com.recyclerviewbuilder.library.ViewItemRepresentable
import com.recyclerviewbuilder.sample.R
import com.recyclerviewbuilder.sample.databinding.ItemBindingChangeProfilePictureBinding
import com.recyclerviewbuilder.sample.extensions.imageview.ImageViewMode
import com.recyclerviewbuilder.sample.extensions.imageview.load
import com.recyclerviewbuilder.sample.models.BindingProfilePicture

class BindingViewItem(
    val model: BindingProfilePicture
) : BindingViewItem<ViewItemRepresentable, ItemBindingChangeProfilePictureBinding>(
    R.layout.item_binding_change_profile_picture, model
) {
    override fun bind(binding: ItemBindingChangeProfilePictureBinding, viewItemPosition: Int) {
        binding.model = model
    }

    companion object {
        @JvmStatic
        @BindingAdapter("android:src")
        fun setImageView(imageView: ImageView, imageResourceId: Int) {
            imageView.load(imageResourceId)
        }
    }
}