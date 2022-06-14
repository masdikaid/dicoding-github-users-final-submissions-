package com.mdidproject.githubuser.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mdidproject.githubuser.api.GithubApi
import com.mdidproject.githubuser.response.UserConnectionResponse
import com.mdidproject.githubuser.response.UserItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersViewModel: ViewModel() {
    private val _followers = MutableLiveData<List<UserItem>>()
    val followers: LiveData<List<UserItem>> = _followers

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        setNoError()
    }

    fun getFollowers(username: String){
        _isLoading.value = true
        val request = GithubApi.getServiceApi().getUserFollowers(username)
        request.enqueue(object: Callback<List<UserItem>> {
            override fun onResponse(call: Call<List<UserItem>>, response: Response<List<UserItem>>) {
                _isLoading.value = false
                if (response.isSuccessful){
                    setNoError()
                    response.body()?.let {
                        _followers.postValue(it)
                    }
                }else{
                    setError("Failed Loading Data")
                    Log.e("Detail User", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<UserItem>>, t: Throwable) {
                _isLoading.value = false
                setError("Failed Loading Data")
                Log.e("Detail User", "onFailure: ${t.message.toString()}")
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