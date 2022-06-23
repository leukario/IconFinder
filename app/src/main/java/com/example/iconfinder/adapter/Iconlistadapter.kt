package com.example.iconfinder.adapter



import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.example.iconfinder.R
import com.example.iconfinder.model.Icon
import com.example.iconfinder.network.*


import kotlinx.android.synthetic.main.grid_item.view.*
import java.io.File

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

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<Icon>) {
        this.list = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        @SuppressLint("SetTextI18n")
        fun bind(position: Int) {
            val item = list[position]


            with(itemView) {
                if(item.categories.isNotEmpty()){
                    if(item.categories[0].name!=null)
                        icon_name.text = "${item.categories[0].name}"
                }

                if (item.raster_sizes.size > 6)
                    imageicon.load(item.raster_sizes[6].formats[0].preview_url)
                else
                    imageicon.load(item.raster_sizes[0].formats[0].preview_url)

                if (item.is_premium) {
                    image_paid.visibility = View.VISIBLE
                    download_btn.visibility = View.GONE
                    price.visibility = View.VISIBLE

                      if (item.prices.isNotEmpty()) {
                           val indianprice = (item.prices[0].price) * 78
                           price.text = "₹$indianprice"
                         }
                }
                else {
                    image_paid.visibility = View.INVISIBLE
                    download_btn.visibility = View.VISIBLE
                    price.visibility = View.INVISIBLE

                    download_btn.setOnClickListener {
                        if (!isPermissionGranted(context)) {
                            requestPermissions(context as Activity,
                                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                2
                            )
                            if(isPermissionGranted(context))
                            {
                                val filePath = item.raster_sizes[0].formats[0].download_url
                                download(context, filePath,item.categories[0].name)
                            }
                        }
                        else {
                          //  Log.d("else","Request Accepted")
                            val filePath = item.raster_sizes[0].formats[0].download_url
                           download(context, filePath,item.categories[0].name)
                        }
                    }
                    }
                }
            }

        private fun download(context: Context, url:String, fileName:String) {
            if (isPermissionGranted(context)) {
                try {
                    val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                    val imageLink = Uri.parse(url)
                    val request = DownloadManager.Request(imageLink)

                    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
                        .setMimeType("image/png")
                        .setAllowedOverRoaming(true)
                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                        .setTitle(fileName)
                        .addRequestHeader("Authorization", "Bearer X0vjEUN6KRlxbp2DoUkyHeM0VOmxY91rA6BbU5j3Xu6wDodwS0McmilLPBWDUcJ1")
                        .setDestinationInExternalPublicDir(
                            Environment.DIRECTORY_DOWNLOADS,
                            File.separator + fileName + ".png"
                        )

                    downloadManager.enqueue(request)
                    context.toast("Icon Download Successful")
                }
                catch (e: Exception) {
                    context.toast("Icon Download Failed")
                }
            }
            else {
                askForPermission(context as Activity)
            }
        }
    }
}
