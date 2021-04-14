package com.freshly.interview.presentation

import android.os.Bundle
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.freshly.interview.R
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val viewModel: MainViewModel by viewModel()

    private val adapter: MainAdapter by lazy(LazyThreadSafetyMode.NONE) {
        MainAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()
        bindViewModel()
    }

    private fun initViews() {
        findViewById<RecyclerView>(R.id.rv_list).adapter = adapter
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
            }
        }
    }
}