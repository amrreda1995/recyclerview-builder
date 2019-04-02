package com.recyclerviewbuilder.sample.extensions.imageview

import android.widget.ImageView
import com.bumptech.glide.Glide

enum class ImageViewMode {
    NORMAL, CENTER_CROP, FIT_CENTER
}

fun ImageView.load(url: String, mode: ImageViewMode = ImageViewMode.NORMAL) {
    val load = Glide.with(context).load(url)
    when (mode) {
        ImageViewMode.NORMAL -> load.into(this)
        ImageViewMode.CENTER_CROP -> load.centerCrop().into(this)
        ImageViewMode.FIT_CENTER -> load.fitCenter().into(this)
    }
}

fun ImageView.load(resourceId: Int, mode: ImageViewMode = ImageViewMode.NORMAL) {
    val load = Glide.with(context).load(resourceId)
    when (mode) {
        ImageViewMode.NORMAL -> load.into(this)
        ImageViewMode.CENTER_CROP -> load.centerCrop().into(this)
        ImageViewMode.FIT_CENTER -> load.fitCenter().into(this)
    }
}