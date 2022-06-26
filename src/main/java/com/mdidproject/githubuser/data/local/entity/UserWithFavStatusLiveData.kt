package com.mdidproject.githubuser.data.local.entity

import androidx.lifecycle.LiveData

class UserWithFavStatusLiveData(username: String, avatar: String, isFav: Boolean, val isFavLiveData: LiveData<Boolean>): UserEntity(username, avatar, isFav)