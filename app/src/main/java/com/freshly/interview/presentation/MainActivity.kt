package com.freshly.interview.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.freshly.interview.R
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.errorData.observe(this) {
            it?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.eventsData.observe(this) {
            it?.let {
                Toast.makeText(
                    this@MainActivity,
                    it.toTypedArray().contentToString(),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}