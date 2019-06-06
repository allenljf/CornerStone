package com.trubuzz.cornerstone.base

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.jetbrains.anko.layoutInflater
import java.util.ArrayList

class BaseAdapter<T>(val layoutResourceId: Int, val items: List<T>?, val init: (View, T) -> Unit) :
        RecyclerView.Adapter<BaseAdapter.ViewHolder<T>>() {

    var mItems: List<T>? = null

    init {
        mItems = items
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<T> {
        val view = parent.context.layoutInflater.inflate(layoutResourceId, parent, false)
        return ViewHolder(view, init)
    }

    override fun onBindViewHolder(holder: ViewHolder<T>, position: Int) {
        holder.bindForecast(mItems!!.get(position))
    }

    override fun getItemCount() = mItems!!.size

    class ViewHolder<in T>(view: View, val init: (View, T) -> Unit) : RecyclerView.ViewHolder(view) {
        fun bindForecast(item: T) {
            with(item) {
                init(itemView, item)
            }
        }
    }

    fun setData(items: List<T>?){
        mItems = items
        notifyDataSetChanged()
    }
}
