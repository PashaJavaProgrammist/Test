package com.freshly.interview.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.freshly.interview.R

class MainAdapter(
    private val list: MutableList<EventPresentation> = mutableListOf(),
    private val onEventClick: (url: String) -> Unit,
    private val onFavoriteClick: (id: Long, favorite: Boolean) -> Unit,
) : RecyclerView.Adapter<MainViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(
            R.layout.item_event,
            null,
            false
        ).apply {
            layoutParams = ConstraintLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        return MainViewHolder(
            view = view,
            onEventClick = onEventClick,
            onFavoriteClick = onFavoriteClick,
        )
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun onViewRecycled(holder: MainViewHolder) {
        holder.unbind()
    }

    override fun getItemCount(): Int = list.size

    fun update(newList: List<EventPresentation>) {
        val diffCallback = DiffUtilCallback(newList = newList, oldList = list)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        list.clear()
        list.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }
}

class MainViewHolder(
    private val view: View,
    private val onEventClick: (url: String) -> Unit,
    private val onFavoriteClick: (id: Long, favorite: Boolean) -> Unit,
) : RecyclerView.ViewHolder(view) {

    fun bind(eventPresentation: EventPresentation) {
        view.findViewById<TextView>(R.id.tv_title).text = eventPresentation.name
        view.findViewById<TextView>(R.id.tv_time).text = eventPresentation.time
        view.findViewById<TextView>(R.id.tv_date).text = eventPresentation.date
        view.findViewById<CheckBox>(R.id.cb_fav).apply {
            isChecked = eventPresentation.favorite
            setOnCheckedChangeListener { _, isChecked ->
                onFavoriteClick(eventPresentation.id, isChecked)
            }
        }
        view.setOnClickListener { onEventClick(eventPresentation.url) }
    }

    fun unbind() {
        view.setOnClickListener(null)
        view.findViewById<CheckBox>(R.id.cb_fav).setOnCheckedChangeListener(null)
    }
}

class DiffUtilCallback(
    private val newList: List<EventPresentation>,
    private val oldList: List<EventPresentation>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return newList[newItemPosition].id == oldList[oldItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.id == newItem.id &&
                oldItem.date == newItem.date &&
                oldItem.time == newItem.time &&
                oldItem.name == newItem.name &&
                oldItem.url == newItem.url &&
                oldItem.favorite == newItem.favorite
    }
}