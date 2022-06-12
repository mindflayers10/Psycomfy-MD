package com.udinn.prcomfy.utils

import android.media.MediaRecorder
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udinn.prcomfy.api.RetrofitInstance
import com.udinn.prcomfy.response.UploadsResponse
import okhttp3.MultipartBody
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun uploads(
        token: String,
        fileMultipart: MultipartBody.Part,
        callback: ApiCallbackString
    ) {
        _isLoading.value = true
        val client = RetrofitInstance.getApiService().uploadsRecord(token, fileMultipart)
        client.enqueue(object : Callback<UploadsResponse> {
            override fun onResponse(
                call: Call<UploadsResponse>,
                response: Response<UploadsResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        callback.onResponse(response.body() != null, SUCCESS)
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")

                    // get message error
                    val jsonObject =
                        JSONTokener(response.errorBody()!!.string()).nextValue() as JSONObject
                    val message = jsonObject.getString("message")
                    callback.onResponse(false, message)
                }
            }

            override fun onFailure(call: Call<UploadsResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
                callback.onResponse(false, t.message.toString())
            }
        })

    }

    companion object {
        private const val TAG = "listViewModel"
        private const val SUCCESS = "File uploaded successfully"
    }
}