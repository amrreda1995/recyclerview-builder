package com.recyclerviewbuilder.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.recyclerviewbuilder.library.RecyclerViewBuilder
import com.recyclerviewbuilder.library.RecyclerViewBuilderFactory
import com.recyclerviewbuilder.library.ViewItemRepresentable
import com.recyclerviewbuilder.library.ViewItemsObserver
import com.recyclerviewbuilder.sample.models.BindingProfilePicture
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class BindingActivity : AppCompatActivity() {

    private lateinit var recyclerViewBuilder: RecyclerViewBuilder

    private val viewItems = MutableLiveData<ViewItemsObserver>()

    private lateinit var models: ArrayList<ViewItemRepresentable>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        models = arrayListOf()
        models.add(BindingProfilePicture())

        recyclerViewBuilder = RecyclerViewBuilderFactory(recyclerView)
            .buildWithLinearLayout(true)
            .bindViewItems(this, viewItems)

        viewItems.value = ViewItemsObserver(ArrayList(models.map { it.viewItem }))
    }

    override fun onPause() {
        super.onPause()

        CoroutineScope(Dispatchers.IO).launch {
            delay(1000)

            withContext(Dispatchers.Main) {
                viewItems.value = ViewItemsObserver(ArrayList(models.map { it.viewItem }))
            }
        }
    }
}
