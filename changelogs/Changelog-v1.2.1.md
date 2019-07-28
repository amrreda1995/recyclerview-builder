## What's new in v1.2.1

### - Bugs fixed


### - Performance enhanced by setting the adapter to use stable ids for each view item


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
