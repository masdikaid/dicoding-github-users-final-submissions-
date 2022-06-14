package com.mdidproject.githubuser.interfaces

import android.view.View

interface ItemAdapterCallback<T> {
    fun onItemClicked(view: View?,data: T)
}