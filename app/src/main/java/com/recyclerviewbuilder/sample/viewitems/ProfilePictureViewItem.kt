package com.recyclerviewbuilder.sample.viewitems

import android.view.View
import com.recyclerviewbuilder.library.ViewItem
import com.recyclerviewbuilder.library.ViewItemRepresentable
import com.recyclerviewbuilder.sample.R
import com.recyclerviewbuilder.sample.extensions.imageview.load
import com.recyclerviewbuilder.sample.models.ProfilePicture
import kotlinx.android.synthetic.main.item_change_profile_picture.view.*

class ProfilePictureViewItem(
    private val model: ProfilePicture
) : ViewItem<ViewItemRepresentable>(R.layout.item_change_profile_picture, model) {
    override fun bind(itemView: View, viewItemPosition: Int) {
        itemView.userName.text = model.user.name
        itemView.time.text = model.time

        itemView.img.load(model.profilePicture)
    }
}