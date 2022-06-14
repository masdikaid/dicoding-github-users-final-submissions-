package com.mdidproject.githubuser.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mdidproject.githubuser.api.GithubApi
import com.mdidproject.githubuser.response.UserItem
import com.mdidproject.githubuser.response.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel: ViewModel() {
    private val _user = MutableLiveData<UserResponse>()
    val user: LiveData<UserResponse> = _user

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        setNoError()
    }

    fun getUser(username: String){
        _isLoading.value = true
        val request = GithubApi.getServiceApi().getUser(username)
        request.enqueue(object: Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                _isLoading.value = false
                if (response.isSuccessful){
                    setNoError()
                    response.body()?.let {
                        _user.value = it
                    }
                }else{
                    setError("Failed Loading Data")
                    Log.e("Detail User", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
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
