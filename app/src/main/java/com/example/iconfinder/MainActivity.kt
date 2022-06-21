package com.example.iconfinder



import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.iconfinder.adapter.IconListAdapter
import com.example.iconfinder.model.Icon
import com.example.iconfinder.viewModel.MainActivityViewModel
import com.example.iconfinder.network.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.grid_item.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: IconListAdapter
    private lateinit var layoutManager: GridLayoutManager
    private lateinit var viewModel: MainActivityViewModel
    var query = "default"
    val defaultQuery = "default"
    private var startIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        title = ""

        init()

        if (savedInstanceState != null) {
            query = savedInstanceState.getString(QUERY, defaultQuery)
            startIndex = savedInstanceState.getInt(START_INDEX, 0)
        }

        if (isNetworkConnected(this))
            loadData(query, 10, startIndex)
        else
            toast("No internet connection available")
        addOnScrollListener()
    }

    private fun init() {
        showLoading(false)
        adapter = IconListAdapter(listOf())
        layoutManager = GridLayoutManager(this, 2)

        with(icon_list) {
            layoutManager = this@MainActivity.layoutManager
            adapter = this@MainActivity.adapter
        }

        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
    }

    private fun loadData(query: String, count: Int, index: Int) {
        showLoading(true)
        //Log.d("InLoaddata", "hello")
        viewModel.getIcons(query, count, index).observe(this,
            { list ->

                listItems.addAll(list)
                removeDuplicateValues(listItems)
                adapter.submitList(listItems)
               //Log.d("Size", listItems.size.toString())//The Api is not sending any data it can be shown in logcat

                showLoading(false)
               if (list.isEmpty()) toast("No more results found!")
              //  Log.d("Main", listItems.size.toString())
            })
    }

    private fun removeDuplicateValues(Items: List<Icon>) {
        val map = LinkedHashMap<Int, Icon>()

        for (item in Items) {
            map[item.icon_id] = item
        }
        listItems.clear()
        listItems.addAll(map.values)
    }

    private fun addOnScrollListener() {
        icon_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val count = layoutManager.itemCount
                Log.d("Scroll2","here")

                    val holderCount = layoutManager.childCount
                    val oldCount = layoutManager.findFirstVisibleItemPosition()
                    Log.d("Scroll1","here")

                    if (holderCount + oldCount >= count - 4 && !isLoading) {
                        Log.d("Scroll","here")
                        startIndex += 20
                        viewModel.getIcons(query, NUMBER_OF_ICONS, startIndex)
                    }
            }
        })
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val search = menu?.findItem(R.id.action_search)
        val searchView = search?.actionView as SearchView


        search.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean = true

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                if (query != defaultQuery) {
                    removeAndReload()
                    viewModel.getIcons(defaultQuery, NUMBER_OF_ICONS, 0)
                }
                return true
            }

        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query == null) return false

                removeAndReload()
                this@MainActivity.query = query
                viewModel.getIcons(query, NUMBER_OF_ICONS, 0)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean = false
        })
        return super.onCreateOptionsMenu(menu)
    }

    private fun removeAndReload() {
        listItems.clear()
        adapter.submitList(listOf())
        showLoading(true)
    }

    private fun showLoading(boolean: Boolean) {
        if (boolean) progress_bar.visibility = View.VISIBLE
        else progress_bar.visibility = View.INVISIBLE
    }

    companion object {
        private val listItems = mutableListOf<Icon>()
    }
}
