package com.freshly.interview.presentation

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.freshly.interview.R
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val viewModel: MainViewModel by viewModel()

    private val adapter: MainAdapter by lazy(LazyThreadSafetyMode.NONE) {
        MainAdapter(
            onEventClick = viewModel::openUrl,
            onFavoriteClick = { id, fav -> viewModel.makeFavoriteOrNot(id, fav) },
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()
        bindViewModel()
    }

    private fun initViews() {
        findViewById<RecyclerView>(R.id.rv_list).adapter = adapter
        findViewById<RadioGroup>(R.id.rb_mode).setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_all -> viewModel.showAllEvents(true)
                R.id.rb_fav -> viewModel.showAllEvents(false)
            }
        }
        findViewById<SwipeRefreshLayout>(R.id.swipe_to_refresh).setOnRefreshListener {
            viewModel.swipeToRefresh()
        }
    }

    private fun bindViewModel() {
        viewModel.errorData.observe(this) {
            it?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.eventsData.observe(this) {
            it?.let(adapter::update)
        }
        viewModel.progressData.observe(this) {
            it?.let {
                // todo: use viewBinding instead of findViewById
                findViewById<ProgressBar>(R.id.pb_progress).isVisible = it
                findViewById<SwipeRefreshLayout>(R.id.swipe_to_refresh).isRefreshing = it
            }
        }
        viewModel.browserData.observe(this) { uri ->
            try {
                startActivity(Intent(Intent.ACTION_VIEW, uri))
            } catch (e: ActivityNotFoundException) {
                // todo: handle error
            }
        }
    }
}