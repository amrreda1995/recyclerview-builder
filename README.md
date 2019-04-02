# RecyclerViewBuilder

RecyclerViewBuilder is an elegant drop-in generic recyclerview that removes all boilerplate code to build recycler view.

RecyclerViewBuilder is built over:

* Factory design pattern
* Builder design pattern
* Live data (or not)
* And lots of abstraction

In short, you simply implement some interfaces and your recycler view will be up on the screen with zero effort.

If you end up using RecyclerViewBuilder in production, I'd love to hear from you. You can reach me through [email](mailto:amr.reda151@gmail.com)

## Preview

| Final Result |
| ---- |
| <img src="https://i.ibb.co/fQ4LHfC/preview-min.png" width="250"/> |

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
dependencies {
  implementation 'com.github.amrreda1995:recyclerview-builder:1.0.0'
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

## Usage

* Create a recycler view
* Create one or more layouts for your recycler view item
* Create your model (Which is your normal class that represents an entity I.E user, post...etc)
* Implement ViewItemRepresentable interface

```kotlin
class Product(val title: String, val date: String): ViewItemRepresentable {
    override val viewItem: ViewItem<ViewItemRepresentable>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
}
```
### This leads us to the next step which is view items

* Create a class which implements ViewItem<ViewItemRepresentable> and pass the model which we created in the previous step, also pass the layout id, the model from the constructor to the interface constructor and finally implement the bind function of the interface

```kotlin
class ProductViewItem(private val model: Product) : ViewItem<ViewItemRepresentable>(R.layout.item_product, model) {
    override fun bind(itemView: View) {
        itemView.titleTextView.text = model.title
        itemView.dateTextView.text = model.date
    }
}
```

* Now, go back to your original model and return this view item on the overriden viewItem variable

```kotlin
class Product(val title: String, val date: String): ViewItemRepresentable {
    override val viewItem: ViewItem<ViewItemRepresentable>
        get() = ProductViewItem(this)
}
```

* Now you are ready to use the builder and you have two routes either use LiveData or normal arrays.

## Using Live Data

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
            .buildWithLinearLayout()
            .bindViewItems(lifecycle, viewItems)

        viewItems.value = ViewItemsObserver(ArrayList(models.map { it.viewItem }), false)
    }
}
```

The Live Data is of type "ViewItemsObserver" class which accepts two parameters clearsOnSet (Boolean), and the viewItems (arrayList). If you'd like to clear the items each time you set a value for the live data; you set clearsOnSet to true, otherwise just leave it to default value which is false and it appends the added value

## Using Normal Arrays

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
| buildWithLinearLayout      | A function that accepts orientation and reverselayout parameters and builds the linear layout recycler view     |
| buildWithGridLayout     | A function that accepts columnCount, orientation and reverselayout parameters and builds the grid layout recycler view     |
| setEmptyView      | A function that accepts a view which is used as a placeholder when the recycler view is empty     |
| setLoadingView     | A function that accepts a view which is used as a placeholder when the recycler view is in loading state     |
| startLoading      |  A function that manually triggers loading state on   |
| finishLoading      | A function that manually triggers loading state off    |
| isLoading      | A function which returns whether the recycler view is in loading state     |
| setViewItems      | A function that accepts viewItemsArrayList and clearsOnSet parameters and either replaces the items or appends them     |
| bindViewItems      | A function that accepts lifecycle of the owner view and viewItems of type MutableLiveData<ViewItemsObserver> as explained previously     |
| notifyDataSetChanged      | A function that reloads the recycler view manually     |
| setEmptyAdapter      | A function that manually clears the recycler view    |
| setOnItemClick      | A function that accepts a lambda function which has three paramaters (itemView, model, position)   |
| setOnItemLongClick      |  A function that accepts a lambda function which has three paramaters (itemView, model, position)    |
| onUpdatingAdapterFinished     |  A function that acceps a lambda and is invoked once the adapter has been filled with the items    |
| setPaginationFeatureEnabled     |   A function that either enables or disables pagination feature   |
| onPaginate     |  A function that accepts a lambda which is triggered once the recylcer view has reached its end to trigger pagination (given that pagination feature is enabled)    |

## **Example project**

  * Clone the repo using `git clone https://github.com/amrreda1995/RecyclerviewBuilder.git`
  * Navigate to the cloned folder
  * Open in in the android studio
  * Build and run to see it in action (It also includes multiple view items if you want to expirement around with it!)

## About RecyclerViewBuilder

Initially, I just wanted to use a library that encapuslates all of this logic and of course I've found more than one. But I asked myslef why not make a library for myself ? And also deliver it for everyone to use (Because boilerplate code is boring, am I right?).

Also RecyclerViewBuilder is built completely in code, no external libraries. It's highly flexible and can be customized easily with minimum effort.


## Support

Please, don't hesitate to [file an issue](https://github.com/amrreda1995/RecyclerviewBuilder/issues/new) if you have questions.

## License

RecyclerViewBuilder is released under the MIT license. [See LICENSE](https://github.com/amrreda1995/RecyclerviewBuilder/blob/master/LICENSE) for details.
