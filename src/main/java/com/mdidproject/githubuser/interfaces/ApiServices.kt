package com.mdidproject.githubuser.interfaces

import UserListResponse
import com.mdidproject.githubuser.response.UserConnectionResponse
import com.mdidproject.githubuser.response.UserResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiServices {
    @GET("search/users?q={username}")
    @Headers("Authorization: token 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")
    fun searchUsers(
        @Path("username") username: String
    ): Call<UserListResponse>

    @GET("users/{username}")
    @Headers("Authorization: token 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")
    fun getUser(
        @Path("username") username: String
    ): Call<UserResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: token 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")
    fun getUserFollowers(
        @Path("username") username: String
    ): Call<UserConnectionResponse>

    @GET("users/{username}/following")
    @Headers("Authorization: token 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")
    fun getUserFollowing(
        @Path("username") username: String
    ): Call<UserConnectionResponse>
}