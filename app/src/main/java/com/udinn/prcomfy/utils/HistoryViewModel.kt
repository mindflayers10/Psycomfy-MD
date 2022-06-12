package com.udinn.prcomfy.utils

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udinn.prcomfy.api.RetrofitInstance
import com.udinn.prcomfy.response.DataResult
import com.udinn.prcomfy.response.GetDataResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _listData = MutableLiveData<GetDataResponse>()
    val listData: LiveData<GetDataResponse> = _listData

    lateinit var dataitem: DataResult

    init {
        getData()
    }

    fun getData() {
        _isLoading.value = true
        val client = RetrofitInstance.getApiService().getResult()
        client.enqueue(object : Callback<GetDataResponse> {
            override fun onResponse(
                call: Call<GetDataResponse>,
                response: Response<GetDataResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listData.postValue(response.body())

                } else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GetDataResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }
}