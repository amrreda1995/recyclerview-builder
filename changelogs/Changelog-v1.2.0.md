## What's new in v1.2.0

### New functions were added


####  isAdapterEmpty
* A function returns whether the adapter is empty or not


####  scrollTo
* A function that accepts viewItemIndex, smoothScroll (true by default) and scrolls to the specific item smoothly
```kotlin
recyclerViewBuilder.scrollTo(viewItemIndex = 7, smoothScroll = false)
```


####  notifyViewItemChanged
* A function that notifies the builder that a certain item was changed and should be reloaded
```kotlin
recyclerViewBuilder.notifyViewItemChanged(atIndex = 8)
```



####  modifyViewItem
* Where T is one of your models (ViewItemRepresentable), It accepts the index, a lambda function (which has the item as a parameter) whatever changes are done to the items takes effect immediately
```kotlin
recyclerViewBuilder.modifyViewItem<ViewItemRepresentable>(atIndex = 5) {
                (it as Product).date = "22/4/2019"
}
```
or
```kotlin
recyclerViewBuilder.modifyViewItem<Product>(atIndex = 5) {
                it.date = "22/4/2019"
}
```



####  modifyViewItems
* Where T is one of your models (ViewItemRepresentable), It accepts the indices, a lambda function (which has the item list as a parameter) whatever changes are done to the items takes effect immediately
```kotlin
recyclerViewBuilder.modifyViewItems<ViewItemRepresentable>(atIndices = 4, 13, 20) {
                it.forEach {
                    when(it) {
                        is Product -> it.title = "New product"
                        is SomeViewItemRepresentableClass -> it.isChecked = true //for example
                    }
                }
}
```



####  insertViewItem
* A function that accepts atIndex, viewItem and inserts the item at specified index
```kotlin
recyclerViewBuilder.insertViewItem(atIndex = 4, viewItem = Product().viewItem)
```
* Note that, if you already have a header or a footer set to the builder and you decide to insert a new view item at their index; the builder by default will insert the new view item after the header or before the footer



####  switchViewItem
* A function that accepts ofIndex, withIndex and replaces the two items
```kotlin
recyclerViewBuilder.switchViewItem(ofIndex = 12, withIndex =  20)
```
* Note that, the builder by default prevents you from switching the header or the footer if you decide to switch some view item with them



####  removeViewItem
* A function that accepts atIndex and removes it from the recylcerview
```kotlin
recyclerViewBuilder.removeViewItem(atIndex = 30)
```
* Note that, you can also use this function to remove the header or the footer (same as setHeader(null) and setFooter(null))
