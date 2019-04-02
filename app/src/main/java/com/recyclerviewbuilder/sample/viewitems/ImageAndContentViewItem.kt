package com.recyclerviewbuilder.sample.viewitems

import android.view.View
import com.recyclerviewbuilder.library.ViewItem
import com.recyclerviewbuilder.library.ViewItemRepresentable
import com.recyclerviewbuilder.sample.R
import com.recyclerviewbuilder.sample.extensions.imageview.ImageViewMode
import com.recyclerviewbuilder.sample.extensions.imageview.load
import com.recyclerviewbuilder.sample.models.ImageAndContent
import kotlinx.android.synthetic.main.item_image_and_content.view.*

class ImageAndContentViewItem(
    private val model: ImageAndContent
) : ViewItem<ViewItemRepresentable>(R.layout.item_image_and_content, model) {
    override fun bind(itemView: View) {
        itemView.userName.text = model.user.name
        itemView.time.text = model.time
        itemView.content.text = model.content
        itemView.img.load(model.image, ImageViewMode.CENTER_CROP)
    }
}