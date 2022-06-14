package com.example.iconfinder.Adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.example.iconfinder.R
import com.example.iconfinder.model.Icon


import kotlinx.android.synthetic.main.grid_item.view.*

class IconListAdapter(var list: List<Icon>)
    : RecyclerView.Adapter<IconListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.grid_item, parent, false)
        )

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    fun submitList(list: List<Icon>) {
        this.list = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(position: Int) {
            val item = list[position]

            with(itemView) {

                if (item.raster_sizes.size > 6)
                    image.load(item.raster_sizes[6].formats[0].preview_url) {
                        crossfade(true)
                        placeholder(R.drawable.ic_loading)
                    }

                else
                    image.load(item.raster_sizes[0].formats[0].preview_url) {
                        crossfade(true)
                        placeholder(R.drawable.ic_loading)
                    }

                }
            }
        }

}
