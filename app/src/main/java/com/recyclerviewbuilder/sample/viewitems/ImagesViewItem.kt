package com.recyclerviewbuilder.sample.viewitems

import android.view.View
import com.recyclerviewbuilder.library.ViewItem
import com.recyclerviewbuilder.library.ViewItemRepresentable
import com.recyclerviewbuilder.sample.R
import com.recyclerviewbuilder.sample.extensions.imageview.ImageViewMode
import com.recyclerviewbuilder.sample.extensions.imageview.load
import com.recyclerviewbuilder.sample.models.Images
import kotlinx.android.synthetic.main.item_images.view.*

class ImagesViewItem(
    private val model: Images
) : ViewItem<ViewItemRepresentable>(R.layout.item_images, model) {
    override fun bind(itemView: View, viewItemPosition: Int) {
        itemView.userName.text = model.user.name
        itemView.time.text = model.time

        itemView.img_1.load(model.images[0], ImageViewMode.CENTER_CROP)
        itemView.img_2.load(model.images[1], ImageViewMode.CENTER_CROP)
        itemView.img_3.load(model.images[2], ImageViewMode.CENTER_CROP)
        itemView.img_4.load(model.images[3], ImageViewMode.CENTER_CROP)
        itemView.img_5.load(model.images[4], ImageViewMode.CENTER_CROP)
    }
}