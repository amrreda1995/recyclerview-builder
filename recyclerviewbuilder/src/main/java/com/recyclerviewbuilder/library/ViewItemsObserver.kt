package com.recyclerviewbuilder.library

data class ViewItemsObserver(
    var viewItemsArrayList: ViewItemArrayList = arrayListOf(),
    var clearsOnSet: Boolean = false,
    var appendToEnd: Boolean = true
)