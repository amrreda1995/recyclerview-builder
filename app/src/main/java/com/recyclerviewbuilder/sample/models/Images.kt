package com.recyclerviewbuilder.sample.models

import com.recyclerviewbuilder.library.AbstractViewItem
import com.recyclerviewbuilder.library.ViewItem
import com.recyclerviewbuilder.library.ViewItemRepresentable
import com.recyclerviewbuilder.sample.R
import com.recyclerviewbuilder.sample.viewitems.ImagesViewItem

class Images(
    var user: User = User("Ben Affleck"),
    var images: ArrayList<Int> = arrayListOf(
        R.drawable.img_1,
        R.drawable.img_2,
        R.drawable.img_3,
        R.drawable.img_4,
        R.drawable.img_6
    )
) : ViewItemRepresentable, Post(time = "Mar 9 at 9:10 PM") {
    override val viewItem: AbstractViewItem
        get() = ImagesViewItem(this)
}