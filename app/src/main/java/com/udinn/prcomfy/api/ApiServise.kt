package com.udinn.prcomfy.api

import android.media.MediaRecorder
import com.udinn.prcomfy.login.ResponseLogin
import com.udinn.prcomfy.response.DataResult
import com.udinn.prcomfy.response.GetDataResponse
import com.udinn.prcomfy.response.RegisterResponse
import com.udinn.prcomfy.response.UploadsResponse
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiServise {
    @FormUrlEncoded
    @POST("signin")
    fun userLogin(
        @Field("email")email:String,
        @Field("password")password:String
    ):Call<ResponseLogin>

    @FormUrlEncoded
    @POST("signup")
    fun userRegister(
        @Field("email") email : String,
        @Field("password") password : String,
        @Field("confirm_password") confirm_password : String,
    ): Call<RegisterResponse>

    @Multipart
    @POST("api/uploads")
    fun uploadsRecord(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,

    ): Call<UploadsResponse>

    @GET("report/8f5bb879-6149-4880-9cdc-5a0a6ca7364e_202206120733")
    fun getResult(

    ) : Call<GetDataResponse>

}

class RetrofitInstance {
    companion object{
        fun getApiService(): ApiServise {
            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://c22-ps203-capstone-352016.et.r.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiServise::class.java)
        }
    }
}