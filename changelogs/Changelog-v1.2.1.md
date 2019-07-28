## What's new in v1.2.1

### - Bugs fixed


### - Performance enhanced by setting the adapter to use stable ids for each view item
* Note that, you may to override "hashCode" function in your view item to take the advantage of this enhancement correctly like this 
```kotlin
class ProductViewItem(private val model: Product) : ViewItem<ViewItemRepresentable>(R.layout.item_product, model) {

    //somthing like this
    override fun hashCode(): Int {
        return model.id
    }
	
    override fun bind(itemView: View, viewItemPosition: Int) {
        itemView.titleTextView.text = model.title
    }
}
```


### - New function was added


####  indexOf
* A function returns the index of the given viewItemRepresentable
* Note to use this function successfully, you have to override "equals" function in your model like this 
```kotlin
class Product(val id: Int, val title: String, val date: String): ViewItemRepresentable {

    override val viewItem: AbstractViewItem<ViewItemRepresentable>
        get() = ProductViewItem(this)
        
    //something like this
    override fun equals(other: Any?): Boolean {
        if (other is Product) {
            return other.id == this.id
        }
        return false
    }
}
```
