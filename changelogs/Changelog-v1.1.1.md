## What's new in v1.1.1

* setPaginationFeatureEnabled function has been renamed to setPaginationEnabled.
* Fix a bug with LiveData and how it interacts with the lifecycle owner, so, in bindViewItems method; you will pass lifecycleOwner instead of lifecycle
* Introduce DataBinding Support with a new ViewItem type BindingViewItem
* Added the ability to set header / footer for the recyclerview
* The ViewItem now accepts optional models which means you can create a ViewItem without a model at all (can be used for headers / footers for example)
* The bind function in ViewItem / BindingViewItem is not abstract anymore and has default implementation you can override it and Android Studio will provide super.bind call which is completely useless and unneeded so feel free to delete it.
* ViewItem / BindingViewItem will have their position on the "bind" function enabling you to find out quickly which item was interacted with via the position (e.g actions for custom buttons in items)

