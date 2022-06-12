package com.mdidproject.githubuser.viewmodel

import UserListResponse
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mdidproject.githubuser.api.GithubApi
import com.mdidproject.githubuser.response.UserItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class UserListViewModel(type: String = FAMOUS): ViewModel() {
    companion object {
        const val SEARCH = "search"
        const val FAMOUS = "famous"
    }
    private val _userList = MutableLiveData<List<UserItem>>()
    val userList: LiveData<List<UserItem>> = _userList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        if (type === FAMOUS){
            getFamouseUsers()
        }
    }

    private fun getFamouseUsers(){
        _userList.value = GithubApi.getFamousUsers().users
        _isLoading.value = false
    }

    fun searchUsers(username: String){
        _isLoading.value = true
        val request = GithubApi.getServiceApi().searchUsers(username)
        request.enqueue(object: Callback<UserListResponse>{
            override fun onResponse(call: Call<UserListResponse>, response: Response<UserListResponse>) {
                _isLoading.value = false
                if (response.isSuccessful){
                    _userList.value = response.body()?.users
                }else{
                    Log.e("Search Users", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserListResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("Search Users", "onFailure: ${t.message.toString()}")
            }

        })
    }
}
