package com.example.juno.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.juno.mvp.models.Repository
import kotlinx.android.synthetic.main.recycler_item.view.*

/**
 * Created by Andrei on 03.05.2018.
 */
class SimpleListAdapter(val resource: Int, var data: List<Repository>) : RecyclerView.Adapter<SimpleListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context)
                .inflate(resource, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.bind(data[position])
    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val title = itemView?.card?.text

        fun bind(item: Repository) = with(itemView) {
            title?.text = item.name
        }

    }
}