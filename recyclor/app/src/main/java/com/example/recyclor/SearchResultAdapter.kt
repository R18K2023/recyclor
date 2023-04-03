package com.example.recyclor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * Adapter for the [RecyclerView] in [SearchFragment].
 */

class SearchResultAdapter(private val dataList: ArrayList<Waste>) :
    RecyclerView.Adapter<SearchResultAdapter.ViewHolderClass>() {
    class ViewHolderClass(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val wasteName: TextView = itemView.findViewById(R.id.jätteenNimi)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolderClass {
        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_search_result, parent, false)
        return ViewHolderClass(layout)
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = dataList[position]
        holder.wasteName.text = currentItem.wasteName
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}