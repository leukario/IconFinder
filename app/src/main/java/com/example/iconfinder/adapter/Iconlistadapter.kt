package com.example.iconfinder.adapter



import android.annotation.SuppressLint
import android.app.Activity
import android.net.wifi.WifiConfiguration.AuthAlgorithm.strings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.example.iconfinder.BuildConfig
import com.example.iconfinder.R
import com.example.iconfinder.model.Category
import com.example.iconfinder.model.Icon
import com.example.iconfinder.network.BASE_URL
import com.example.iconfinder.network.askForPermission
import com.example.iconfinder.network.downloadImage
import com.example.iconfinder.network.isPermissionGranted


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

        @SuppressLint("SetTextI18n")
        fun bind(position: Int) {
            val item = list[position]


            with(itemView) {
                   icon_name.text = "${item.categories[0].name}".capitalize()

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

                if (item.is_premium) { // Show Price
                    image_paid.visibility = View.VISIBLE
                    download_btn.visibility = View.GONE
                    price.visibility = View.VISIBLE


                if (item.prices.isNotEmpty()) {
                    val indianprice = (item.prices[0].price) * 78
                    price.text = "$indianprice Rupees"
                }
                } else {
                    image_paid.visibility = View.INVISIBLE
                    download_btn.visibility = View.VISIBLE
                    price.visibility = View.INVISIBLE
                    setButtonClick(item)
                }
                }
            }
        private fun View.setButtonClick(item: Icon) {
            download_btn.setOnClickListener {
                if (!isPermissionGranted(context)) {
                    askForPermission(context as Activity)
                } else {
                    val filePath = item.raster_sizes[0].formats[0].download_url
                    downloadImage(context, filePath)
                }
            }
        }
    }
}
