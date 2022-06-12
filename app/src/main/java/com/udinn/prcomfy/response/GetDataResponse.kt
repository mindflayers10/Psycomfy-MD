package com.udinn.prcomfy.response

import com.google.gson.annotations.SerializedName

data class GetDataResponse(

	@field:SerializedName("data")
	val data: DataResult,

	@field:SerializedName("error")
	val error: Boolean
)

data class DataResult(

	@field:SerializedName("result")
	val result: String?= null,

	@field:SerializedName("report_id")
	val reportId: String,

	@field:SerializedName("audio_url")
	val audioUrl: String,

	@field:SerializedName("id")
	val id: Int
)
