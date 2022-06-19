package com.example.iconfinder



import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.iconfinder.model.ApiResponse
import com.example.iconfinder.model.Icon
import com.example.iconfinder.netowrk.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Request
import retrofit2.Response



object Repository     {

    private val coroutineScope = Dispatchers.IO
    private val iconsLiveData = MutableLiveData<List<Icon>>()

    fun getIcons(query: String, count: Int, index: Int): MutableLiveData<List<Icon>> {
        isLoading = true

val job = CoroutineScope(coroutineScope).launch {
            val request: Response<ApiResponse> = retrofitClient.getIcons(query,params(count,index))
            if (request.isSuccessful) {
                isLoading = false
               Log.d("Accepted","Request Accepted")
                iconsLiveData.postValue(request.body()?.icons)
            } else {
                Log.d("Rejected","Request Rejected")
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