package com.mdidproject.githubuser.interfaces

interface ItemAdapterCallback<T> {
    fun onItemClicked(data: T)
}