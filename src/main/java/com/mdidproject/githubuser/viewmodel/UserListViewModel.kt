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

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        setNoError()
        if (type === FAMOUS){
            getFamouseUsers()
        }
    }

    private fun getFamouseUsers(){
        _userList.value = GithubApi.getFamousUsers().users
        _isLoading.value = false
        setNoError()
    }

    fun searchUsers(username: String){
        _isLoading.value = true
        val request = GithubApi.getServiceApi().searchUsers(username)
        request.enqueue(object: Callback<UserListResponse>{
            override fun onResponse(call: Call<UserListResponse>, response: Response<UserListResponse>) {
                _isLoading.value = false
                if (response.isSuccessful){
                    setNoError()
                    _userList.value = response.body()?.users
                }else{
                    setError("Failed Loading Data")
                    Log.e("Search Users", "failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserListResponse>, t: Throwable) {
                setError("Failed Loading Data")
                Log.e("Search Users", "onFailure: ${t.message.toString()}")
            }

        })
    }

    private fun setNoError(){
        _isError.value = false
        _errorMessage.value = ""
    }

    private fun setError(err: String){
        _isError.value = true
        _errorMessage.value = err
    }
}
