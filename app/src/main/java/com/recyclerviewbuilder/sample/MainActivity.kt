package com.recyclerviewbuilder.sample

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.recyclerviewbuilder.library.RecyclerViewBuilder
import com.recyclerviewbuilder.library.RecyclerViewBuilderFactory
import com.recyclerviewbuilder.library.ViewItemRepresentable
import com.recyclerviewbuilder.library.ViewItemsObserver
import com.recyclerviewbuilder.sample.models.ImageAndContent
import com.recyclerviewbuilder.sample.models.Images
import com.recyclerviewbuilder.sample.models.ProfilePicture
import com.recyclerviewbuilder.sample.viewitems.FooterViewItem
import com.recyclerviewbuilder.sample.viewitems.HeaderViewItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerViewBuilder: RecyclerViewBuilder

    private val viewItems = MutableLiveData<ViewItemsObserver>()

    private lateinit var models: ArrayList<ViewItemRepresentable>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        models = arrayListOf()
        models.add(ImageAndContent())
        models.add(Images())
        models.add(ProfilePicture())

        recyclerViewBuilder = RecyclerViewBuilderFactory(recyclerView)
                .buildWithLinearLayout()
                .setLoadingView(loading)
                .setEmptyView(noData)
                .setHeader(HeaderViewItem())
                .setFooter(FooterViewItem())
                .bindViewItems(this, viewItems)
                .setPaginationEnabled(true)
                .onUpdatingAdapterFinished {
                    Toast.makeText(this, "Job done!", Toast.LENGTH_SHORT).show()
                }.onPaginate {
                    CoroutineScope(Dispatchers.IO).launch {
                        delay(2000)

                        withContext(Dispatchers.Main) {
                            //                      recyclerViewBuilderFactory.setViewItems(viewItems = ArrayList(models.map { it.viewItem }))
                            viewItems.value = ViewItemsObserver(ArrayList(models.map { it.viewItem }))
                            this@launch
                        }
                    }
                }.startLoading()

        CoroutineScope(Dispatchers.IO).launch {
            delay(2000)

            withContext(Dispatchers.Main) {
                //              recyclerViewBuilderFactory.setViewItems(viewItems = ArrayList(models.map { it.viewItem }))
                viewItems.value = ViewItemsObserver(ArrayList(models.map { it.viewItem }))
                this@launch
            }
        }
    }
}
