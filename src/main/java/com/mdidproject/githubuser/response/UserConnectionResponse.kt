package com.mdidproject.githubuser.response

import com.google.gson.annotations.SerializedName

data class UserConnectionResponse(
	@field:SerializedName("UserConnectionResponse")
	val users: List<UserItem>
)

