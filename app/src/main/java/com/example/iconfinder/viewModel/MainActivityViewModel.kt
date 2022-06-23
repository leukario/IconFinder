package com.example.iconfinder.viewModel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.iconfinder.MyApplication
import com.example.iconfinder.model.ApiResponse
import com.example.iconfinder.model.Icon
import com.example.iconfinder.network.COUNT
import com.example.iconfinder.network.IconFinderService
import com.example.iconfinder.network.START_INDEX
import com.example.iconfinder.network.isLoading
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


class MainActivityViewModel(application: Application) : AndroidViewModel(application){
@Inject
lateinit var mService:IconFinderService

    private var recentQuery = "default"
    private var recentCount = 0
    private var recentIndex = 0
    private var latestData = MutableLiveData<List<Icon>>()

    init {
      (application as MyApplication).getRetroComponent().inject(this)
       latestData=MutableLiveData()
}

    fun getIcons(query: String, count: Int, index: Int): LiveData<List<Icon>> {
        if (query == recentQuery && recentCount == count && recentIndex == index) {
            return latestData
        }

        recentQuery = query
        recentCount = count
        recentIndex = index

        latestData = getIconsR(query, count, index)

        return latestData

    }
    private fun getIconsR(query: String, count: Int, index: Int): MutableLiveData<List<Icon>> {
        val iconsLiveData = MutableLiveData<List<Icon>>()
        isLoading = true
       CoroutineScope(Dispatchers.IO).launch {
            val request: Response<ApiResponse> = mService.getIcons(query, params(count, index))

            if (request.isSuccessful) {
                isLoading = false
                iconsLiveData.postValue(request.body()?.icons)
            } else {
                 iconsLiveData.postValue(null)
            }
        }

        return iconsLiveData
    }


    private fun params(count: Int, index: Int): Map<String, String> =
    mapOf(
        COUNT to count.toString(),
        START_INDEX to index.toString(),
    )
}

