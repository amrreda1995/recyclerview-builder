# RecyclerViewBuilder

RecyclerViewBuilder is an elegant drop-in generic recyclerview that removes all boilerplate code to build recycler view.

RecyclerViewBuilder is built over:

* Factory design pattern
* Builder design pattern
* Live data (or not)
* DataBinding (or not)
* And lots of abstraction

In short, you simply implement some interfaces and your recycler view will be up on the screen with zero effort.

If you end up using RecyclerViewBuilder in production, I'd love to hear from you. You can reach me through [email](mailto:amr.reda151@gmail.com)

## Changelogs
[v1.1.1](https://github.com/amrreda1995/recyclerview-builder/blob/master/changelogs/Changelog-v1.1.1.md)
[v1.2.0](https://github.com/amrreda1995/recyclerview-builder/blob/master/changelogs/Changelog-v1.2.0.md)

## Preview

| Final Result | Header | Footer |
| ---- | ---- | ---- |
| <img src="https://i.ibb.co/fQ4LHfC/preview-min.png" width="250"/> | <img src="https://i.ibb.co/Tt7n77L/device-2019-04-09-131734-min.png" width="250"/> | <img src="https://i.ibb.co/F7S3SPk/device-2019-04-09-131757-min.png" width="250"/> |

## Installation

### With Jitpack

#### Step 1
Add the **JitPack** repository in your root `build.gradle` at the end of repositories:
```groovy
allprojects {
    repositories {
	//Other repositories...
	maven { url 'https://jitpack.io' }
    }
}
```
#### Step 2
Add the **dependency**:
```groovy
android {
    dataBinding {
	enabled = true
    }
}

dependencies {
     implementation 'com.github.amrreda1995:recyclerview-builder:1.2.0'
}
```
### Without Jitpack

Clone the repo and copy the recycler view builder files into your project.

## Features
* Multiple view item types
* Pagination
* Item click listener
* Item long click listener
* Different states for recycler view (empty views, loading views)
* Execute a block of code when updating adapter is finished
* If you decide to use live data; the builder is by default life cycle aware
* Header / Footer items
* DataBinding

## Usage

* Create a recycler view
* Create one or more layouts for your recycler view item
* Create your model (Which is your normal class that represents an entity I.E user, post...etc)
* Implement ViewItemRepresentable interface

```kotlin
class Product(val title: String, val date: String): ViewItemRepresentable {
    override val viewItem: AbstractViewItem<ViewItemRepresentable>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
}
```

### This leads us to the next step which is view items

* Now we have two routes to take, either use normal ViewItems or BindingViewItems

**Using ViewItem**

* Create a class which implements ViewItem<ViewItemRepresentable> and pass the model which we created in the previous step, also pass the layout id, the model from the constructor to the interface constructor and finally implement the bind function of the interface

```kotlin
class ProductViewItem(private val model: Product) : ViewItem<ViewItemRepresentable>(R.layout.item_product, model) {
    override fun bind(itemView: View, viewItemPosition: Int) {
        itemView.titleTextView.text = model.title
        itemView.dateTextView.text = model.date
    }
}
```

**Using BindingViewItem**
* Make sure your databinding is enabled (it should really be at this point or the library won't even install correctly, but just make sure)
```groovy
android {
    dataBinding {
        enabled = true
    }
}
```

* Then, create a class which implements BindingViewItem<ViewItemRepresentable, ViewDataBinding> and pass the model which we created in the previous step, also pass the layout id, the model from the constructor to the interface constructor. The second generic parameter is of type ViewDataBinding, which is the class that is automtically generated when you enable dataBinding and create an XML built over DataBinding.
Given that you have created an XML named "item_product" the generated class name will be "ItemProductBinding"

```kotlin
class ProductViewItem(private val model: Product) : BindingViewItem<ViewItemRepresentable, ItemProductBinding>(R.layout.item_product, model) {
    override fun bind(binding: ItemProductBinding, viewItemPosition: Int) {
        binding.model = model
    }
}
```

**Or create a ViewItem (for header or footer) which has no implementation and to be used directly without the next step**

```kotlin
class FooterViewItem : ViewItem<ViewItemRepresentable>(R.layout.item_footer)
```

```kotlin
class FooterViewItem : BindingViewItem<ViewItemRepresentable, SomeBindingClass>(R.layout.item_footer)
```

* Now, go back to your original model and return this view item on the overriden viewItem variable

```kotlin
class Product(val title: String, val date: String): ViewItemRepresentable {
    override val viewItem: AbstractViewItem<ViewItemRepresentable>
        get() = ProductViewItem(this)
}
```

* Now you are ready to use the builder and you have two routes either use LiveData or normal arrays.

## Using LiveData (with/without DataBinding)

```kotlin
class MainActivity : AppCompatActivity() {

    private lateinit var recyclerViewBuilder: RecyclerViewBuilder

    private val viewItems = MutableLiveData<ViewItemsObserver>()

    private var models = arrayListOf(
        Product("Product one", "2/4/2019"),
        Product("Product two", "2/4/2019"),
        Product("Product three", "2/4/2019"),
        Product("Product four", "2/4/2019")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerViewBuilder = RecyclerViewBuilderFactory(recyclerView)
            .buildWithLinearLayout(isDataBindingEnabled = true)
            .bindViewItems(this, viewItems)

        viewItems.value = ViewItemsObserver(ArrayList(models.map { it.viewItem }), false)
    }
}
```

The Live Data is of type "ViewItemsObserver" class which accepts two parameters; viewItemsArrayList (ArrayList) and clearsOnSet (Boolean). If you'd like to clear the items each time you set a value for the live data; you set clearsOnSet to true, otherwise just leave it to default value which is false and it appends the added value

## Using Normal Arrays (with/without DataBinding)

```kotlin
class MainActivity : AppCompatActivity() {

    private lateinit var recyclerViewBuilder: RecyclerViewBuilder

    private var models = arrayListOf(
        Product("Product one", "2/4/2019"),
        Product("Product two", "2/4/2019"),
        Product("Product three", "2/4/2019"),
        Product("Product four", "2/4/2019")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerViewBuilder = RecyclerViewBuilderFactory(recyclerView)
            .buildWithLinearLayout()

        recyclerViewBuilder.setViewItems(ArrayList(models.map { it.viewItem }), false)
    }
}
```

### Supported functionality

| Function        | Description      |
| ------------- |-------------|
| buildWithLinearLayout      | A function that accepts isDataBindingEnabled, orientation and reverselayout parameters and builds the linear layout recycler view     |
| buildWithGridLayout     | A function that accepts isDataBindingEnabled, columnCount, orientation and reverselayout parameters and builds the grid layout recycler view     |
| setEmptyView      | A function that accepts a view which is used as a placeholder when the recycler view is empty     |
| setLoadingView     | A function that accepts a view which is used as a placeholder when the recycler view is in loading state     |
| startLoading      |  A function that manually triggers loading state on   |
| finishLoading      | A function that manually triggers loading state off    |
| isLoading      | A function which returns whether the recycler view is in loading state     |
| setViewItems      | A function that accepts viewItemsArrayList and clearsOnSet parameters and either replaces the items or appends them     |
| bindViewItems      | A function that accepts lifecycleOwner and viewItems of type MutableLiveData<ViewItemsObserver> as explained previously     |
| notifyDataSetChanged      | A function that reloads the recycler view manually     |
| setEmptyAdapter      | A function that manually clears the recycler view    |
| setOnItemClick      | A function that accepts a lambda function which has three paramaters (itemView, model, position)   |
| setOnItemLongClick      |  A function that accepts a lambda function which has three paramaters (itemView, model, position)    |
| onUpdatingAdapterFinished     |  A function that acceps a lambda and is invoked once the adapter has been updated (filled with view items or was cleared from them)    |
| setPaginationEnabled     |   A function that either enables or disables pagination feature   |
| onPaginate     |  A function that accepts a lambda which is triggered once the recylcer view has reached its end to trigger pagination (given that pagination feature is enabled)    |
| setHeader     |  A function that accepts a view item and always sets it as the first item no matter how many items are added or removed    |
| setFooter     |  A function that accepts a view item and always sets it as the last item no matter how many items are added or removed    |
| isAdapterEmpty      |  A function returns whether the adapter is empty or not   |
| scrollTo      |  A function that accepts viewItemIndex, smoothScroll (true by default) and scrolls to the specific item smoothly    |
| notifyViewItemChanged      |  A function that notifies the builder that a certain item was changed and should be reloaded   |
| modifyViewItem<`T`>      |  Where T is one of your models (ViewItemRepresentable), It accepts the index, a lambda function (which has the item as a parameter) whatever changes are done to the items takes effect immediately    |
| modifyViewItems<`T`>      |  Where T is one of your models (ViewItemRepresentable), It accepts the indices, a lambda function (which has the item list as a parameter) whatever changes are done to the items takes effect immediately   |
| insertViewItem      |  A function that accepts atIndex, viewItem and inserts the item at specified index  |
| switchViewItem      |  A function that accepts ofIndex, withIndex and replaces the two items   |
| removeViewItem      |  A function that accepts atIndex and removes it from the recylcerview   |

## **Example project**

  * Clone the repo using `git clone https://github.com/amrreda1995/recyclerview-builder.git`
  * Navigate to the cloned folder
  * Open it in the android studio
  * Build and run to see it in action (It also includes multiple view items if you want to expirement around with it!


## About RecyclerViewBuilder

Initially, I just wanted to use a library that encapuslates all of this logic and of course I've found more than one. But I asked myself why not make a library for myself ? And also deliver it for everyone to use (Because boilerplate code is boring, am I right?).

Also RecyclerViewBuilder is built completely in code, no external libraries. It's highly flexible and can be customized easily with minimum effort.


## Support

Please, don't hesitate to [file an issue](https://github.com/amrreda1995/RecyclerviewBuilder/issues/new) if you have questions.

## License

RecyclerViewBuilder is released under the MIT license. [See LICENSE](https://github.com/amrreda1995/RecyclerviewBuilder/blob/master/LICENSE) for details.

