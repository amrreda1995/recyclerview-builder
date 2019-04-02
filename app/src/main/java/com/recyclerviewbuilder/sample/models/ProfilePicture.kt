package com.recyclerviewbuilder.sample.models

import com.recyclerviewbuilder.library.ViewItem
import com.recyclerviewbuilder.library.ViewItemRepresentable
import com.recyclerviewbuilder.sample.R
import com.recyclerviewbuilder.sample.viewitems.ProfilePictureViewItem

class ProfilePicture(
    var user: User = User("Cristian Bale"),
    var profilePicture: Int = R.drawable.avatar
) : ViewItemRepresentable, Post(time = "Mar 9 at 10:05 PM") {
    override val viewItem: ViewItem<ViewItemRepresentable>
        get() = ProfilePictureViewItem(this)
}