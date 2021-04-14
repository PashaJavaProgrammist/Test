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

class MainAdapter(private val list: MutableList<EventPresentation> = mutableListOf()) :
    RecyclerView.Adapter<MainViewHolder>() {

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
        return MainViewHolder(view = view)
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

class MainViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(eventPresentation: EventPresentation) {
        view.findViewById<TextView>(R.id.tv_title).text = eventPresentation.name
        view.findViewById<TextView>(R.id.tv_time).text = eventPresentation.time
        view.findViewById<TextView>(R.id.tv_date).text = eventPresentation.date
        view.findViewById<CheckBox>(R.id.cb_fav).isChecked = eventPresentation.favorite
    }

    fun unbind() {
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