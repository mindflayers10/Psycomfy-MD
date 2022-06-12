package com.udinn.prcomfy.response

import com.google.gson.annotations.SerializedName

data class UploadsResponse(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)
