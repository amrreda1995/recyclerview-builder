package com.recyclerviewbuilder.sample.models

import com.recyclerviewbuilder.library.ViewItem
import com.recyclerviewbuilder.library.ViewItemRepresentable
import com.recyclerviewbuilder.sample.R
import com.recyclerviewbuilder.sample.viewitems.ImageAndContentViewItem

class ImageAndContent(
    var user: User = User("Robert Downey"),
    val image: Int = R.drawable.img_3
) : ViewItemRepresentable, Post("Van Gogh, Died: 29 July 1890", "Mar 9 at 8:15 PM") {
    override val viewItem: ViewItem<ViewItemRepresentable>
        get() = ImageAndContentViewItem(this)
}