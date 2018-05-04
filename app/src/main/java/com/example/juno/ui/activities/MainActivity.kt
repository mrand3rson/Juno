package com.example.juno.ui.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.juno.R
import com.example.juno.mvp.models.GithubApiService
import com.example.juno.mvp.models.Repository
import com.example.juno.mvp.presenters.MainPresenter
import com.example.juno.mvp.views.MainView
import com.example.juno.mvp.views.MainView.Companion.apiService
import com.example.juno.ui.adapters.SimpleListAdapter
import com.example.juno.ui.adapters.VerticalSpaceItemDecorator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), MainView {

    var mVerticalSpace: Float = 0.0f

    lateinit var mRecycler: RecyclerView
    lateinit var mAdapter: SimpleListAdapter
    lateinit var mToolbar: Toolbar

    private lateinit var mPresenter: MainPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mRecycler = rView
        mToolbar = toolbar

        setSupportActionBar(mToolbar)

        mVerticalSpace = resources.getDimension(R.dimen.recycler_item_vertical_space)
        apiService = GithubApiService.create()

        mPresenter = MainPresenter(this, apiService, Schedulers.io(), AndroidSchedulers.mainThread())
        mPresenter.getRepositories()
    }

    override fun showResult(result: List<Repository>?) {
        super.showResult(result)
        initRecycler(result)
    }

    override fun showError(error: Throwable) {
        super.showError(error)
        Toast.makeText(this, error.message, Toast.LENGTH_LONG).show()

        mAdapter = SimpleListAdapter(resource = R.layout.recycler_item, data = emptyList())
    }

    protected fun initRecycler(data: List<Repository>?) {
        if (data != null) {
            mRecycler.addItemDecoration(VerticalSpaceItemDecorator(mVerticalSpace.toInt()))
            mRecycler.layoutManager = GridLayoutManager(this, 2)

            mPresenter.data = data
            mAdapter = SimpleListAdapter(resource = R.layout.recycler_item, data = data)
            mRecycler.adapter = mAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val menuItem: MenuItem? = menu?.findItem(R.id.action_search)
        val searchView: SearchView? = menuItem?.actionView?.findViewById(R.id.action_search)

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(text: String?): Boolean {
                if ( !searchView.isIconified) {
                    searchView.isIconified = true
                }
                menuItem.collapseActionView()
                mAdapter.data = mPresenter.data
                return false
            }

            override fun onQueryTextChange(text: String): Boolean {
                mAdapter.data = mPresenter.filterRecyclerItems(text)
                mAdapter.notifyDataSetChanged()
                return false
            }
        })

        return super.onPrepareOptionsMenu(menu)
    }
}
