package com.mdidproject.githubuser.api

import UserListResponse
import androidx.viewbinding.BuildConfig
import com.mdidproject.githubuser.interfaces.ApiServices
import com.mdidproject.githubuser.response.UserItem
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GithubApi {
    companion object {
        fun getServiceApi(): ApiServices {
            val loggingInterceptor = if(BuildConfig.DEBUG) {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            } else {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiServices::class.java)
        }

        fun getFamousUsers(): UserListResponse {
            val userItem = listOf<UserItem>(
                UserItem("https://avatars.githubusercontent.com/u/66577?v=4", "JakeWharton"),
                UserItem("https://avatars.githubusercontent.com/u/9877145?v=4", "amitshekhariitbhu"),
                UserItem("https://avatars.githubusercontent.com/u/869684?v=4", "romainguy"),
                UserItem("https://avatars.githubusercontent.com/u/227486?v=4", "chrisbanes"),
                UserItem("https://avatars.githubusercontent.com/u/1521451?v=4", "tipsy"),
                UserItem("https://avatars.githubusercontent.com/u/497670?v=4", "ravi8x"),
                UserItem("https://avatars.githubusercontent.com/u/363917?v=4", "jasoet"),
                UserItem("https://avatars.githubusercontent.com/u/2031493?v=4", "budioktaviyan"),
                UserItem("https://avatars.githubusercontent.com/u/3713580?v=4", "hendisantika"),
                UserItem("https://avatars.githubusercontent.com/u/4090245?v=4", "sidiqpermana"),
            )
            return UserListResponse(userItem.size, false, userItem)
        }
    }
}